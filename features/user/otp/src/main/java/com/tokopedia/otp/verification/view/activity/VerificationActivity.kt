package com.tokopedia.otp.verification.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.otp.R
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpActivity
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.fragment.*
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 *
 * For navigate: use [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.COTP]
 * please pass :
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_MSISDN]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_OTP_TYPE]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_SOURCE]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD]
 */
open class VerificationActivity : BaseOtpActivity() {

    @Inject
    lateinit var userSession: UserSessionInterface

    var isResetPin2FA = false
    var otpData = OtpData()
    private var isLoginRegisterFlow = false

    override fun getNewFragment(): Fragment? = null

    override fun setupFragment(savedInstance: Bundle?) {
        component.inject(this)
        setupParams()
        goToVerificationMethodPage()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeyboardHandler.hideSoftKeyboard(this)
    }

    override fun onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this)
        val fragment = this.supportFragmentManager.findFragmentById(R.id.parent_view)
        (fragment as? IOnBackPressed)?.onBackPressed()?.let { isBackPressed ->
            if (isBackPressed) {
                if (supportFragmentManager.backStackEntryCount > 1) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        }
    }

    private fun setupParams() {
        if (isResetPin2FA || intent?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN) == true) {
            otpData.userId = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_USER_ID, "").toEmptyStringIfNull()
        } else {
            otpData.userId = userSession.userId ?: userSession.temporaryUserId
        }
        otpData.otpType = intent?.extras?.getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0) ?: 0
        otpData.otpMode = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, "").toEmptyStringIfNull()
        otpData.email = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "").toEmptyStringIfNull()
        otpData.msisdn = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "").toEmptyStringIfNull()
        otpData.source = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "").toEmptyStringIfNull()
        otpData.userIdEnc = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_USER_ID_ENC, "").toEmptyStringIfNull()
        otpData.canUseOtherMethod = intent?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false) ?: false
        otpData.isShowChooseMethod = intent?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false) ?: false
        otpData.accessToken = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, "").toEmptyStringIfNull()
        isLoginRegisterFlow = intent?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, false)
                ?: false
    }

    private fun createBundle(modeListData: ModeListData? = null, isMoreThanOne: Boolean = true): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, isLoginRegisterFlow)
        modeListData?.let {
            bundle.putParcelable(OtpConstant.OTP_MODE_EXTRA, it)
        }
        bundle.putBoolean(OtpConstant.IS_MORE_THAN_ONE_EXTRA, isMoreThanOne)
        return bundle
    }

    fun doFragmentTransaction(fragment: Fragment, tag: String, isBackAnimation: Boolean) {
        supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragmentTransactionManager = supportFragmentManager.beginTransaction()

        fragmentTransactionManager.add(parentViewResourceID, fragment, tag)
        if (isBackAnimation)
            fragmentTransactionManager.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
        else fragmentTransactionManager.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_left)
        fragmentTransactionManager.addToBackStack(BACK_STACK_ROOT_TAG)
        fragmentTransactionManager.commit()
    }

    open fun goToVerificationMethodPage() {
        val fragment = VerificationMethodFragment.createInstance(createBundle())
        doFragmentTransaction(fragment, TAG_OTP_MODE, true)
    }

    fun goToVerificationPage(modeListData: ModeListData, isMoreThanOne: Boolean = true) {
        val bundle = createBundle(modeListData, isMoreThanOne)
        val fragment = generateVerificationFragment(modeListData, bundle)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    private fun generateVerificationFragment(modeListData: ModeListData, bundle: Bundle): VerificationFragment {
        return when (modeListData.modeText) {
            OtpConstant.OtpMode.EMAIL -> {
                EmailVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.SMS -> {
                SmsVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.WA -> {
                WhatsappVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.GOOGLE_AUTH -> {
                GoogleAuthVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.PIN -> {
                PinVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.MISCALL -> {
                MisscallVerificationFragment.createInstance(bundle)
            }
            else -> {
                VerificationFragment.createInstance(bundle)
            }
        }
    }

    fun goToOnboardingMiscallPage(modeListData: ModeListData) {
        val fragment = OnboardingMiscallFragment.createInstance(createBundle(modeListData))
        doFragmentTransaction(fragment, TAG_OTP_MISCALL, false)
    }

    fun goToMethodPageResetPin(otpData: OtpData) {
        isResetPin2FA = true
        val bundle = Bundle().apply {
            putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        }
        val fragment = VerificationMethodFragment.createInstance(bundle)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    companion object {
        private const val BACK_STACK_ROOT_TAG = "root_fragment"

        const val TAG_OTP_MODE = "otpMode"
        private const val TAG_OTP_VALIDATOR = "otpValidator"
        private const val TAG_OTP_MISCALL = "otpMiscall"
    }
}