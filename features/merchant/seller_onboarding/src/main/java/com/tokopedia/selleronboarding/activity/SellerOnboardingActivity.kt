package com.tokopedia.selleronboarding.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.requestStatusBarDark
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.adapter.ViewPagerAdapter
import com.tokopedia.selleronboarding.fragment.SellerOnboardingFragment
import com.tokopedia.selleronboarding.utils.StatusBarHelper
import kotlinx.android.synthetic.main.activity_sob_onboarding.*

/**
 * Created By @ilhamsuaib on 09/04/20
 */

class SellerOnboardingActivity : BaseActivity() {

    private val viewPagerAdapter by lazy {
        ViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sob_onboarding)

        setupView()
        initOnboardingPages()
    }

    private fun setupView() {
        viewPagerSob.adapter = viewPagerAdapter
        viewPagerSob.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pageIndicatorSob.setCurrentIndicator(position)
            }
        })

        btnSobOpenApp.setOnClickListener {
            openApp()
        }

        setWhiteStatusBar()
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
        rootLayoutSob.setPadding(0, statusBarHeight, 0, 0)
    }

    private fun initOnboardingPages() {
        viewPagerAdapter.clearFragments()
        viewPagerAdapter.addFragment(SellerOnboardingFragment.newInstance(SellerOnboardingFragment.PAGE_1))
        viewPagerAdapter.addFragment(SellerOnboardingFragment.newInstance(SellerOnboardingFragment.PAGE_2))
        viewPagerAdapter.addFragment(SellerOnboardingFragment.newInstance(SellerOnboardingFragment.PAGE_3))

        pageIndicatorSob.setIndicator(viewPagerAdapter.itemCount)
    }

    private fun openApp() {

    }
}