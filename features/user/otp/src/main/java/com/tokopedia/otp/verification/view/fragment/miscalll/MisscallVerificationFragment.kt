package com.tokopedia.otp.verification.view.fragment.miscalll

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.otp.R
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.verification.common.util.PhoneCallBroadcastReceiver
import com.tokopedia.otp.verification.domain.data.OtpRequestData
import com.tokopedia.otp.verification.view.fragment.VerificationFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

open class MisscallVerificationFragment : VerificationFragment(), PhoneCallBroadcastReceiver.OnCallStateChange {

    @Inject
    lateinit var phoneCallBroadcastReceiver: PhoneCallBroadcastReceiver

    private val permissionCheckerHelper = PermissionCheckerHelper()

    private var remoteConfigInstance: RemoteConfigInstance? = null

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remoteConfigInstance = RemoteConfigInstance.getInstance()

        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                registerIncomingPhoneCall(it)
            }
        }
        sendOtp()
    }

    override fun initView() {
        modeListData.otpListImgUrl = ""
        super.initView()

        setNewTextTitleAndDescription()
        setIcon()
    }

    private fun setIcon() {
        val height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            FLOAT_270,
            context?.resources?.displayMetrics
        )
        viewBound.methodIcon?.apply {
            setMargin(0, 0, 0, 0)
            scaleType = ImageView.ScaleType.FIT_CENTER
            layoutParams.apply {
                this?.height = height.toInt()
                this?.width = ViewGroup.LayoutParams.WRAP_CONTENT
                viewBound.methodIcon?.layoutParams = this
            }
            setImageUrl(URL_IMG_VERIFICATION_MISCALL_NEW)
        }
    }

    private fun setNewTextTitleAndDescription() {
        viewBound.pin?.pinTitle = getString(R.string.cotp_miscall_verification_title)
        viewBound.pin?.pinDescription = getString(R.string.cotp_miscall_verification_desc)
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

    override fun setRunningCountdownText(countdown: Int) {
        val text = String.format(getString(R.string.cotp_miscall_coundown_text), countdown)
        viewBound.pin?.pinMessage = MethodChecker.fromHtml(text)
    }

    private fun getFooterTextResendWithOtherMethod(): String {
        return context?.getString(R.string.validation_resend_or_with_other_method).orEmpty()
    }

    private fun getFooterTextResend(): String {
        return context?.getString(R.string.validation_resend).orEmpty()
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
            activity?.let {
                phoneCallBroadcastReceiver.unregisterReceiver(it)
            }
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
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_READ_CALL_LOG,
                PermissionCheckerHelper.Companion.PERMISSION_CALL_PHONE,
                PermissionCheckerHelper.Companion.PERMISSION_READ_PHONE_STATE
        )
    }

    private fun setPrefixMiscall(prefix: String = DEFAULT_PREFIX_MISCALL) {
        viewBound.pin?.pinPrefixText = prefix
    }

    private fun autoFillPhoneNumber(number: String) {
        if (isOnValidation) return

        val phoneHint = replaceRegionPhoneCode(viewBound.pin?.pinPrefixText.toString())
        var phoneNumber = replaceRegionPhoneCode(number)

        if (phoneNumber.contains(phoneHint)) {
            phoneNumber = phoneNumber.substring(phoneNumber.length - modeListData.otpDigit, phoneNumber.length)
            viewBound.pin?.value = phoneNumber
            isOnValidation = true
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
        private const val FLOAT_270 = 270f

        private const val REMOTE_CONFIG_KEY_DISABLE_AUTOREAD_MISSCALL = "android_disable_autoread_misscall"
        private const val DEFAULT_PREFIX_MISCALL = "000-00"
        private const val REGEX_PHONE_NUMBER = """[+()\-\s]"""
        private const val REGEX_PHONE_NUMBER_REGION = "^(\\+\\d{1,2})"

        private const val URL_IMG_VERIFICATION_MISCALL_NEW = TokopediaImageUrl.URL_IMG_VERIFICATION_MISCALL_NEW

        fun createInstance(bundle: Bundle?): VerificationFragment {
            val fragment = MisscallVerificationFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }
}
