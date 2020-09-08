package com.tokopedia.otp.verification.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant
import com.tokopedia.otp.common.analytics.TrackingValidatorConstant.Screen.SCREEN_ACCOUNT_ACTIVATION
import com.tokopedia.otp.common.analytics.TrackingValidatorUtil
import com.tokopedia.otp.common.util.SmsBroadcastReceiver
import com.tokopedia.otp.common.util.SmsBroadcastReceiver.ReceiveSMSListener
import com.tokopedia.otp.verification.common.IOnBackPressed
import com.tokopedia.otp.verification.common.VerificationPref
import com.tokopedia.otp.verification.common.di.VerificationComponent
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.domain.data.OtpValidateData
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

class VerificationFragment : BaseVerificationFragment(), IOnBackPressed {

    @Inject
    lateinit var analytics: TrackingValidatorUtil
    @Inject
    lateinit var verificationPref: VerificationPref
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var smsBroadcastReceiver: SmsBroadcastReceiver
    @Inject
    lateinit var smsRetrieverClient: SmsRetrieverClient

    private lateinit var otpData: OtpData
    private lateinit var modeListData: ModeListData
    private lateinit var countDownTimer: CountDownTimer

    private var isRunningCountDown = false
    private var isFirstSendOtp = true

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VerificationViewModel::class.java)
    }

    override val viewBound = VerificationViewBinding()

    override fun getScreenName() = when (otpData.otpType) {
        OtpConstant.OtpType.REGISTER_EMAIL -> SCREEN_ACCOUNT_ACTIVATION
        else -> TrackingValidatorConstant.Screen.SCREEN_COTP + modeListData.modeText
    }

    override fun initInjector() = getComponent(VerificationComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
        modeListData = arguments?.getParcelable(OtpConstant.OTP_MODE_EXTRA) ?: ModeListData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        if (!(modeListData.modeText == OtpConstant.OtpMode.PIN ||
                modeListData.modeText == OtpConstant.OtpMode.GOOGLE_AUTH)) {
            smsRetrieverClient.startSmsRetriever()
            sendOtp()
        }
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            smsBroadcastReceiver.register(it, getOtpReceiverListener())
        }
        showKeyboard()
    }

    override fun onPause() {
        super.onPause()
        if(::smsBroadcastReceiver.isInitialized) activity?.unregisterReceiver(smsBroadcastReceiver)
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
        }
        return true
    }

    private fun sendOtp() {
        if (isCountdownFinished()) {
            viewModel.sendOtp(
                    otpType = otpData.otpType.toString(),
                    mode = modeListData.modeText,
                    msisdn = otpData.msisdn,
                    email = otpData.email,
                    otpDigit = modeListData.otpDigit
            )
        } else {
            setFooterText()
        }
    }

    private fun validate(code: String) {
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

        showLoading()
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
                is Success -> onSuccessSendOtp().invoke(it.data)
                is Fail -> onFailedSendOtp().invoke(it.throwable)
            }
        })
        viewModel.otpValidateResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessOtpValidate().invoke(it.data)
                is Fail -> onFailedOtpValidate().invoke(it.throwable)
            }
        })
    }

    private fun onSuccessSendOtp(): (OtpRequestData) -> Unit {
        return { otpRequestData ->
            if (otpRequestData.success) {
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
                hideLoading()
                setPrefixMiscall(otpRequestData.prefixMisscall)
                startCountDown()
                viewBound.containerView?.let {
                    Toaster.make(it, otpRequestData.message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL)
                }
            } else if (otpRequestData.errorMessage.isNotEmpty()) {
                onFailedSendOtp().invoke(MessageErrorException(otpRequestData.errorMessage))
            } else {
                onFailedSendOtp().invoke(Throwable())
            }

            isFirstSendOtp = false
            showKeyboard()
        }
    }

    private fun onFailedSendOtp(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
            hideLoading()
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

    private fun onSuccessOtpValidate(): (OtpValidateData) -> Unit {
        return { otpValidateData ->
            when {
                otpValidateData.success -> {
                    when (otpData.otpType) {
                        OtpConstant.OtpType.REGISTER_PHONE_NUMBER -> {
                            analytics.trackSuccessClickVerificationRegisterPhoneButton()
                        }
                        OtpConstant.OtpType.REGISTER_EMAIL -> {
                            analytics.trackSuccessClickVerificationRegisterEmailButton()
                        }
                    }
                    hideLoading()
                    resetCountDown()

                    activity?.let { activity ->
                        val bundle = Bundle().apply {
                            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, otpValidateData.validateToken)
                            putString(ApplinkConstInternalGlobal.PARAM_MSISDN, otpData.msisdn)
                            putString(ApplinkConstInternalGlobal.PARAM_EMAIL, otpData.email)
                            putString(ApplinkConstInternalGlobal.PARAM_SOURCE, otpData.source)
                            putString(ApplinkConstInternalGlobal.PARAM_OTP_CODE, viewBound.pin?.value.toString())
                        }
                        if((activity as VerificationActivity).isResetPin2FA){
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.CHANGE_PIN).apply {
                                bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_2FA, true)
                                bundle.putString(ApplinkConstInternalGlobal.PARAM_UUID, otpData.userId)
                                putExtras(bundle)
                            }
                            activity.startActivity(intent)
                        }else {
                            bundle.putString(ApplinkConstInternalGlobal.PARAM_UUID, otpValidateData.validateToken)
                            activity.setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
                        }
                        activity.finish()
                    }
                }
                otpValidateData.errorMessage.isNotEmpty() -> {
                    onFailedOtpValidate().invoke(MessageErrorException(otpValidateData.errorMessage))
                }
                else -> {
                    onFailedOtpValidate().invoke(Throwable())
                }
            }
        }
    }

    private fun onFailedOtpValidate(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
            hideLoading()
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
            }
            viewBound.pin?.isError = true
            showKeyboard()
        }
    }

    private fun getOtpReceiverListener(): ReceiveSMSListener {
        return object : ReceiveSMSListener {
            override fun onReceiveOTP(otpCode: String) {
                viewBound.pin?.value = otpCode
                validate(otpCode)
            }
        }
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

    private fun initView() {

        setPrefixMiscall()

        if (modeListData.modeText == OtpConstant.OtpMode.MISCALL) {
            viewBound.prefixTextMethodIcon?.visible()
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270f, resources.displayMetrics)
            viewBound.methodIcon?.layoutParams.apply {
                this?.height = height.toInt()
                this?.width = WRAP_CONTENT
                viewBound.methodIcon?.layoutParams = this
            }
            viewBound.methodIcon?.setMargin(0, 0, 0, 0)
            viewBound.methodIcon?.setImageUrl(MISSCALL_IMAGE_URL)
            viewBound.methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER
        } else {
            if (modeListData.otpListImgUrl.isNotEmpty()) {
                viewBound.methodIcon?.setImageUrl(modeListData.otpListImgUrl)
                viewBound.methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        if (modeListData.afterOtpListTextHtml.isNotEmpty()) {
            setActivateTextFull(modeListData.afterOtpListTextHtml)
        }

        viewBound.pin?.pinCount = modeListData.otpDigit

        if (modeListData.modeText == OtpConstant.OtpMode.PIN) {
            viewBound.pin?.type = PinUnify.TYPE_HIDDEN
        }

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
                if(view.requestFocus()) {
                    val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
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

    private fun setFooterText() {
        val spannable: Spannable
        if(otpData.otpType == 148){
            val message = getString(R.string.forgot_pin)
            spannable = SpannableString(message)
            setForgotPinFooterSpan(message, spannable)
        }
        else if (modeListData.modeText == OtpConstant.OtpMode.PIN ||
                modeListData.modeText == OtpConstant.OtpMode.GOOGLE_AUTH) {
            val message = getString(R.string.login_with_other_method)
            spannable = SpannableString(message)
            setOtherMethodPinFooterSpan(message, spannable)
        } else if (otpData.canUseOtherMethod) {
            val message = getString(R.string.validation_resend_email_or_with_other_method)
            spannable = SpannableString(message)
            setResendOtpFooterSpan(message, spannable)
            setOtherMethodFooterSpan(message, spannable)
        } else {
            val message = getString(R.string.validation_resend_email)
            spannable = SpannableString(message)
            setResendOtpFooterSpan(message, spannable)
        }
        viewBound.pin?.pinMessageView?.visible()
        viewBound.pin?.pinMessageView?.movementMethod = LinkMovementMethod.getInstance()
        viewBound.pin?.pinMessageView?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun setResendOtpFooterSpan(message: String, spannable: Spannable) {

        val otpMsg = getString(R.string.resend_otp)
        val start = message.indexOf(otpMsg)
        val end = start + otpMsg.length

        if(start < 0 || end < 0) {
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
                        ds.color = MethodChecker.getColor(context, R.color.Green_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                start,
                end,
                0
        )
    }

    private fun setForgotPinFooterSpan(message: String, spannable: Spannable) {
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        val data = otpData
                        data.otpType = 149
                        data.otpMode = ""
//                        data.msisdn =
                        (activity as VerificationActivity).goToMethodPageResetPin(data)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Green_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                message.indexOf(getString(R.string.forgot_pin)),
                message.indexOf(getString(R.string.forgot_pin)) + getString(R.string.forgot_pin).length,
                0
        )
    }

    private fun setOtherMethodFooterSpan(message: String, spannable: Spannable) {
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        analytics.trackClickUseOtherMethod(otpData.otpType)
                        (activity as VerificationActivity).goToVerificationMethodPage()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Green_G500)
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                },
                message.indexOf(getString(R.string.with_other_method)),
                message.indexOf(getString(R.string.with_other_method)) + getString(R.string.with_other_method).length,
                0
        )
    }

    private fun setOtherMethodPinFooterSpan(message: String, spannable: Spannable) {
        spannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        analytics.trackClickUseOtherMethod(otpData.otpType)
                        (activity as VerificationActivity).goToVerificationMethodPage()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, R.color.Green_G500)
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

    private fun setPrefixMiscall(prefix: String = DEFAULT_PREFIX_MISCALL) {
        if (modeListData.modeText == OtpConstant.OtpMode.MISCALL) {
            viewBound.prefixTextMethodIcon?.text = prefix
            viewBound.pin?.pinPrefixText = prefix
        }
    }

    private fun showLoading() {
        viewBound.loader?.show()
        viewBound.containerView?.hide()
    }

    private fun hideLoading() {
        viewBound.loader?.hide()
        viewBound.containerView?.show()
    }

    companion object {

        private const val DEFAULT_PREFIX_MISCALL = "000-00"
        private const val MISSCALL_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/otp_miscall_img.png"

        private const val INTERVAL = 1000
        private const val COUNTDOWN_LENGTH = 30

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = VerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}