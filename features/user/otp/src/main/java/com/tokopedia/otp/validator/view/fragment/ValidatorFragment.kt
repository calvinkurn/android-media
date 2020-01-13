package com.tokopedia.otp.validator.view.fragment

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AlertDialog
import android.text.*
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Screen.SCREEN_ACCOUNT_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorUtil
import com.tokopedia.otp.common.design.PinInputEditText
import com.tokopedia.otp.validator.data.*
import com.tokopedia.otp.validator.data.ModeListData
import com.tokopedia.otp.validator.data.OtpRequestData
import com.tokopedia.otp.validator.data.OtpValidateData

import com.tokopedia.otp.validator.di.ValidatorComponent
import com.tokopedia.otp.validator.viewmodel.ValidatorViewModel
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_cotp_miscall_verification.*
import kotlinx.android.synthetic.main.fragment_validator.*
import java.util.concurrent.TimeUnit
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

    private lateinit var verifyButton: UnifyButton
    private lateinit var inputVerifyCode: PinInputEditText
    private lateinit var footer: TextView
    private lateinit var errorImage: ImageView
    private lateinit var registerIcon: ImageView
    private lateinit var errorOtp: TextView
    private lateinit var activationText: TextView
    private lateinit var parent: View
    private lateinit var progressBar: ProgressBar

    private var otpParams = OtpParams()
    private var modeListData = ModeListData()

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var cacheHandler: LocalCacheHandler

    private var isRunningTimer = false

    override fun getScreenName(): String = SCREEN_ACCOUNT_ACTIVATION

    override fun initInjector() {
        getComponent(ValidatorComponent::class.java).inject(this)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cacheHandler = LocalCacheHandler(activity, CACHE_VALIDATOR)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_validator, container, false)
        verifyButton = view.findViewById(R.id.verify_button)
        inputVerifyCode = view.findViewById(R.id.input_verify_code)
        footer = view.findViewById(R.id.footer)
        errorImage = view.findViewById(R.id.error_image)
        registerIcon = view.findViewById(R.id.register_icon)
        errorOtp = view.findViewById(R.id.error_otp)
        activationText = view.findViewById(R.id.activation_text)
        parent = view.findViewById(R.id.parent)
        progressBar = view.findViewById(R.id.progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
        requestCode(false)

        verifyButton.setOnClickListener {
            analytics.trackClickActivationButton()
            validateCode()
        }

        footer.setOnClickListener {
            analytics.trackClickResendButton()
            resendDialog(otpParams.email)
        }

        inputVerifyCode.requestFocus()
        inputVerifyCode.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) { }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == modeListData.otpDigit) {
                    validateCode()
                    verifyButton.isEnabled = true
                }else{
                    verifyButton.isEnabled = false
                }
            }

        })

        inputVerifyCode.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                validateCode()
                true
            }else false
        }

        errorImage.setOnClickListener {
            inputVerifyCode.setText("")
            removeErrorOtp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    private fun prepareView(){
        activity?.let {
            initVar()
            initObserver()

            if (modeListData.otpListImgUrl.isNotEmpty()) {
                ImageHandler.LoadImage(registerIcon, modeListData.otpListImgUrl)
            }

            if (modeListData.afterOtpListTextHtml.isNotEmpty()) {
                setActivateTextFull(modeListData.afterOtpListTextHtml)
            }

            inputVerifyCode.setLength(modeListData.otpDigit)

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

        inputVerifyCode.requestFocus()
        inputVerifyCode.requestFocusFromTouch()
        showKeyboard()
    }

    private fun initVar() {
        arguments?.let {
            otpParams = it.getParcelable(OtpConstant.OTP_PARAMS) as OtpParams
            modeListData = it.getParcelable(OtpConstant.OTP_MODE_PARAM) as ModeListData
        }

        if (!isCountdownFinished()) {
            startTimer()
        } else {
            footer.show()
        }
    }

    private fun initObserver(){
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
                is Fail -> {
                    val message = it.throwable.message as String
                    if (message.contains(getString(R.string.job_was_cancelled))) {
                        onErrorOtpValidate(Throwable(getString(R.string.no_network_connection)))
                    } else {
                        onErrorOtpValidate(it.throwable)
                    }
                }
            }
        })
    }

    private fun startTimer() {
        if (isCountdownFinished()) {
            cacheHandler.putBoolean(HAS_TIMER, true)
            cacheHandler.setExpire(COUNTDOWN_LENGTH)
            cacheHandler.applyEditor()
        }

        if (!isRunningTimer) {
            countDownTimer = object : CountDownTimer((cacheHandler.remainingTime * INTERVAL).toLong(), INTERVAL.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    isRunningTimer = true
                    setRunningCountdownText(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toInt())
                }

                override fun onFinish() {
                    isRunningTimer = false
                    countDownText?.hide()
                    footer.show()
                }

            }.start()
        }
    }

    private fun requestCode(isResend: Boolean) {
        showLoading()
        validatorViewModel.otpRequestEmail(otpParams.otpType.toString(), otpParams.email, isResend, modeListData.otpDigit)
    }

    private fun validateCode() {
        verifyButton.isLoading = true
        validatorViewModel.otpValidateEmail(otpParams.otpType.toString(), inputVerifyCode.text.toString(), otpParams.email)
    }

    private fun onSuccessOtpRequest(otpRequestData: OtpRequestData){
        dismissLoading()
        startTimer()
        showKeyboard()
        inputVerifyCode.requestFocus()
        inputVerifyCode.requestFocusFromTouch()
    }

    private fun onErrorOtpRequest(throwable: Throwable){
        dismissLoading()
        inputVerifyCode.text.clear()
        view?.let {
            val error = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            NetworkErrorHelper.showEmptyState(context, it, error) {
                requestCode(true)
            }
        }
    }

    private fun onSuccessOtpResendRequest(otpRequestData: OtpRequestData){
        dismissLoading()
        removeErrorOtp()
        startTimer()
        showKeyboard()
        inputVerifyCode.requestFocus()
        inputVerifyCode.requestFocusFromTouch()
        analytics.trackSuccessClickResendButton()

        view?.let { it ->
            Toaster.make(it, getString(R.string.success_resend_activation))
        }
    }

    private fun onErrorOtpResendRequest(throwable: Throwable){
        dismissLoading()
        inputVerifyCode.text.clear()
        inputVerifyCode.requestFocus()
        inputVerifyCode.requestFocusFromTouch()
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
        dismissLoading()
        verifyButton.isLoading = false
        analytics.trackSuccessClickActivationButton()
        activity?.let {
            if(otpValidateData.validateToken.isEmpty()){
                it.setResult(Activity.RESULT_CANCELED)
                it.finish()
            }else{
                val intent = Intent()
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, otpParams.email)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, otpValidateData.validateToken)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, otpParams.source)
                it.setResult(Activity.RESULT_OK, intent)
                it.finish()
            }
        }
    }

    private fun onErrorOtpValidate(throwable: Throwable){
        dismissLoading()
        verifyButton.isLoading = false
        inputVerifyCode.requestFocus()
        inputVerifyCode.requestFocusFromTouch()
        activity?.let {
            throwable.message?.let { errorMessage ->
                analytics.trackFailedClickActivationButton(errorMessage)
                dismissLoading()
                if (errorMessage == "") {
                    NetworkErrorHelper.showSnackbar(it)
                } else {
                    NetworkErrorHelper.showSnackbar(it, errorMessage)
                }

                errorImage.visibility = View.VISIBLE
                errorOtp.visibility = View.VISIBLE
            }
        }
    }

    private fun resendDialog(email: String) {
        val dialogMessage = String.format(getString(R.string.message_resend_email_to), email)
        activity?.let {
            AlertDialog.Builder(it)
                    .setTitle(R.string.resend_activation_email)
                    .setMessage(MethodChecker.fromHtml(dialogMessage))
                    .setPositiveButton(android.R.string.yes) { _, _ ->
                        analytics.trackClickOkResendButton()
                        requestCode(true)
                    }
                    .setNegativeButton(R.string.cancel_dialog_change_email){ dialog, _ ->
                        analytics.trackFailedClickResendButton(
                                it.getString(R.string.change_email_error_condition))
                        dialog.dismiss()
                    }
                    .show()
        }
    }

    private fun setActivateTextFull(text: String){
        activity?.let {
            if(text.isNotEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activationText.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    activationText.text = Html.fromHtml(text)
                }
            }
            inputVerifyCode.requestFocus()
        }
    }

    private fun setRunningCountdownText(countdown: Int) {
        countDownText?.show()
        footer.hide()

        countDownText?.setTextColor(MethodChecker.getColor(activity, R.color.font_black_disabled_38))
        countDownText?.isEnabled = false
        val text = String.format(activity?.getString(R.string.validator_coundown_text) as String, countdown)
        countDownText?.text = MethodChecker.fromHtml(text)
    }

    private fun isCountdownFinished(): Boolean {
        return cacheHandler.isExpired || !cacheHandler.getBoolean(HAS_TIMER, false)
    }

    private fun removeErrorOtp() {
        errorOtp.visibility = View.INVISIBLE
        errorImage.visibility = View.INVISIBLE
    }

    private fun showLoading() {
        parent.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        parent.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun showKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(inputVerifyCode, InputMethodManager.SHOW_FORCED)
    }

    companion object {

        const val INTERVAL = 1000
        const val COUNTDOWN_LENGTH = 30
        const val CACHE_VALIDATOR = "cacheValidator"
        const val HAS_TIMER = "hasTimer"

        fun createInstance(otpParams: OtpParams, modeListData: ModeListData): Fragment {
            val bundle = Bundle()
            bundle.putParcelable(OtpConstant.OTP_PARAMS, otpParams)
            bundle.putParcelable(OtpConstant.OTP_MODE_PARAM, modeListData)

            val fragment = ValidatorFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}