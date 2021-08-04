package com.tokopedia.selleronboarding.old.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.old.fragment.SellerOnboardingFragment
import com.tokopedia.selleronboarding.old.utils.StatusBarHelper
import kotlinx.android.synthetic.main.activity_sob_old_onboarding.*

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SellerOnboardingActivity : BaseActivity() {

    private val onboardingFragment: SellerOnboardingFragment by lazy {
        SellerOnboardingFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sob_old_onboarding)

        showFragment()
        setWhiteStatusBar()
    }

    private fun showFragment() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.onboardingContainer, onboardingFragment)
                .commitNowAllowingStateLoss()
    }

    private fun setWhiteStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
            requestStatusBarDark()

            addParentLayoutPadding()
        }
    }

    private fun addParentLayoutPadding() {
        val statusBarHeight = StatusBarHelper.getStatusBarHeight(this)
        onboardingContainer.setPadding(0, statusBarHeight, 0, 0)
    }
}