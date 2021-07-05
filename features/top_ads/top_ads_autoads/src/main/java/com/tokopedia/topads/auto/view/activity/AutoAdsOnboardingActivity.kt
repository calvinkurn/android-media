package com.tokopedia.topads.auto.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.di.module.AutoAdsModule
import com.tokopedia.topads.auto.di.module.AutoAdsQueryModule
import com.tokopedia.topads.auto.view.OnBoardingSliderAdapter
import com.tokopedia.topads.auto.view.fragment.AutoAdsOnboardingFragScreen1
import com.tokopedia.topads.auto.view.fragment.AutoAdsOnboardingFragScreen2
import com.tokopedia.topads.auto.view.fragment.AutoAdsOnboardingFragScreen3
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.topads_auto_onboarding_activity_layout.*

/**
 * Created by Pika on 27/5/20.
 */

private const val CLICK_AKTIFKAN = "click - aktifkan"
class AutoAdsOnboardingActivity : BaseActivity(), HasComponent<AutoAdsComponent> {

    private lateinit var adapter: OnBoardingSliderAdapter

    override fun getComponent(): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).autoAdsQueryModule(AutoAdsQueryModule(this)).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_auto_onboarding_activity_layout)
        renderTabAndViewPager()
        setupToolBar()
        backArrow.setOnClickListener {
            onBackPressed()
        }

        skip_button.setOnClickListener {
            startActivity(Intent(this, CreateAutoAdsActivity::class.java))
            finish()
        }
        btn_submit.setOnClickListener {
            if (view_pager.currentItem == 2) {
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsDashboardEvent(CLICK_AKTIFKAN, "")
                startActivity(Intent(this, CreateAutoAdsActivity::class.java))
                finish()
            } else
                view_pager.currentItem = view_pager.currentItem + 1
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setToolBarStatusBar(position)
            }

        })
    }

    private fun setToolBarStatusBar(position: Int) {
        setStatusBar(position)
        setButton(position)
        when (position) {
            0 -> toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.topads_autoads_onboarding_color1))
            2 -> toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.topads_autoads_onboarding_color1))
            1 -> toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.topads_autoads_onboarding_color2))
        }
    }

    private fun setButton(position: Int) {
        if (position == 2) {
            btn_submit.buttonVariant = UnifyButton.Variant.FILLED
            btn_submit.text = resources.getString(R.string.autoads_coba_iklan)
            skip_button.visibility = View.GONE
        } else {
            btn_submit.buttonVariant = UnifyButton.Variant.GHOST
            btn_submit.text = resources.getString(R.string.autoads_selanjutnya)
            skip_button.visibility = View.VISIBLE

        }
    }

    private fun setStatusBar(position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            if (position == 0 || position == 2)
                window.statusBarColor = ContextCompat.getColor(this, R.color.topads_autoads_onboarding_color1)
            else
                window.statusBarColor = ContextCompat.getColor(this, R.color.topads_autoads_onboarding_color2)
        }
    }

    private fun setupToolBar() {
        setToolBarStatusBar(0)
        setSupportActionBar(toolbar)
    }

    private fun renderTabAndViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.currentItem = 0
    }

    private fun getViewPagerAdapter(): OnBoardingSliderAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        list.add(AutoAdsOnboardingFragScreen1())
        list.add(AutoAdsOnboardingFragScreen2())
        list.add(AutoAdsOnboardingFragScreen3())
        adapter = OnBoardingSliderAdapter(supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    override fun onBackPressed() {
        if (view_pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            view_pager.currentItem = view_pager.currentItem - 1
        }
    }
}