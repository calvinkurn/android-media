package com.tokopedia.topads.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.adapter.OnBoardingCreationSliderAdapter
import com.tokopedia.topads.view.fragment.CreationOnboardingFragScreen
import com.tokopedia.topads.view.fragment.CreationOnboardingFragScreen2
import com.tokopedia.topads.view.fragment.CreationOnboardingFragScreen3
import kotlinx.android.synthetic.main.topads_create_onboarding_screen.*

/**
 * Created by Pika on 27/8/20.
 */

class CreationOnboardingActivity : BaseActivity(), HasComponent<CreateAdsComponent>  {

    private lateinit var adapter: OnBoardingCreationSliderAdapter


    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_create_onboarding_screen)
        renderTabAndViewPager()
        header?.setNavigationOnClickListener {
            onBackPressed()
        }
        header?.actionTextView?.setOnClickListener {
            startActivity(Intent(this, AdCreationChooserActivity::class.java))
            finish()
        }

        btn_submit.setOnClickListener {
            if (view_pager.currentItem == 2) {
                startActivity(Intent(this, AdCreationChooserActivity::class.java))
                finish()
            } else
                view_pager.currentItem = view_pager.currentItem + 1
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setTextforFragment(position)
            }

        })
    }

    private fun setTextforFragment(position: Int) {
        if (position == 2) {
            btn_submit.text = resources.getString(R.string.topads_create_onboarding_button2)
        } else {
            btn_submit.text = resources.getString(R.string.topads_create_onboarding_button1)
        }
    }

    private fun renderTabAndViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.currentItem = 0
    }

    private fun getViewPagerAdapter(): OnBoardingCreationSliderAdapter {
        val list: ArrayList<Fragment> = ArrayList()
        list.add(CreationOnboardingFragScreen())
        list.add(CreationOnboardingFragScreen2())
        list.add(CreationOnboardingFragScreen3())
        adapter = OnBoardingCreationSliderAdapter(supportFragmentManager, 0)
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