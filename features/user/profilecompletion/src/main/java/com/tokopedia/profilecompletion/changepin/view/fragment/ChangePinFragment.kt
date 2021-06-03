package com.tokopedia.profilecompletion.changepin.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.pin.PinUnify
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.CheckPinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinData
import com.tokopedia.profilecompletion.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.profilecompletion.changepin.view.activity.ChangePinActivity
import com.tokopedia.profilecompletion.changepin.view.viewmodel.ChangePinViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.common.LoadingDialog
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant
import com.tokopedia.profilecompletion.common.analytics.TrackingPinUtil
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
open class ChangePinFragment : BaseDaggerFragment(), CoroutineScope {

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var loadingDialog: LoadingDialog

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    protected val changePinViewModel by lazy { viewModelProvider.get(ChangePinViewModel::class.java) }

    protected var isConfirm = false
    protected var isValidated = false
    protected var inputNewPin = false
    private val job = Job()

    protected var pin = ""
    private var oldPin = ""
    protected var source = 0

    private var changePinInput: PinUnify? = null
    private var methodIcon: ImageUnify? = null
    private var mainView: View? = null

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val onForgotPinClick = View.OnClickListener {
        forgotPinState()
    }

    override fun getScreenName(): String = TrackingPinConstant.Screen.SCREEN_POPUP_PIN_INPUT

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_change_pin, container, false)
        changePinInput = view.findViewById(R.id.pin)
        methodIcon = view.findViewById(R.id.method_icon)
        mainView = view.findViewById(R.id.container)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initViews() {
        methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER
        methodIcon?.setImage(R.drawable.ic_lock_green, 0f)
        toggleForgotText(false)
        changePinInput?.pinTextField?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                handleInputPin(s.toString())
            }
        })
    }

    open fun handleInputPin(text: String) {
        if (text.length == PIN_LENGTH) {
            when {
                isConfirm -> handleConfirmState(text)
                inputNewPin -> handleInputNewPinState(text)
                isValidated -> handleValidatedAndForgotState(text)
                else -> {
                    changePinViewModel.validatePin(text)
                }
            }
        } else {
            changePinInput?.isError = false
        }
    }

    open fun handleValidatedAndForgotState(input: String) {
        pin = input
        konfirmasiState()
    }

    open fun handleConfirmState(input: String) {
        if (pin == input) {
            showLoading()
            changePinViewModel.changePin(pin, input, oldPin)
        } else {
            displayErrorPin(getString(R.string.error_wrong_pin))
        }
    }

    open fun handleInputNewPinState(input: String) {
        changePinViewModel.checkPin(input)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_COTP_PHONE_VERIFICATION) {
            val validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
            showLoading()
            changePinViewModel.resetPin(validateToken)
        }
    }

    open fun goToVerificationActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle().apply {
            putString(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
            putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
            putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
            putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        }
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_COTP_PHONE_VERIFICATION)
    }

    private fun showLoading() {
        loadingDialog.show()
        hideKeyboard()
    }

    private fun dismissLoading() {
        loadingDialog.dismiss()
        showKeyboard()
    }

    private fun showKeyboard() {
        changePinInput?.pinTextField?.let { view ->
            view.post {
                if (view.requestFocus()) {
                    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
                }
            }
        }
    }

    private fun hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    private fun konfirmasiState() {
        resetInputPin()
        isConfirm = true
        changePinInput?.pinTitle = getString(R.string.confirm_create_pin)
        changePinInput?.pinDescription = getString(R.string.subtitle_confirm_create_pin)
        changePinInput?.pinMessage = ""
        changePinInput?.pinSecondaryActionText = ""
    }

    protected fun forgotPinState() {
        arguments?.let { (activity as ChangePinActivity).goToForgotPin(it) }
    }

    open fun inputNewPinState() {
        inputNewPin = true
        resetInputPin()
        changePinInput?.pinTitle = getString(R.string.change_pin_title_new_pin)
        changePinInput?.pinDescription = getString(R.string.change_pin_subtitle_new_pin)
        changePinInput?.pinSecondaryActionText = ""
        toggleForgotText(true)
    }

    private fun resetInputPin() {
        hideErrorPin()
        changePinInput?.pinTextField?.setText("")
    }

    private fun toggleForgotText(isForgot: Boolean) {
        if (isForgot) {
            changePinInput?.pinMessage = getString(R.string.change_pin_avoid_info)
        } else {
            changePinInput?.pinSecondaryActionView?.apply {
                text = getString(R.string.change_pin_forgot_btn)
                setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                setOnClickListener(onForgotPinClick)
                isEnabled = true
            }
        }
    }

    private fun onSuccessCheckPin(checkPinData: CheckPinData) {
        if (checkPinData.valid) {
            if (inputNewPin) {
                pin = changePinInput?.pinTextField?.text.toString()
                inputNewPin = false
                konfirmasiState()
            } else inputNewPinState()
        } else {
            displayErrorPin(checkPinData.errorMessage)
        }
    }

    private fun displayErrorPin(error: String) {
        changePinInput?.isError = true
        mainView?.run {
            Toaster.make(this, error, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    private fun hideErrorPin() {
        changePinInput?.isError = false
    }

    private fun onError(throwable: Throwable?) {
        dismissLoading()
        mainView?.run {
            val errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
            Toaster.make(this, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    private fun onSuccessValidatePin(data: ValidatePinData) {
        isValidated = data.valid
        oldPin = changePinInput?.pinTextField?.text.toString()
        if (data.valid) {
            inputNewPinState()
        } else {
            displayErrorPin(data.errorMessage)
        }
    }

    private fun onErrorValidatePin(error: Throwable) {
        oldPin = ""
        isValidated = false
        onError(error)
    }

    open fun goToSuccessPage() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN_COMPLETE).apply {
            flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            if(source < 1) {
                source = PinCompleteFragment.SOURCE_CHANGE_PIN
            }
            putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
            source = 0
        }
        startActivity(intent)
        activity?.finish()
    }

    private fun onSuccessResetPin(data: AddChangePinData) {
        dismissLoading()
        if (data.success) goToSuccessPage()
        else onError(Throwable())
    }

    private fun onSuccessChangePin(data: AddChangePinData) {
        dismissLoading()
        if (data.success) goToSuccessPage()
        else onError(Throwable())
    }

    private fun initObserver() {
        changePinViewModel.resetPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessResetPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

        changePinViewModel.resetPin2FAResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    dismissLoading()
                    if (it.data.is_success == 1) goToSuccessPage()
                    else onError(Throwable())
                }
                is Fail -> onError(it.throwable)
            }
        })

        changePinViewModel.validatePinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessValidatePin(it.data)
                is Fail -> onErrorValidatePin(it.throwable)
            }
        })

        changePinViewModel.checkPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessCheckPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

        changePinViewModel.changePinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessChangePin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

    }

    companion object {
        const val REQUEST_CODE_COTP_PHONE_VERIFICATION = 101
        const val OTP_TYPE_PHONE_VERIFICATION = 125
        const val PIN_LENGTH = 6

        fun createInstance(bundle: Bundle): ChangePinFragment {
            val fragment = ChangePinFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
