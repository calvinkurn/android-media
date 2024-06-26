package com.tokopedia.accountprofile.settingprofile.addpin.view.fragment

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.pin.PinUnify
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.addpin.data.CheckPinData
import com.tokopedia.accountprofile.settingprofile.addpin.data.SkipOtpPinData
import com.tokopedia.accountprofile.settingprofile.addpin.viewmodel.AddChangePinViewModel
import com.tokopedia.accountprofile.common.ColorUtils
import com.tokopedia.accountprofile.common.LoadingDialog
import com.tokopedia.accountprofile.common.ProfileCompletionUtils.removeErrorCode
import com.tokopedia.accountprofile.common.analytics.TrackingPinConstant
import com.tokopedia.accountprofile.common.analytics.TrackingPinUtil
import com.tokopedia.accountprofile.common.model.CheckPinV2Data
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-08-30.
 * ade.hadian@tokopedia.com
 */

open class AddPinFragment : BaseDaggerFragment() {

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var loadingDialog: LoadingDialog? = null

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val addChangePinViewModel by lazy { viewModelProvider.get(AddChangePinViewModel::class.java) }

    private var isConfirmPin = false
    private var isSkipOtp: Boolean = false
    private var validateToken: String = ""

    private var initialPin = ""

    private var inputPin: PinUnify? = null
    private var methodIcon: ImageUnify? = null
    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_pin, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = context?.let { LoadingDialog(it) }
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputPin = view.findViewById(R.id.pin)
        methodIcon = view.findViewById(R.id.method_icon)
        mainView = view.findViewById(R.id.container)

        initVar()
        displayInitPin()

        methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER
        methodIcon?.setImage(R.drawable.ic_lock_green, 0f)
        inputPin?.pinTextField?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == PIN_LENGTH) {
                    if (isConfirmPin) {
                        if (s.toString() == initialPin) {
                            initialPin = s.toString()

                            if (isSkipOtp) {
                                addChangePinViewModel.checkSkipOtpPin(validateToken)
                            } else {
                                goToVerificationActivity()
                            }
                        } else {
                            val errorMessage = getString(R.string.error_wrong_pin)
                            trackingPinUtil.trackFailedInputConfirmationPin(errorMessage)
                            displayErrorPin(errorMessage)
                        }
                    } else {
                        checkPinMediator(s.toString())
                    }
                } else {
                    hideErrorPin()
                }
            }
        })

        initObserver()
    }

    private fun checkPinMediator(pin: String) {
        addChangePinViewModel.checkPinV2(pin)
    }

    override fun onStart() {
        super.onStart()
        trackingPinUtil.trackScreen(screenName)
    }

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun getScreenName(): String =
        if (!isConfirmPin) TrackingPinConstant.Screen.SCREEN_POPUP_PIN_INPUT
        else TrackingPinConstant.Screen.SCREEN_POPUP_PIN_CONFIRMATION

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    private fun initObserver() {
        addChangePinViewModel.addPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessAddPin(it.data.success)
                is Fail -> onErrorAddPin(it.throwable)
            }
        })

        addChangePinViewModel.mutatePin.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessAddPin(it.data.success)
                is Fail -> onErrorAddPin(it.throwable)
            }
        })

        addChangePinViewModel.checkPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessCheckPin(it.data)
                is Fail -> onErrorCheckPin(it.throwable)
            }
        })

        addChangePinViewModel.checkPinV2Response.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessCheckPinV2(it.data)
                is Fail -> onErrorCheckPin(it.throwable)
            }
        })

        addChangePinViewModel.skipOtpPinResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessSkipOtp(it.data)
                is Fail -> onErrorSkipOtpPin(it.throwable)
            }
        })
    }

    private fun initVar() {
        isSkipOtp =
            arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, false) ?: false
        validateToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN, "").toString()
    }

    private fun goToVerificationActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)

        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_CODE_COTP_PHONE_VERIFICATION)
    }

    open fun onSuccessAddPin(isSuccess: Boolean) {
        dismissLoading()
        if (isSuccess) {
            trackingPinUtil.trackSuccessInputConfirmationPin()
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.ADD_PIN_COMPLETE)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            intent.putExtra(
                ApplinkConstInternalGlobal.PARAM_SOURCE,
                PinCompleteFragment.SOURCE_ADD_PIN
            )
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun onSuccessCheckPin(checkPinData: CheckPinData) {
        when {
            checkPinData.valid -> {
                trackingPinUtil.trackSuccessInputCreatePin()
                displayConfirmPin()
                trackingPinUtil.trackScreen(screenName)
            }
            checkPinData.errorMessage.isNotEmpty() -> {
                trackingPinUtil.trackFailedInputCreatePin(checkPinData.errorMessage)
                displayErrorPin(checkPinData.errorMessage)
            }
        }
    }

    private fun onSuccessCheckPinV2(checkPinV2Data: CheckPinV2Data) {
        when {
            checkPinV2Data.valid -> {
                trackingPinUtil.trackSuccessInputCreatePin()
                displayConfirmPin()
                trackingPinUtil.trackScreen(screenName)
            }
            checkPinV2Data.errorMessage.isNotEmpty() -> {
                trackingPinUtil.trackFailedInputCreatePin(checkPinV2Data.errorMessage)
                displayErrorPin(checkPinV2Data.errorMessage)
            }
        }
    }

    private fun onSuccessSkipOtp(skipOtpPinData: SkipOtpPinData) {
        if (skipOtpPinData.skipOtp && skipOtpPinData.validateToken.isNotEmpty()) {
            showLoading()
            addPinMediator(skipOtpPinData.validateToken)
        } else {
            goToVerificationActivity()
        }
    }

    private fun onErrorAddPin(throwable: Throwable) {
        dismissLoading()
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        trackingPinUtil.trackFailedInputConfirmationPin(errorMessage.removeErrorCode())
        onError(throwable)
        displayInitPin()
    }

    private fun onErrorCheckPin(throwable: Throwable) {
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        trackingPinUtil.trackFailedInputCreatePin(errorMessage.removeErrorCode())
        onError(throwable)
    }

    private fun onErrorSkipOtpPin(throwable: Throwable) {
        val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
        trackingPinUtil.trackFailedInputConfirmationPin(errorMessage.removeErrorCode())
        onError(throwable)
    }

    private fun onError(throwable: Throwable) {
        mainView?.run {
            val errorMessage = ErrorHandlerSession.getErrorMessage(context, throwable)
            Toaster.make(this, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_COTP_PHONE_VERIFICATION &&
            resultCode == Activity.RESULT_OK
        ) {
            data?.extras?.run {
                val uuid = this.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                if (uuid.isNotEmpty()) {
                    showLoading()
                    addPinMediator(uuid)
                }
            }
        } else {
            trackingPinUtil.trackFailedInputConfirmationPin(getString(R.string.error_from_cotp))
        }
    }

    private fun displayInitPin() {
        hideErrorPin()
        inputPin?.pinTitle = getString(R.string.create_pin)
        inputPin?.pinDescription = getString(R.string.desc_create_pin)
        inputPin?.pinMessage = getString(R.string.message_create_pin)
        isConfirmPin = false
        initialPin = ""
    }

    private fun displayConfirmPin() {
        hideErrorPin()
        inputPin?.pinTitle = getString(R.string.confirm_create_pin)
        inputPin?.pinDescription = getString(R.string.subtitle_confirm_create_pin)
        isConfirmPin = true
        if (inputPin?.pinTextField?.text.toString().isNotEmpty())
            initialPin = inputPin?.pinTextField?.text.toString()
        inputPin?.value = ""
        inputPin?.pinMessage = ""
    }

    private fun displayErrorPin(error: String) {
        inputPin?.isError = true
        mainView?.run {
            Toaster.make(this, error, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    private fun hideErrorPin() {
        inputPin?.isError = false
    }

    private fun showLoading() {
        loadingDialog?.show()
        hideKeyboard()
    }

    protected fun dismissLoading() {
        loadingDialog?.dismiss()
        showKeyboard()
    }

    private fun showKeyboard() {
        inputPin?.pinTextField?.let { view ->
            view.post {
                if (view.requestFocus() && context != null) {
                    val systemService = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
                    if (systemService != null && systemService is InputMethodManager) {
                        systemService.showSoftInput(view, InputMethodManager.SHOW_FORCED)
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    open fun onBackPressedFromConfirm(): Boolean {
        return if (isConfirmPin) {
            trackingPinUtil.trackClickBackButtonConfirmation()
            displayInitPin()
            trackingPinUtil.trackScreen(screenName)
            true
        } else {
            trackingPinUtil.trackClickBackButtonInput()
            false
        }
    }

    private fun addPinMediator(validateToken: String) {
        addChangePinViewModel.addPinV2(validateToken)
    }

    companion object {

        const val REQUEST_CODE_COTP_PHONE_VERIFICATION = 101
        const val OTP_TYPE_PHONE_VERIFICATION = 124

        const val PIN_LENGTH = 6
        fun createInstance(bundle: Bundle): AddPinFragment {
            val fragment = AddPinFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
