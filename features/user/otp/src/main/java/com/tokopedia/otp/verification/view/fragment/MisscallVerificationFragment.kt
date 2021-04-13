package com.tokopedia.otp.verification.view.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.otp.R
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.common.util.PhoneCallBroadcastReceiver
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class MisscallVerificationFragment : VerificationFragment(), PhoneCallBroadcastReceiver.OnCallStateChange {

    @Inject
    lateinit var phoneCallBroadcastReceiver: PhoneCallBroadcastReceiver

    private val permissionCheckerHelper = PermissionCheckerHelper()
    private var crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendOtp()
    }

    override fun initView() {
        super.initView()
        setPrefixMiscall()
        viewBound.prefixTextMethodIcon?.visible()
        val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270f, resources.displayMetrics)
        viewBound.methodIcon?.layoutParams.apply {
            this?.height = height.toInt()
            this?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            viewBound.methodIcon?.layoutParams = this
        }
        viewBound.methodIcon?.setMargin(0, 0, 0, 0)
        viewBound.methodIcon?.setImageUrl(MISSCALL_IMAGE_URL)
        viewBound.methodIcon?.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    override fun setFooterText(spannable: Spannable?) {
        context?.let {
            val spannableChild: Spannable
            if (otpData.canUseOtherMethod && isMoreThanOneMethod) {
                val message = it.getString(R.string.validation_resend_or_with_other_method)
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
                setOtherMethodFooterSpan(message, spannableChild)
            } else {
                val message = it.getString(R.string.validation_resend)
                spannableChild = SpannableString(message)
                setResendOtpFooterSpan(message, spannableChild)
            }
            super.setFooterText(spannableChild)
        }
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

    private fun registerIncomingPhoneCall(it: Context) {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
        val disableAutoReadMissCall = firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_KEY_DISABLE_AUTOREAD_MISSCALL, false)
        if (disableAutoReadMissCall) {
            return
        }
        if (permissionCheckerHelper.hasPermission(it, getPermissions())) {
            phoneCallBroadcastReceiver.registerReceiver(it, this)
        } else {
            sendLogTracker("PhoneCallBroadcastReceiver not registered; permission=${permissionCheckerHelper.hasPermission(it, getPermissions())}")
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
            phoneNumber = phoneNumber.substring(phoneNumber.length - 4, phoneNumber.length)
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

    companion object {
        private const val REMOTE_CONFIG_KEY_DISABLE_AUTOREAD_MISSCALL = "android_disable_autoread_misscall"
        private const val DEFAULT_PREFIX_MISCALL = "000-00"
        private const val MISSCALL_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/otp_miscall_img.png"
        private const val REGEX_PHONE_NUMBER = """[+()\-\s]"""
        private const val REGEX_PHONE_NUMBER_REGION = "^(\\+\\d{1,2})"

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = MisscallVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}