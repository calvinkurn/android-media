package com.tokopedia.otp.cotp.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Parcelable
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ScrollView
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.OTPAnalytics
import com.tokopedia.otp.common.di.DaggerOtpComponent
import com.tokopedia.otp.cotp.di.DaggerCotpComponent
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER
import com.tokopedia.otp.cotp.view.activity.VerificationActivity
import com.tokopedia.otp.cotp.view.presenter.VerificationPresenter
import com.tokopedia.otp.cotp.view.viewlistener.VerificationOtpMiscall
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_cotp_miscall_verification.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VerificationOtpMiscallFragment : BaseDaggerFragment(), VerificationOtpMiscall.View {

    private var countDownTimer: CountDownTimer? = null
    private var isRunningTimer = false
    private lateinit var cacheHandler: LocalCacheHandler
    private lateinit var viewModel: VerificationViewModel

    @Inject
    lateinit var presenter: VerificationPresenter

    @Inject
    lateinit var analytics: OTPAnalytics

    override fun initInjector() {
        val otpComponent = DaggerOtpComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication)
                        .baseAppComponent).build()

        DaggerCotpComponent.builder()
                .otpComponent(otpComponent)
                .build().inject(this)

        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null && arguments?.getParcelable<Parcelable>(ARGS_PASS_DATA) != null) {
            viewModel = parseViewModel(arguments as Bundle)
        } else {
            activity?.finish()
        }

        cacheHandler = LocalCacheHandler(activity, CACHE_OTP)
    }



    private fun parseViewModel(bundle: Bundle): VerificationViewModel {
        viewModel = bundle.getParcelable(ARGS_PASS_DATA) as VerificationViewModel
        return viewModel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(ARGS_DATA, viewModel)
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(activity, screenName)
    }

    override fun getScreenName(): String {
        return if (!TextUtils.isEmpty(viewModel.appScreen)) {
            viewModel.appScreen
        } else {
            OTPAnalytics.Screen.SCREEN_COTP_DEFAULT
        }
    }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cotp_miscall_verification, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupGeneralView()
        updateViewFromServer()
        requestOtp()
        showKeyboard(false)
    }

    private fun updateViewFromServer() {
        presenter.updateViewFromServer(viewModel)
    }

    private fun requestOtp() {
        presenter.requestOTP(viewModel)
    }

    private fun setupGeneralView() {

        ImageHandler.loadImageAndCache(imgVerify, IMAGE_URL)

        textInputOtp?.setOnClickListener {
            showKeyboard(true)
        }

        textInputOtp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (textInputOtp?.text?.length == viewModel.numberOtpDigit) {
                    enableVerifyButton()
                    verifyOtp()
                } else {
                    disableVerifyButton()
                }
            }
        })

        textInputOtp?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && textInputOtp?.length() == MAX_OTP_LENGTH) {
                verifyOtp()
                true
            }
            false
        }

        buttonVerify?.setOnClickListener {
            analytics.eventClickVerifyButton(viewModel.otpType)
            if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
                analytics.eventClickVerificationButton()
            }
            verifyOtp()
        }
    }

    private fun verifyOtp() {
        presenter.verifyOtp(
                viewModel.otpType,
                viewModel.phoneNumber,
                viewModel.email,
                textInputOtp?.text.toString(),
                viewModel.mode
        )
    }

    private fun disableVerifyButton() {
        buttonVerify?.isEnabled = false
        buttonVerify?.setTextColor(MethodChecker.getColor(activity, R.color.grey_500))
        MethodChecker.setBackground(buttonVerify,
                MethodChecker.getDrawable(activity, R.drawable.grey_button_otp))
    }

    private fun enableVerifyButton() {
        buttonVerify?.isEnabled = true
        buttonVerify?.setTextColor(MethodChecker.getColor(activity, R.color.white))
        MethodChecker.setBackground(buttonVerify,
                MethodChecker.getDrawable(activity, R.drawable.green_button_otp))
        removeErrorOtp()

    }

    override fun onSuccessGetOTP(textMessageVerify: String) {
        if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
            analytics.trackSuccessClickResendPhoneOtpButton()
        }
        NetworkErrorHelper.showSnackbar(activity, textMessageVerify)

        buttonVerify?.text = getString(R.string.button_verify)
        startTimer()
    }

    override fun updatePhoneHint(phoneHint: String) {
        textPhoneHint?.text = phoneHint
    }

    override fun onSuccessVerifyOTP(uuid: String, msisdn: String) {
        removeErrorOtp()
        resetCountDown()

        if (activity != null) {

            if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
                analytics.eventSuccessClickVerificationButton()
            }

            val phoneNumber: String = if (msisdn.isNotEmpty()) {
                msisdn
            } else {
                viewModel.phoneNumber
            }

            val intent = Intent()
            val bundle = Bundle()
            bundle.putString(ApplinkConstInternalGlobal.PARAM_UUID, uuid)
            bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
            val otpCode = textInputOtp?.text.toString().trim { it <= ' ' }
            bundle.putString(ApplinkConstInternalGlobal.PARAM_OTP_CODE, otpCode)
            intent.putExtras(bundle)
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }

    }

    override fun onGoToPhoneVerification() {
        if (activity != null) {
            activity?.setResult(Activity.RESULT_OK)
            val intent = RouteManager.getIntent(activity, ApplinkConst.PHONE_VERIFICATION)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun resetCountDown() {
        cacheHandler.putBoolean(HAS_TIMER, false)
        cacheHandler.applyEditor()
    }

    override fun onErrorGetOTP(errorMessage: String) {
        if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
            analytics.trackFailedClickResendPhoneOtpButton(errorMessage)
        }
        view?.let { Toaster.showError(it, errorMessage, Snackbar.LENGTH_LONG) }
        setFinishedCountdownText()
    }

    override fun onLimitOTPReached(errorMessage: String) {
        textMessageVerify?.visibility = View.VISIBLE
        textMessageVerify?.text = errorMessage
        textMessageVerify?.setTextColor(MethodChecker.getColor(activity, R.color.red_500))
        setLimitReachedCountdownText()
    }

    override fun logUnknownError(throwable: Throwable) {
        try {
            Crashlytics.logException(throwable)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun setErrorView(errorMessage: String) {
        if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
            analytics.eventFailedClickVerificationButton(errorMessage)
        }

        textInputOtp?.text?.clear()
        textInputOtp?.isError = true
        textErrorVerify?.visibility = View.VISIBLE
        textErrorVerify?.text = errorMessage.substring(0, errorMessage.indexOf("("))

        if (errorMessage.contains(LIMIT_ERR_MSG)) {
            buttonVerify?.visibility = View.VISIBLE
            buttonVerify?.setText(R.string.other_method)
            buttonVerify?.setOnClickListener { onOtherMethodClick() }
        }
    }

    override fun onErrorVerifyOtpCode(errorMessage: String) {
        setErrorView(errorMessage)
    }

    override fun onErrorVerifyLogin(errorMessage: String) {
        setErrorView(errorMessage)
    }

    override fun onErrorVerifyOtpCode(resId: Int) {
        onErrorVerifyOtpCode(getString(resId))
    }

    override fun trackOnBackPressed() {
        analytics.eventClickBackOTPPage(viewModel.otpType)
        if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
            analytics.eventClickBackRegisterOTPPage()
        }
    }

    override fun showLoadingProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun dismissLoadingProgress() {
        progressBar.visibility = View.GONE
    }

    override fun isCountdownFinished(): Boolean {
        return cacheHandler.isExpired || !cacheHandler.getBoolean(HAS_TIMER, false)
    }

    override fun dropKeyboard() {
        KeyboardHandler.DropKeyboard(activity, view)
    }

    private fun startTimer() {
        if (isCountdownFinished) {
            cacheHandler.putBoolean(HAS_TIMER, true)
            cacheHandler.setExpire(COUNTDOWN_LENGTH)
            cacheHandler.applyEditor()
        }

        textResend?.visibility = View.GONE
        if (!isRunningTimer) {
            countDownTimer = object : CountDownTimer((cacheHandler.remainingTime * INTERVAL).toLong(), INTERVAL.toLong()) {
                override fun onTick(millisUntilFinished: Long) {
                    if (isAdded) {
                        isRunningTimer = true
                        setRunningCountdownText(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString())
                    }
                }

                override fun onFinish() {
                    isRunningTimer = false
                    setFinishedCountdownText()
                }

            }.start()
        }
        textInputOtp?.requestFocus()
    }

    private fun setFinishedCountdownText() {
        textMessageVerify?.visibility = View.VISIBLE
        textMessageVerify?.text = MethodChecker.fromHtml(getString(R.string.not_received_code))

        textOr?.visibility = View.VISIBLE
        textResend?.visibility = View.VISIBLE
        textResend?.text = MethodChecker.fromHtml(getString(R.string.cotp_miscall_verification_resend))
        textResend?.setOnClickListener {
            analytics.eventClickResendOtp(viewModel.otpType)
            if (viewModel.otpType == OTP_TYPE_REGISTER_PHONE_NUMBER) {
                analytics.eventClickResendPhoneOtpButton()
            }
            textInputOtp?.text?.clear()
            textInputOtp?.isError = false
            removeErrorOtp()
            requestOtp()
        }

        if (viewModel.canUseOtherMethod()) {
            textUseOtherMethod?.visibility = View.VISIBLE
            textUseOtherMethod?.setOnClickListener { onOtherMethodClick() }
        } else {
            textUseOtherMethod?.visibility = View.GONE
        }
    }

    private fun onOtherMethodClick() {
        analytics.eventClickUseOtherMethod(viewModel.otpType)
        dropKeyboard()
        goToOtherVerificationMethod()
    }

    private fun removeErrorOtp() {
        textInputOtp?.isError = false
        textErrorVerify?.visibility = View.INVISIBLE
    }

    private fun setLimitReachedCountdownText() {
        textInputOtp?.text?.clear()
        textInputOtp?.isEnabled = false

        if (viewModel.canUseOtherMethod()) {
            textResend?.visibility = View.GONE
            textOr?.visibility = View.GONE
            textUseOtherMethod?.visibility = View.GONE

            buttonVerify?.text = getString(R.string.cotp_miscall_verification_with_other_method)
            buttonVerify?.isEnabled = true
            buttonVerify?.setOnClickListener { goToOtherVerificationMethod() }
        } else {
            textMessageVerify?.text = MethodChecker.fromHtml(getString(R.string.cotp_miscall_verification_not_received_code))
        }
    }

    private fun setRunningCountdownText(countdown: String) {
        textMessageVerify?.visibility = View.VISIBLE
        textMessageVerify?.setOnClickListener(null)

        val text = String.format("%s <b> %s %s</b> %s",
                getString(R.string.please_wait_in),
                countdown,
                getString(R.string.second),
                getString(R.string.to_resend_otp))

        textMessageVerify?.text = MethodChecker.fromHtml(text)
    }

    private fun goToOtherVerificationMethod() {
        if (activity is VerificationActivity) {
            (activity as VerificationActivity).goToSelectVerificationMethod()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        presenter.detachView()
    }

    override fun onSuccessGetModelFromServer(methodItem: MethodItem) {
        this.viewModel.imageUrl = methodItem.imageUrl
        this.viewModel.message = methodItem.verificationText
    }

    private fun showKeyboard(isClicked: Boolean) {
        if (!isClicked) {
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(textInputOtp.windowToken, InputMethodManager.SHOW_FORCED, 0)
        }

        scrollView.postDelayed({
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }, 500)
    }
  
    companion object {
        private const val ARGS_DATA = "ARGS_DATA"
        private const val ARGS_PASS_DATA = "pass_data"

        private const val COUNTDOWN_LENGTH = 30
        private const val INTERVAL = 1000
        private const val MAX_OTP_LENGTH = 6

        private const val CACHE_OTP = "CACHE_OTP"
        private const val HAS_TIMER = "has_timer"

        private const val LIMIT_ERR_MSG = "3"

        private const val VERIFICATION_CODE = "Kode verifikasi"
        private const val PIN_ERR_MSG = "PIN"

        private const val IMAGE_URL = "https://ecs7.tokopedia.net/android/others/otp_miscall_verification.png"

        fun createInstance(passModel: VerificationViewModel): Fragment {
            val fragment = VerificationOtpMiscallFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARGS_PASS_DATA, passModel)
            fragment.arguments = bundle
            return fragment
        }
    }
}