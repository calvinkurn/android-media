package com.tokopedia.otp.validator.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.validator.data.ModeListData
import com.tokopedia.otp.validator.data.OtpParams
import com.tokopedia.otp.validator.di.DaggerValidatorComponent
import com.tokopedia.otp.validator.di.ValidatorComponent
import com.tokopedia.otp.validator.view.fragment.OtpModeListFragment
import com.tokopedia.otp.validator.view.fragment.ValidatorFragment
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 *
 * For navigate: use [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.OTP_VALIDATOR]
 * please pass :
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_MSISDN]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_OTP_TYPE]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_SOURCE]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD]
 * @param : [com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD]
 */
class ValidatorActivity : BaseSimpleActivity(), HasComponent<ValidatorComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var otpParams = OtpParams()
    private var isShowChooseMethod = true

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): ValidatorComponent {
        return DaggerValidatorComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun setupFragment(savedInstance: Bundle?) {
        component.inject(this)
        setupParams(savedInstance)
        goToOtpModeListPage(otpParams)
    }

    private fun setupParams(savedInstance: Bundle?) {
        val bundle = intent.extras
        otpParams.userId = userSession.userId ?: userSession.temporaryUserId

        when {
            savedInstance != null ->
                savedInstance?.run {
                    otpParams.otpType = getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0)
                    otpParams.email = getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
                    otpParams.msisdn = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                    otpParams.source = getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "")
                    otpParams.canUseOtherMethod = getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
                    isShowChooseMethod = getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
                }
            bundle != null ->
                bundle?.run {
                    otpParams.otpType = getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0)
                    otpParams.email = getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
                    otpParams.msisdn = getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                    otpParams.source = getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "")
                    otpParams.canUseOtherMethod = getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
                    isShowChooseMethod = getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
                }
            else -> finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpParams.otpType)
        outState?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, otpParams.email)
        outState?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, otpParams.msisdn)
        outState?.getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, otpParams.canUseOtherMethod)
        outState?.getString(ApplinkConstInternalGlobal.PARAM_SOURCE, otpParams.source)
        outState?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, isShowChooseMethod)
    }

    override fun onBackPressed() {
        KeyboardHandler.hideSoftKeyboard(this)
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    fun goToOtpModeListPage(otpParams: OtpParams) {
        val fragment = OtpModeListFragment.createInstance(otpParams)
        doFragmentTransaction(fragment, TAG_OTP_MODE)
    }

    fun goToValidatorPage(otpParams: OtpParams, modeListData: ModeListData) {
        val fragment = ValidatorFragment.createInstance(otpParams, modeListData)
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR)
    }

    private fun doFragmentTransaction(fragment: Fragment, tag: String) {
        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragmentTransactionManager = supportFragmentManager.beginTransaction()
        fragmentTransactionManager.add(parentViewResourceID, fragment, tag)
        fragmentTransactionManager.addToBackStack(tag)
        fragmentTransactionManager.commit()
    }

    companion object {
        private const val TAG_OTP_MODE = "otpMode"
        private const val TAG_OTP_VALIDATOR = "otpValidator"
        private const val TAG_OTP_MISCALL = "otpMiscall"
    }
}