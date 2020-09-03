package com.tokopedia.otp.verification.view.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.verification.common.IOnBackPressed
import com.tokopedia.otp.verification.common.di.VerificationComponent
import com.tokopedia.otp.verification.common.di.VerificationComponentBuilder
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.ModeListData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.view.fragment.OnboardingMiscallFragment
import com.tokopedia.otp.verification.view.fragment.VerificationFragment
import com.tokopedia.otp.verification.view.fragment.VerificationMethodFragment
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
class VerificationActivity : BaseSimpleActivity(), HasComponent<VerificationComponent> {

    @Inject
    lateinit var userSession: UserSessionInterface

    private var otpData = OtpData()

    override fun getNewFragment(): Fragment? = null

    override fun getComponent(): VerificationComponent = VerificationComponentBuilder.getComponent(application as BaseMainApplication)

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
            if(isBackPressed) {
                if (supportFragmentManager.backStackEntryCount > 1) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        toolbar = findViewById(toolbarResourceID)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            elevation = 0f
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@VerificationActivity, R.color.Neutral_N0)))
        }
    }

    @SuppressLint("InlinedApi")
    override fun setupStatusBar() {
        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.KITKAT until Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setWindowFlag(false)
            window.statusBarColor = ContextCompat.getColor(this, R.color.Neutral_N0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setWindowFlag(on: Boolean) {
        val winParams = window.attributes
        if (on) {
            winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }

    private fun setupParams() {
        otpData.userId = userSession.userId ?: userSession.temporaryUserId
        otpData.otpType = intent?.extras?.getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0) ?: 0
        otpData.otpMode = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, "") ?: ""
        otpData.email = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "") ?: ""
        otpData.msisdn = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                ?: ""
        otpData.source = intent?.extras?.getString(ApplinkConstInternalGlobal.PARAM_SOURCE, "")
                ?: ""
        otpData.canUseOtherMethod = intent?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
                ?: false
        otpData.isShowChooseMethod = intent?.extras?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
                ?: true
    }

    private fun createBundle(modeListData: ModeListData? = null): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(OtpConstant.OTP_DATA_EXTRA, otpData)
        modeListData?.let {
            bundle.putParcelable(OtpConstant.OTP_MODE_EXTRA, it)
        }
        return bundle
    }

    private fun doFragmentTransaction(fragment: Fragment, tag: String, isBackAnimation: Boolean) {
        supportFragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragmentTransactionManager = supportFragmentManager.beginTransaction()

        fragmentTransactionManager.add(parentViewResourceID, fragment, tag)
        if(isBackAnimation)
            fragmentTransactionManager.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right)
        else fragmentTransactionManager.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_left)
        fragmentTransactionManager.addToBackStack(BACK_STACK_ROOT_TAG)
        fragmentTransactionManager.commit()
    }

    fun goToVerificationMethodPage() {
        val fragment = VerificationMethodFragment.createInstance(createBundle())
        doFragmentTransaction(fragment, TAG_OTP_MODE, true)
    }

    fun goToVerificationPage(modeListData: ModeListData) {
        val fragment = VerificationFragment.createInstance(createBundle(modeListData))
        doFragmentTransaction(fragment, TAG_OTP_VALIDATOR, false)
    }

    fun goToOnboardingMiscallPage(modeListData: ModeListData) {
        val fragment = OnboardingMiscallFragment.createInstance(createBundle(modeListData))
        doFragmentTransaction(fragment, TAG_OTP_MISCALL, false)
    }

    companion object {
        private const val BACK_STACK_ROOT_TAG = "root_fragment"

        private const val TAG_OTP_MODE = "otpMode"
        private const val TAG_OTP_VALIDATOR = "otpValidator"
        private const val TAG_OTP_MISCALL = "otpMiscall"
    }
}