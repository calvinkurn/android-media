package com.tokopedia.updateinactivephone.features.onboarding.regular

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.replaceFragment
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import com.tokopedia.updateinactivephone.features.submitnewphone.InactivePhoneSubmitDataActivity

class InactivePhoneRegularActivity: BaseSimpleActivity() {

    private var tracker: InactivePhoneTracker? = InactivePhoneTracker()
    private var currentFragment: Fragment? = null
    private var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

    override fun getNewFragment(): Fragment {
        return InactivePhoneOnboardingFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

        intent?.extras?.let {
            inactivePhoneUserDataModel = it.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
        }
    }

    private fun setupToolbar() {
        updateTitle(getString(R.string.text_title))
        toolbar.setTitleTextAppearance(this, R.style.BoldToolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    override fun onBackPressed() {
        sendTrackerOnBackPress()
        if (supportFragmentManager.backStackEntryCount < 1) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun sendTrackerOnBackPress() {
        when(currentFragment) {
            is InactivePhoneOnboardingFragment -> { tracker?.clickOnBackButtonOnbaording() }
            is InactivePhoneCaptureIdCardFragment -> { tracker?.clickOnBackButtonIdCardOnboarding() }
            is InactivePhoneCaptureSelfieFragment -> { tracker?.clickOnBackButtonSelfiewOnboarding() }
        }
    }

    fun gotoOnboardingIdCard() {
        val fragment = InactivePhoneCaptureIdCardFragment()
        currentFragment = fragment
        replaceFragment(parentViewResourceID, fragment)
    }

    fun gotoOnboardingSelfie() {
        val fragment = InactivePhoneCaptureSelfieFragment()
        currentFragment = fragment
        replaceFragment(parentViewResourceID, fragment)
    }

    fun gotoUploadData() {
        val intent = inactivePhoneUserDataModel?.let {
            InactivePhoneSubmitDataActivity.getIntent(this, InactivePhoneConstant.REGULAR, it)
        }

        startActivity(intent)
        finish()
    }

    companion object {
        fun createIntent(context: Context, inactivePhoneUserDataModel: InactivePhoneUserDataModel): Intent {
            return Intent(context, InactivePhoneRegularActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putParcelable(InactivePhoneConstant.PARAM_USER_DATA, inactivePhoneUserDataModel)
                })
            }
        }
    }
}