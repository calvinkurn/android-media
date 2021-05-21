package com.tokopedia.otp.verification.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpConstant.Screen.SCREEN_ACCOUNT_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.common.VerificationPref
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.otp.verification.view.viewbinding.VerificationViewBinding
import com.tokopedia.otp.verification.viewmodel.VerificationViewModel
import com.tokopedia.pin.PinUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Ade Fulki on 02/06/20.
 */

open class VerificationFragment : BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingOtpUtil

    @Inject
    lateinit var verificationPref: VerificationPref

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var otpData: OtpData
    private lateinit var modeListData: ModeListData
    private lateinit var countDownTimer: CountDownTimer

    private var isRunningCountDown = false
    private var isFirstSendOtp = true
    protected var isMoreThanOneMethod = true

    private var tempOtp: CharSequence? = null
    private var indexTempOtp = 0
    private val delayAnimateText: Long = 350

    private var handler: Handler = Handler()

    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            tempOtp?.let {
                viewBound.pin?.value = it.subSequence(0, indexTempOtp++)
                if (indexTempOtp <= it.length) {
                    handler?.postDelayed(this, delayAnimateText)
                }
            }
        }
    }

    protected val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)
    }

    override val viewBound = VerificationViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)


    override fun getScreenName() = when (otpData.otpType) {
        OtpConstant.OtpType.REGISTER_EMAIL -> SCREEN_ACCOUNT_ACTIVATION
        else -> TrackingOtpConstant.Screen.SCREEN_COTP + modeListData.modeText
    }

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
        modeListData = arguments?.getParcelable(OtpConstant.OTP_MODE_EXTRA) ?: ModeListData()
        viewModel.isLoginRegisterFlow = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW)
                ?: false
        isMoreThanOneMethod = arguments?.getBoolean(OtpConstant.IS_MORE_THAN_ONE_EXTRA, true)
                ?: true
        activity?.runOnUiThread {
            handler = Handler()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
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
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    override fun onBackPressed(): Boolean {
        analytics.trackClickBackOtpPage(otpData.otpType)
        if (otpData.otpType == OtpConstant.OtpType.REGISTER_PHONE_NUMBER) {
            analytics.trackClickBackRegisterPhoneOtp()
        } else if (otpData.otpType == OtpConstant.OtpType.REGISTER_EMAIL) {
            analytics.trackClickBackRegisterEmailOtp()
        }
        return true
    }

    protected fun sendOtp() {
        if (isCountdownFinished()) {
            viewModel.sendOtp(
                    otpType = otpData.otpType.toString(),
                    mode = modeListData.modeText,
                    msisdn = otpData.msisdn,
                    email = otpData.email,
                    otpDigit = modeListData.otpDigit,
                    validateToken = otpData.accessToken,
                    userIdEnc = otpData.userIdEnc
            )
        } else {
            setFooterText()
        }
    }

    protected fun validate(code: String) {
        when (otpData.otpType) {
            OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                analytics.trackClickVerificationRegisterPhoneButton()
            }
            OtpConstant.OtpType.REGISTER_EMAIL -> {
                analytics.trackClickVerificationRegisterEmailButton()
            }
            else -> {
                analytics.trackClickVerificationButton(otpData.otpType)
            }
        }
        viewModel.otpValidate(
                code = code,
                otpType = otpData.otpType.toString(),
                msisdn = otpData.msisdn,
                email = otpData.email,
                mode = modeListData.modeText,
                userId = otpData.userId.toIntOrZero(),
                userIdEnc = otpData.userIdEnc,
                validateToken = otpData.accessToken
        )
    }

    private fun initObserver() {
        viewModel.sendOtpResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessSendOtp(it.data)
                is Fail -> onFailedSendOtp().invoke(it.throwable)
            }
        })
        viewModel.otpValidateResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessOtpValidate(it.data)
                is Fail -> onFailedOtpValidate(it.throwable)
            }
        })
    }

    open fun onSuccessSendOtp(otpRequestData: OtpRequestData) {
        when {
            otpRequestData.success -> {
                if (!isFirstSendOtp) {
                    when (otpData.otpType) {
                        OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                            analytics.trackSuccessClickResendRegisterPhoneOtpButton()
                        }
                        OtpConstant.OtpType.REGISTER_EMAIL -> {
                            analytics.trackSuccessClickResendRegisterEmailOtpButton()
                        }
                    }
                }
                startCountDown()
                viewBound.containerView?.let {
                    Toaster.make(it, otpRequestData.message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
            }
            otpRequestData.errorMessage.isNotEmpty() -> {
                onFailedSendOtp().invoke(MessageErrorException(otpRequestData.errorMessage))
            }
            else -> {
                onFailedSendOtp().invoke(Throwable())
            }
        }

        isFirstSendOtp = false
        showKeyboard()
    }

    private fun onFailedSendOtp(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
            viewBound.containerView?.let {
                val message = ErrorHandler.getErrorMessage(context, throwable)
                Toaster.make(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)

                if (!isFirstSendOtp) {
                    when (otpData.otpType) {
                        OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                            analytics.trackFailedClickResendRegisterPhoneOtpButton(message)
                        }
                        OtpConstant.OtpType.REGISTER_EMAIL -> {
                            analytics.trackFailedClickResendRegisterEmailOtpButton(message)
                        }
                    }
                }
            }

            isFirstSendOtp = false
            showKeyboard()
        }
    }

    private fun onSuccessOtpValidate(otpValidateData: OtpValidateData) {
        when {
            otpValidateData.success -> {
                viewModel.done = true
                trackSuccess()
                resetCountDown()
                val bundle = Bundle().apply {
                    putString(ApplinkConstInternalGlobal.PARAM_UUID, otpValidateData.validateToken)
                    putString(ApplinkConstInternalGlobal.PARAM_TOKEN, otpValidateData.validateToken)
                    putString(ApplinkConstInternalGlobal.PARAM_MSISDN, otpData.msisdn)
                    putString(ApplinkConstInternalGlobal.PARAM_EMAIL, otpData.email)
                    putString(ApplinkConstInternalGlobal.PARAM_SOURCE, otpData.source)
                    putString(ApplinkConstInternalGlobal.PARAM_OTP_CODE, viewBound.pin?.value.toString())
                }
                redirectAfterValidationSuccessful(bundle)
            }
            otpValidateData.errorMessage.isNotEmpty() -> {
                onFailedOtpValidate(MessageErrorException(otpValidateData.errorMessage))
            }
            else -> {
                onFailedOtpValidate(Throwable())
            }
        }
    }

    protected open fun trackSuccess() {
        when (otpData.otpType) {
            OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                analytics.trackSuccessClickVerificationRegisterPhoneButton()
            }
            OtpConstant.OtpType.REGISTER_EMAIL -> {
                analytics.trackSuccessClickVerificationRegisterEmailButton()
            }
        }
    }

    private fun redirectAfterValidationSuccessful(bundle: Bundle) {
        if ((activity as VerificationActivity).isResetPin2FA) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PIN).apply {
                bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true)
                bundle.putString(ApplinkConstInternalGlobal.PARAM_USER_ID, otpData.userId)
                putExtras(bundle)
            }
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            activity?.startActivity(intent)
        } else {
            activity?.setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
        }
        activity?.finish()
    }

    protected open fun onFailedOtpValidate(throwable: Throwable) {
        throwable.printStackTrace()
        viewBound.containerView?.let {
            val message = ErrorHandler.getErrorMessage(context, throwable)
            Toaster.make(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            when (otpData.otpType) {
                OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                    analytics.trackFailedClickVerificationRegisterPhoneButton(message)
                }
                OtpConstant.OtpType.REGISTER_EMAIL -> {
                    analytics.trackFailedClickVerificationRegisterEmailButton(message)
                }
            }
            viewBound.pin?.isError = true
            showKeyboard()
        }
    }

    fun animateText(txt: CharSequence) {
        tempOtp = txt
        indexTempOtp = 0
        viewBound.pin?.value = ""
        handler?.removeCallbacks(characterAdder)
        handler?.postDelayed(characterAdder, delayAnimateText)
    }

    private fun isCountdownFinished(): Boolean {
        return verificationPref.isExpired() || !verificationPref.hasTimer
    }

    private fun startCountDown() {
        if (isCountdownFinished()) {
            verificationPref.hasTimer = true
            verificationPref.setExpire(COUNTDOWN_LENGTH)
        }

        if (!isRunningCountDown) {
            countDownTimer = object : CountDownTimer((verificationPref.getRemainingTime() * INTERVAL).toLong(), INTERVAL.toLong()) {
                override fun onFinish() {
                    isRunningCountDown = false
                    setFooterText()
                }

                override fun onTick(millisUntilFinished: Long) {
                    if (isAdded) {
                        isRunningCountDown = true
                        setRunningCountdownText(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toInt())
                    }
                }

            }.start()
        }
    }

    private fun resetCountDown() {
        verificationPref.hasTimer = false
    }

    open fun initView() {
        if (modeListData.otpListImgUrl.isNotEmpty()) {
            viewBound.methodIcon?.setImageUrl(modeListData.otpListImgUrl)
            viewBound.methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER
        }

        if (modeListData.afterOtpListTextHtml.isNotEmpty()) {
            setActivateTextFull(modeListData.afterOtpListTextHtml)
        }

        viewBound.pin?.pinCount = modeListData.otpDigit

        viewBound.pin?.pinPrimaryActionView?.hide()

        viewBound.pin?.onPinChangedListener = object : PinUnify.OnPinChangedListener {
            override fun onFinish(value: CharSequence?) {
                validate(value.toString())
            }

            override fun onPinChanged(value: CharSequence?) {
                viewBound.pin?.isError = false
            }
        }

        setFooterText()
    }

    private fun showKeyboard() {
        viewBound.pin?.pinTextField?.let { view ->
            view.post {
                if (view.requestFocus()) {
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE)?.let { inputMethodManager ->
                        when (inputMethodManager) {
                            is InputMethodManager -> inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                            else -> {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    private fun setActivateTextFull(text: String) {
        activity?.let {
            if (text.isNotEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    viewBound.pin?.pinDescription = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    viewBound.pin?.pinDescription = Html.fromHtml(text)
                }
            }
        }
    }

    open fun setFooterText(spannable: Spannable? = SpannableString("")) {
        context?.let {
            viewBound.pin?.pinMessageView?.visible()
            viewBound.pin?.pinMessageView?.movementMethod = LinkMovementMethod.getInstance()
            viewBound.pin?.pinMessageView?.setText(spannable, TextView.BufferType.SPANNABLE)
        }
    }

    protected fun setResendOtpFooterSpan(message: String, spannable: Spannable) {

        val otpMsg = getString(R.string.resend_otp)
        val start = message.indexOf(otpMsg)
        val end = start + otpMsg.length

        if (start < 0 || end < 0) {
            return
        }

        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        when (otpData.otpType) {
                            OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                                analytics.trackClickResendRegisterPhoneOtpButton()
                            }
                            OtpConstant.OtpType.REGISTER_EMAIL -> {
                                analytics.trackClickResendRegisterEmailOtpButton()
                            }
                            else -> {
                                analytics.trackClickResendOtpButton(otpData.otpType)
                            }
                        }

                        sendOtp()
                        viewBound.pin?.value = ""
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                start,
                end,
                0
        )
    }

    protected fun setOtherMethodFooterSpan(message: String, spannable: Spannable) {
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        viewModel.done = true
                        analytics.trackClickUseOtherMethod(otpData.otpType)
                        (activity as VerificationActivity).goToVerificationMethodPage()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                message.indexOf(getString(R.string.with_other_method)),
                message.indexOf(getString(R.string.with_other_method)) + getString(R.string.with_other_method).length,
                0
        )
    }

    protected fun setOtherMethodPinFooterSpan(message: String, spannable: Spannable) {
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        viewModel.done = true
                        analytics.trackClickUseOtherMethod(otpData.otpType)
                        (activity as VerificationActivity).goToVerificationMethodPage()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                message.indexOf(getString(R.string.login_with_other_method)),
                message.indexOf(getString(R.string.login_with_other_method)) + getString(R.string.login_with_other_method).length,
                0
        )
    }

    private fun setRunningCountdownText(countdown: Int) {
        val text = String.format(
                getString(R.string.verification_coundown_text),
                countdown
        )
        viewBound.pin?.pinMessage = MethodChecker.fromHtml(text)
    }

    companion object {
        private const val INTERVAL = 1000
        private const val COUNTDOWN_LENGTH = 30

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = VerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}