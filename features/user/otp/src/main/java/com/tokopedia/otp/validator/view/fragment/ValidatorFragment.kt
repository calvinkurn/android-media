package com.tokopedia.otp.validator.view.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.*
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Screen.SCREEN_ACCOUNT_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorUtil
import com.tokopedia.otp.validator.data.ModeListData
import com.tokopedia.otp.validator.data.OtpModeListData
import com.tokopedia.otp.validator.data.OtpRequestData
import com.tokopedia.otp.validator.data.OtpValidateData
import com.tokopedia.otp.validator.di.ValidatorComponent
import com.tokopedia.otp.validator.viewmodel.ValidatorViewModel
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_validator.*
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

class ValidatorFragment: BaseDaggerFragment(){

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var analytics: TrackingValidatorUtil
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val validatorViewModel by lazy { viewModelProvider.get(ValidatorViewModel::class.java) }

    private var otpType = ""
    private var email = ""
    private var source = ""

    override fun getScreenName(): String = SCREEN_ACCOUNT_ACTIVATION

    override fun initInjector() {
        getComponent(ValidatorComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            if(userSession.isLoggedIn){
                it.setResult(Activity.RESULT_OK)
                it.finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_validator, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareView()

        verify_button.setOnClickListener {
            analytics.trackClickActivationButton()
            validatorViewModel.otpValidateEmail(otpType, input_verify_code.text.toString(), email)
        }

        footer.setOnClickListener {
            analytics.trackClickResendButton()
            showChangeEmailDialog(email)
        }

        input_verify_code.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 6) {
                    verify_button.buttonCompatType = ButtonCompat.PRIMARY
                }else{
                    verify_button.buttonCompatType = ButtonCompat.PRIMARY_DISABLED
                }
            }

        })

        input_verify_code.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                validatorViewModel.otpValidateEmail(otpType, input_verify_code.text.toString(), email)
                true
            }else false
        }

        error_image.setOnClickListener {
            input_verify_code.setText("")
            removeErrorOtp()
        }

        validatorViewModel.otpRequestEmail(otpType, email, false)
    }

    private fun prepareView(){
        activity?.let {
            initVar()
            initObserver()
            if(email.isNotEmpty() && otpType.isNotEmpty())
                showLoading()
                validatorViewModel.otpModeListEmail(otpType, email)

            val spannable = SpannableString(getString(R.string.validation_resend_email))
            spannable.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(view: View) {}

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = MethodChecker.getColor(it, R.color.tkpd_main_green)
                        }
                    },
                    getString(R.string.validation_resend_email).indexOf("Kirim"),
                    getString(R.string.validation_resend_email).length,
                    0
            )
            footer.setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    private fun initObserver(){
        validatorViewModel.otpModeListResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessOtpModeList(it.data)
                is Fail -> onErrorOtpModeList(it.throwable)
            }
        })
        validatorViewModel.otpRequestResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessOtpRequest(it.data)
                is Fail -> onErrorOtpRequest(it.throwable)
            }
        })
        validatorViewModel.otpResendRequestResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessOtpResendRequest(it.data)
                is Fail -> onErrorOtpResendRequest(it.throwable)
            }
        })
        validatorViewModel.otpValidateResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessOtpValidate(it.data)
                is Fail -> onErrorOtpValidate(it.throwable)
            }
        })
    }

    private fun initVar() {
        arguments?.let {
            otpType = it.getString(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, "")
            email = it.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
            source = it.getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode){
                    REQUEST_CHANGE_EMAIL_REGISTER -> {
                        data?.extras?.let {
                            email = it.getString(EXTRA_EMAIL, "")
                            setActivateText(email)
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessOtpModeList(modeListData: ModeListData){
        dismissLoading()
        if(modeListData.afterOtpListTextHtml.isNotEmpty())
            setActivateTextFull(modeListData.afterOtpListTextHtml)

        if(modeListData.otpListImgUrl.isNotEmpty())
            ImageHandler.LoadImage(register_icon, modeListData.otpListImgUrl)

    }

    private fun onErrorOtpModeList(throwable: Throwable){
        view?.let {
            val error = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            NetworkErrorHelper.showEmptyState(context, it, error) {
                validatorViewModel.otpModeListEmail(otpType, email)
            }
        }
    }

    private fun onSuccessOtpRequest(otpRequestData: OtpRequestData){

    }

    private fun onErrorOtpRequest(throwable: Throwable){

    }

    private fun onSuccessOtpResendRequest(otpRequestData: OtpRequestData){
        activity?.let {
            analytics.trackSuccessClickOkResendButton()
            analytics.trackSuccessClickResendButton()
            KeyboardHandler.DropKeyboard(it, input_verify_code)
            removeErrorOtp()
            dismissLoading()
            ToasterNormal.show(it, getString(R.string.success_resend_activation))
        }
    }

    private fun onErrorOtpResendRequest(throwable: Throwable){
        dismissLoading()
        throwable.message?.let {
            analytics.trackFailedClickOkResendButton(it)
            analytics.trackFailedClickResendButton(it)
            if (it.isEmpty()) {
                NetworkErrorHelper.showSnackbar(activity)
            } else {
                NetworkErrorHelper.showSnackbar(activity, it)
            }
        }
    }

    private fun onSuccessOtpValidate(otpValidateData: OtpValidateData){
        analytics.trackSuccessClickActivationButton()
        activity?.let {
            if(otpValidateData.validateToken.isEmpty()){
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            }else{
                val intent = Intent()
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, otpValidateData.validateToken)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
                it.setResult(Activity.RESULT_OK, intent)
                it.finish()
            }
        }
    }

    private fun onErrorOtpValidate(throwable: Throwable){
        activity?.let {
            throwable.message?.let { errorMessage ->
                analytics.trackFailedClickActivationButton(errorMessage)
                KeyboardHandler.DropKeyboard(it, input_verify_code)
                dismissLoading()
                if (errorMessage == "") {
                    NetworkErrorHelper.showSnackbar(it)
                } else {
                    NetworkErrorHelper.showSnackbar(it, errorMessage)
                }

                error_image.visibility = View.VISIBLE
                error_otp.visibility = View.VISIBLE
            }
        }
    }

    private fun goToChangeEmail(email: String){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_EMAIL_REGISTER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        startActivityForResult(intent, REQUEST_CHANGE_EMAIL_REGISTER)
    }

    private fun showChangeEmailDialog(email: String) {
        if (activity != null) {
            val dialogMessage = getString(R.string.message_resend_email_to) + " <b>" + email + "</b>"
            AlertDialog.Builder(activity!!)
                    .setTitle(R.string.resend_activation_email)
                    .setMessage(MethodChecker.fromHtml(dialogMessage))
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        analytics.trackClickOkResendButton()
                        validatorViewModel.otpRequestEmail(otpType, email, true)
                    }
                    .setNegativeButton(R.string.cancel_dialog_change_email){ dialog, which ->
                        analytics.trackFailedClickResendButton(
                                activity!!.getString(R.string.change_email_error_condition))
                        dialog.dismiss()
                    }
                    .show()
        }
    }

    private fun setActivateText(email: String){
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activation_text.text = Html.fromHtml(getString(R.string.validation_text).replace(
                        getString(R.string.param_email_validation_text),
                        email, false
                ), Html.FROM_HTML_MODE_COMPACT)
            } else {
                activation_text.text = Html.fromHtml(getString(R.string.validation_text).replace(
                        getString(R.string.param_email_validation_text),
                        email, false
                ))
            }

            input_verify_code.requestFocus()
            KeyboardHandler.DropKeyboard(it, input_verify_code)
        }
    }

    private fun setActivateTextFull(text: String){
        activity?.let {
            if(text.isNotEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activation_text.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    activation_text.text = Html.fromHtml(text)
                }
            }

            input_verify_code.requestFocus()
            KeyboardHandler.DropKeyboard(it, input_verify_code)
        }
    }

    private fun removeErrorOtp() {
        error_otp.visibility = View.INVISIBLE
        error_image.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        parent.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        parent.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    companion object {

        const val REQUEST_CHANGE_EMAIL_REGISTER = 200

        const val EXTRA_EMAIL = "EXTRA_EMAIL"

        fun createInstance(bundle: Bundle): ValidatorFragment {
            val fragment = ValidatorFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}