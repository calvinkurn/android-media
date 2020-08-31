package com.tokopedia.profilecompletion.changepin.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addpin.data.AddChangePinData
import com.tokopedia.profilecompletion.addpin.data.CheckPinData
import com.tokopedia.profilecompletion.addpin.data.ValidatePinData
import com.tokopedia.profilecompletion.addpin.view.fragment.PinCompleteFragment
import com.tokopedia.profilecompletion.changepin.view.activity.ChangePinActivity
import com.tokopedia.profilecompletion.changepin.view.viewmodel.ChangePinViewModel
import com.tokopedia.profilecompletion.common.analytics.TrackingPinConstant
import com.tokopedia.profilecompletion.common.analytics.TrackingPinUtil
import com.tokopedia.profilecompletion.customview.focus
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_change_pin.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ChangePinFragment : BaseDaggerFragment() {

    @Inject
    lateinit var trackingPinUtil: TrackingPinUtil
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val changePinViewModel by lazy { viewModelProvider.get(ChangePinViewModel::class.java) }

    private var isConfirm = false
    private var isValidated = false
    private var isForgotPin = false
    private var inputNewPin = false

    private var pin = ""
    private var oldPin = ""

    private val onForgotPinClick = View.OnClickListener {
        forgotPinState()
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

    override fun getScreenName(): String = TrackingPinConstant.Screen.SCREEN_POPUP_PIN_INPUT

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserver()
    }

    private fun initViews(){
        toggleForgotText(true)
        changePinInput.pinTextField.addTextChangedListener(object: TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {
                handleInputPin(s.toString())
            }
        })
    }

    private fun handleInputPin(text: String){
        if(text.length == PIN_LENGTH){
            when {
                isConfirm -> handleConfirmState(text)
                inputNewPin -> handleInputNewPinState(text)
                isValidated || isForgotPin -> handleValidatedAndForgotState(text)
                else -> changePinViewModel.validatePin(text)
            }
        }else {
            changePinInput.isError = false
            changePinInput.pinErrorMessageView.text = ""
        }
    }

    private fun handleValidatedAndForgotState(input: String){
        pin = input
        konfirmasiState()
    }

    private fun handleConfirmState(input: String){
        if(pin == input){
            if(isForgotPin) {
                goToVerificationActivity()
            } else {
                changePinViewModel.changePin(pin, input, oldPin)
            }
        }else{
            changePinInput.pinTextField.setText("")
            changePinInput.focus()
            displayErrorPin(getString(R.string.error_wrong_pin))
        }
    }

    private fun handleInputNewPinState(input: String){
        changePinViewModel.checkPin(input)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_COTP_PHONE_VERIFICATION){
            val validateToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
            changePinViewModel.resetPin(validateToken)
        }
    }

    private fun goToVerificationActivity(){
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
        mainView.visibility = View.GONE
        changePinProgressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        changePinProgressBar.visibility = View.GONE
    }

    private fun konfirmasiState(){
        resetInputPin()
        isConfirm = true
        changePinTitle.text = getString(R.string.confirm_create_pin)
        changePinSubtitle.text = getString(R.string.subtitle_confirm_create_pin)
        toggleForgotText(false)
    }

    private fun forgotPinState(){
        isForgotPin = true
        inputNewPinState()
        if(activity is ChangePinActivity) (activity as ChangePinActivity).supportActionBar?.title = resources.getString(R.string.change_pin_title_setting)
    }

    private fun inputNewPinState(){
        inputNewPin = true
        resetInputPin()
        changePinTitle.text = getString(R.string.change_pin_title_new_pin)
        changePinSubtitle.text = getString(R.string.change_pin_subtitle_new_pin)
        toggleForgotText(false)
    }

    private fun resetInputPin(){
        hideErrorPin()
        changePinInput.pinTextField.setText("")
    }

    private fun toggleForgotText(isForgot: Boolean){
        if(isForgot){
            changePinForgotBtn.apply {
                text = getString(R.string.change_pin_forgot_btn)
                setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Green_G500))
                setOnClickListener(onForgotPinClick)
                isEnabled = true
            }
        }else {
            changePinForgotBtn.apply {
                text = getString(R.string.change_pin_avoid_info)
                setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_44))
                isEnabled = false
            }
        }
    }

    private fun onSuccessCheckPin(checkPinData: CheckPinData){
        dismissLoading()
        if(checkPinData.valid){
            if(inputNewPin) {
                pin = changePinInput.pinTextField.text.toString()
                inputNewPin = false
                konfirmasiState()
            }
            else inputNewPinState()
        }else  {
            changePinInput.pinTextField.setText("")
            displayErrorPin(checkPinData.errorMessage)
        }
    }

    private fun displayErrorPin(error: String){
        changePinInput.isError = true
        changePinInput.pinErrorMessageView.text = error
    }

    private fun hideErrorPin(){
        changePinInput.isError = false
    }

    private fun onError(throwable: Throwable?){
        dismissLoading()
        view?.run{
            val errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
        changePinInput.focus()
    }

    private fun onSuccessValidatePin(data: ValidatePinData){
        isValidated = data.valid
        oldPin = changePinInput.pinTextField.text.toString()
        if(data.valid){
            inputNewPinState()
        }else{
            resetInputPin()
            changePinInput.focus()
            displayErrorPin(data.errorMessage)
        }
    }

    private fun onErrorValidatePin(error: Throwable){
        resetInputPin()
        oldPin = ""
        isValidated = false
        onError(error)
    }

    private fun goToSuccessPage(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN_COMPLETE).apply {
            flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, if(isForgotPin) PinCompleteFragment.SOURCE_FORGOT_PIN else PinCompleteFragment.SOURCE_CHANGE_PIN)
        }
        startActivity(intent)
        activity?.finish()
    }

    private fun onSuccessResetPin(data: AddChangePinData){
        if(data.success) goToSuccessPage()
        else onError(Throwable())
    }

    private fun onSuccessChangePin(data: AddChangePinData){
        if(data.success) goToSuccessPage()
        else onError(Throwable())
    }

    private fun initObserver(){
        changePinViewModel.resetPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessResetPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

        changePinViewModel.validatePinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessValidatePin(it.data)
                is Fail -> onErrorValidatePin(it.throwable)
            }
        })

        changePinViewModel.checkPinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessCheckPin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

        changePinViewModel.changePinResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessChangePin(it.data)
                is Fail -> onError(it.throwable)
            }
        })

    }

}
