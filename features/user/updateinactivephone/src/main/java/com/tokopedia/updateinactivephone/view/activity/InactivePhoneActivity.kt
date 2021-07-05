package com.tokopedia.updateinactivephone.view.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.common.replaceFragment
import com.tokopedia.updateinactivephone.view.InactivePhoneTracker
import com.tokopedia.updateinactivephone.view.fragment.InactivePhoneOnboardingFragment
import com.tokopedia.updateinactivephone.view.fragment.InactivePhoneOnboardingIdCardFragment
import com.tokopedia.updateinactivephone.view.fragment.InactivePhoneOnboardingSelfieFragment

class InactivePhoneActivity : BaseSimpleActivity(), FragmentTransactionInterface {

    lateinit var tracker: InactivePhoneTracker

    private lateinit var remoteConfig: FirebaseRemoteConfigImpl
    private var currentFragment = Fragment()

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent?.extras != null) {
            bundle.putAll(intent?.extras)
        }

        return InactivePhoneOnboardingFragment.createInstance(bundle)
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        updateTitle(getString(R.string.text_title))
        toolbar.setTitleTextAppearance(this, R.style.BoldToolbar)
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        remoteConfig = FirebaseRemoteConfigImpl(this)
        tracker = InactivePhoneTracker()

        versionChecker()
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
            is InactivePhoneOnboardingFragment -> { tracker.clickOnBackButtonOnbaording() }
            is InactivePhoneOnboardingIdCardFragment -> { tracker.clickOnBackButtonIdCardOnboarding() }
            is InactivePhoneOnboardingSelfieFragment -> { tracker.clickOnBackButtonSelfiewOnboarding() }
        }
    }

    override fun replace(fragment: Fragment) {
        replaceFragment(parentViewResourceID, fragment)
        currentFragment = fragment
    }

    private fun versionChecker() {
        val minimumVersionSeller = remoteConfig.getLong(KEY_MINIMUM_VERSION_SELLER)
        val minimumVersionCustomer = remoteConfig.getLong(KEY_MINIMUM_VERSION_CUSTOMER)

        if (GlobalConfig.isSellerApp()) {
            if (GlobalConfig.VERSION_CODE < minimumVersionSeller) showDialogChecker()
        } else {
            if (GlobalConfig.VERSION_CODE < minimumVersionCustomer) showDialogChecker()
        }
    }

    private fun showDialogChecker() {
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.text_update_dialog_title))
            setDescription(getString(R.string.text_update_dialog_description))
            setPrimaryCTAText(getString(R.string.text_update_cta_primary))
            setSecondaryCTAText(getString(R.string.text_update_cta_secondary))
            setPrimaryCTAClickListener {
                gotoPlayStore()
            }
            setSecondaryCTAClickListener {
                finish()
            }
            setCancelable(false)
            setOverlayClose(false)
        }.show()
    }

    private fun gotoPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(APPLINK_PLAY_STORE + getAppPackageName()))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_PLAY_STORE + getAppPackageName()))
            startActivity(intent)
        }
    }

    private fun getAppPackageName(): String {
        return applicationContext.packageName
    }

    companion object {

        private const val APPLINK_PLAY_STORE = "market://details?id="
        private const val URL_PLAY_STORE = "https://play.google.com/store/apps/details?id="

        private const val KEY_MINIMUM_VERSION_CUSTOMER = "key_android_inactive_phone_minimum_version_customer"
        private const val KEY_MINIMUM_VERSION_SELLER = "key_android_inactive_phone_minimum_version_seller"

        fun getIntent(context: Context): Intent {
            return Intent(context, InactivePhoneActivity::class.java)
        }
    }
}
