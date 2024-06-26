package com.tokopedia.verification.otp.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.verification.R
import com.tokopedia.verification.common.IOnBackPressed
import com.tokopedia.verification.common.abstraction.BaseOtpActivity
import com.tokopedia.verification.otp.data.OtpConstant
import com.tokopedia.verification.otp.data.OtpData
import com.tokopedia.verification.otp.domain.pojo.ModeListData
import com.tokopedia.verification.otp.view.fragment.*
import com.tokopedia.verification.otp.view.fragment.inactivephone.InactivePhoneEmailVerificationFragment
import com.tokopedia.verification.otp.view.fragment.inactivephone.InactivePhonePinVerificationFragment
import com.tokopedia.verification.otp.view.fragment.inactivephone.InactivePhoneSmsVerificationFragment
import com.tokopedia.verification.otp.view.fragment.inactivephone.InactivePhoneVerificationMethodFragment
import com.tokopedia.verification.otp.view.uimodel.DefaultOtpUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 *
 * For navigate: use [com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.COTP]
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
    var isDefaultOtp = false

    var otpData = OtpData()
    private var isLoginRegisterFlow = false

    var otpCache: DefaultOtpUiModel? = null

    override fun getTagFragment(): String = TAG

    override fun getNewFragment(): Fragment {
        setupParams()
        return createVerificationMethodFragment(createBundle())
    }

    protected open fun createVerificationMethodFragment(bundle: Bundle): Fragment {
        return if (otpData.source == SOURCE_INACTIVE_PHONE) {
            InactivePhoneVerificationMethodFragment.createInstance(bundle)
        } else {
            VerificationMethodFragment.createInstance(bundle)
        }
    }

    override fun setupFragment(savedInstance: Bundle?) {
        component.inject(this)
        super.setupFragment(savedInstance)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null && savedInstanceState.containsKey(OtpConstant.OTP_DATA_EXTRA)) {
            val oldOtpData = savedInstanceState.getParcelable<OtpData>(OtpConstant.OTP_DATA_EXTRA)
            if(oldOtpData != null) {
                otpData = oldOtpData
            }
        }
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

    open fun setupParams() {
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

    protected fun createBundle(modeListData: ModeListData? = null, isMoreThanOne: Boolean = true): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, isLoginRegisterFlow)
        modeListData?.let {
            bundle.putParcelable(OtpConstant.OTP_MODE_EXTRA, it)
        }
        bundle.putBoolean(OtpConstant.IS_MORE_THAN_ONE_EXTRA, isMoreThanOne)
        return bundle
    }

    fun createBundleForDefaultOtp(modeListData: ModeListData? = null, isMoreThanOne: Boolean = true, isDefaultOtpFlow: Boolean): Bundle {
        return Bundle().apply {
            putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, isLoginRegisterFlow)
            modeListData?.let {
                putParcelable(OtpConstant.OTP_MODE_EXTRA, it)
            }
            putBoolean(OtpConstant.IS_MORE_THAN_ONE_EXTRA, isMoreThanOne)
        }
    }

    fun doFragmentTransaction(fragment: Fragment, tag: String, isBackAnimation: Boolean) {
        if(supportFragmentManager.isStateSaved || fragment.isAdded) {
            return
        }

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

    fun goToVerificationPageDefaultFlow(modeListData: ModeListData, isMoreThanOne: Boolean = true) {
        val bundle = createBundleForDefaultOtp(modeListData, isMoreThanOne, true)
        val fragment = generateVerificationFragment(modeListData, bundle)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    fun goToWhatsappNotRegistered(title: String, subtitle: String, imgLink: String) {
        val bundle = Bundle()
        bundle.putString(OtpConstant.OTP_WA_NOT_REGISTERED_TITLE, title)
        bundle.putString(OtpConstant.OTP_WA_NOT_REGISTERED_SUBTITLE, subtitle)
        bundle.putString(OtpConstant.OTP_WA_NOT_REGISTERED_IMG_LINK, imgLink)
        val fragment = WhatsappNotRegisteredFragment.createInstance(bundle)
        doFragmentTransaction(fragment, TAG_OTP_WA_NOT_REGISTERED, false)
    }

    open fun generateVerificationFragment(modeListData: ModeListData, bundle: Bundle): VerificationFragment {
        if (otpData.source == SOURCE_INACTIVE_PHONE) {
            return getVerificationForInactivePhoneFlow(modeListData, bundle)
        }

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
            else -> {
                VerificationFragment.createInstance(bundle)
            }
        }
    }

    private fun getVerificationForInactivePhoneFlow(modeListData: ModeListData, bundle: Bundle): VerificationFragment {
        return when(modeListData.modeText) {
            OtpConstant.OtpMode.EMAIL -> {
                InactivePhoneEmailVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.PIN -> {
                InactivePhonePinVerificationFragment.createInstance(bundle)
            }
            OtpConstant.OtpMode.SMS -> {
                InactivePhoneSmsVerificationFragment.createInstance(bundle)
            }
            else -> {
                VerificationFragment.createInstance(bundle)
            }
        }
    }

    open fun goToSilentVerificationpage(modeListData: ModeListData) {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalUserPlatform.SILENT_VERIFICAITON)
        val bundle = createBundle(modeListData)
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_SILENT_VERIF)
    }

    open fun goToMethodPageResetPin(otpData: OtpData) {
        isResetPin2FA = true
        val bundle = Bundle().apply {
            putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        }
        val fragment = VerificationMethodFragment.createInstance(bundle)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
    }

    companion object {
        val TAG = VerificationActivity::class.java.name

        private const val BACK_STACK_ROOT_TAG = "root_fragment"

        const val TAG_OTP_MODE = "otpMode"
        const val TAG_OTP_VALIDATOR = "otpValidator"
        const val TAG_OTP_WA_NOT_REGISTERED = "otpWaNotRegistered"
        const val REQUEST_SILENT_VERIF = 1122

        private const val SOURCE_INACTIVE_PHONE = "inactivePhone"
    }
}
