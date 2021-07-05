package com.tokopedia.otp.verification.view.fragment

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.otp.R
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.common.util.PhoneCallBroadcastReceiver
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.view.activity.VerificationActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class MisscallVerificationFragment : VerificationFragment(), PhoneCallBroadcastReceiver.OnCallStateChange {

    @Inject
    lateinit var phoneCallBroadcastReceiver: PhoneCallBroadcastReceiver

    private val permissionCheckerHelper = PermissionCheckerHelper()
    private var crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    private var remoteConfigInstance: RemoteConfigInstance? = null
    private var rollanceType = ""

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerIncomingPhoneCall(it)
            }
        }
        sendOtp()
    }

    override fun initView() {
        rollanceType = getAbTestPlatform()?.getString(ROLLANCE_KEY_MISCALL_OTP).toString()
        super.initView()

        setPrefixMiscall()
        setIcon()
        setNewTextTitleAndDescription()

        viewBound.prefixTextMethodIcon?.hide()
    }

    private fun setIcon() {
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270f, resources.displayMetrics)
        viewBound.methodIcon?.layoutParams.apply {
            this?.height = height.toInt()
            this?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            viewBound.methodIcon?.layoutParams = this
        }
        viewBound.methodIcon?.setMargin(0, 0, 0, 0)
        viewBound.methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER

        if (isOtpMiscallNew()) {
            context?.let {
                viewBound.methodIcon?.setImageUrl(URL_IMG_VERIFICATION_MISCALL_NEW)
            }
        } else {
            viewBound.methodIcon?.setImageUrl(MISSCALL_IMAGE_URL)
        }
    }

    private fun setNewTextTitleAndDescription() {
        if (isOtpMiscallNew()) {
            viewBound.pin?.pinTitle = getString(R.string.cotp_miscall_verification_title)
            viewBound.pin?.pinDescription = getString(R.string.cotp_miscall_verification_desc)
        }
    }

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            val spannableChild: Spannable
            if (otpData.canUseOtherMethod && isMoreThanOneMethod) {
                val message = getFooterTextResendWithOtherMethod()
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
                setOtherMethodFooterSpan(message, spannableChild)
            } else {
                val message = getFooterTextResend()
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
            }
            super.setFooterText(spannableChild)
        }
    }

    override fun setResendOtpFooterSpan(message: String, spannable: Spannable) {
        if (isOtpMiscallNew()) {
            val otpMsg = getString(R.string.cotp_miscall_resend)
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
        } else {
            super.setResendOtpFooterSpan(message, spannable)
        }
    }

    override fun setOtherMethodFooterSpan(message: String, spannable: Spannable) {
        if (isOtpMiscallNew()) {
            spannable.setSpan(
                    object : ClickableSpan() {
                        override fun onClick(view: View) {
                            viewModel.done = true
                            analytics.trackClickUseOtherMethod(otpData, modeListData)
                            (activity as VerificationActivity).goToVerificationMethodPage()
                        }

                        override fun updateDrawState(ds: TextPaint) {
                            ds.color = MethodChecker.getColor(context, R.color.Unify_G500)
                            ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        }
                    },
                    message.indexOf(getString(R.string.cotp_miscall_use_other_method)),
                    message.indexOf(getString(R.string.cotp_miscall_use_other_method)) + getString(R.string.cotp_miscall_use_other_method).length,
                    0
            )
        } else {
            super.setOtherMethodFooterSpan(message, spannable)
        }
    }

    override fun setRunningCountdownText(countdown: Int) {
        val text = String.format(getString(R.string.cotp_miscall_coundown_text), countdown)
        viewBound.pin?.pinMessage = MethodChecker.fromHtml(text)
    }

    private fun getFooterTextResendWithOtherMethod(): String {
        var footerText = ""
        context?.let {
            footerText = if (isOtpMiscallNew()) {
                it.getString(R.string.cotp_miscall_resend_with_other_method)
            } else {
                it.getString(R.string.validation_resend_or_with_other_method)
            }
        }
        return footerText
    }

    private fun getFooterTextResend(): String {
        var footerText = ""
        context?.let {
            footerText = if (isOtpMiscallNew()) {
                it.getString(R.string.cotp_miscall_resend)
            } else {
                it.getString(R.string.validation_resend)
            }
        }
        return footerText
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerIncomingPhoneCall(it)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::phoneCallBroadcastReceiver.isInitialized) {
            activity?.let { phoneCallBroadcastReceiver.unregisterReceiver(it) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context?.let {
                permissionCheckerHelper.onRequestPermissionsResult(it, requestCode, permissions, grantResults)
            }
        }
    }

    override fun onSuccessSendOtp(otpRequestData: OtpRequestData) {
        super.onSuccessSendOtp(otpRequestData)
        if (otpRequestData.success) {
            setPrefixMiscall(otpRequestData.prefixMisscall)
        }
    }

    override fun onIncomingCallStart(phoneNumber: String) {
        autoFillPhoneNumber(phoneNumber)
    }

    override fun onMissedCall(phoneNumber: String) {
        autoFillPhoneNumber(phoneNumber)
    }

    override fun onIncomingCallEnded(phoneNumber: String) {
        autoFillPhoneNumber(phoneNumber)
    }

    private fun registerIncomingPhoneCall(context: Context) {
        if (phoneCallBroadcastReceiver.isRegistered) {
            return
        }

        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        val disableAutoReadMissCall = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_DISABLE_AUTOREAD_MISSCALL, false)
        if (disableAutoReadMissCall) {
            return
        }

        if (permissionCheckerHelper.hasPermission(context, getPermissions())) {
            phoneCallBroadcastReceiver.registerReceiver(context, this)
        } else {
            sendLogTracker("PhoneCallBroadcastReceiver not registered; permission=${permissionCheckerHelper.hasPermission(context, getPermissions())}")
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_READ_CALL_LOG,
                PermissionCheckerHelper.Companion.PERMISSION_CALL_PHONE,
                PermissionCheckerHelper.Companion.PERMISSION_READ_PHONE_STATE
        )
    }

    private fun sendLogTracker(message: String) {
        try {
            crashlytics.recordException(Throwable(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setPrefixMiscall(prefix: String = DEFAULT_PREFIX_MISCALL) {
        viewBound.prefixTextMethodIcon?.text = prefix
        viewBound.pin?.pinPrefixText = prefix
    }

    private fun autoFillPhoneNumber(number: String) {
        val phoneHint = replaceRegionPhoneCode(viewBound.pin?.pinPrefixText.toString())
        var phoneNumber = replaceRegionPhoneCode(number)

        if (phoneNumber.contains(phoneHint)) {
            phoneNumber = phoneNumber.substring(phoneNumber.length - modeListData.otpDigit, phoneNumber.length)
            viewBound.pin?.value = phoneNumber
            validate(phoneNumber)
        }
    }

    private fun replaceRegionPhoneCode(phoneNumber: String): String {
        val regionRegex = Regex(REGEX_PHONE_NUMBER_REGION)
        val symbolRegex = Regex(REGEX_PHONE_NUMBER)
        var result = phoneNumber

        if (phoneNumber.contains(regionRegex)) {
            result = phoneNumber.replace(regionRegex, "0")
        }

        return result.replace(symbolRegex, "")
    }

    private fun isOtpMiscallNew(): Boolean {
        return rollanceType.contains(ROLLANCE_KEY_MISCALL_OTP)
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        if (remoteConfigInstance == null) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance?.abTestPlatform
    }

    companion object {
        private const val REMOTE_CONFIG_KEY_DISABLE_AUTOREAD_MISSCALL = "android_disable_autoread_misscall"
        private const val DEFAULT_PREFIX_MISCALL = "000-00"
        private const val MISSCALL_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/otp_miscall_img.png"
        private const val REGEX_PHONE_NUMBER = """[+()\-\s]"""
        private const val REGEX_PHONE_NUMBER_REGION = "^(\\+\\d{1,2})"

        private const val URL_IMG_VERIFICATION_MISCALL_NEW = "https://images.tokopedia.net/img/android/user/miscall/ic_miscall_verification.png"

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = MisscallVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}