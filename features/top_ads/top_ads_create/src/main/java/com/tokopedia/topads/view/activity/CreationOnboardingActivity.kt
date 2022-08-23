package com.tokopedia.topads.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.widget.TouchViewPager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.adapter.OnBoardingCreationSliderAdapter
import com.tokopedia.topads.view.fragment.CreationOnboardingFragScreen
import com.tokopedia.topads.view.fragment.CreationOnboardingFragScreen2
import com.tokopedia.topads.view.fragment.CreationOnboardingFragScreen3
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Pika on 27/8/20.
 */

class CreationOnboardingActivity : BaseActivity(), HasComponent<CreateAdsComponent> {

    private var header: HeaderUnify? = null
    private var viewPager: TouchViewPager? = null
    private var btnSubmit: UnifyButton? = null

    private lateinit var adapter: OnBoardingCreationSliderAdapter

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun initView() {
        header = findViewById(R.id.header)
        viewPager = findViewById(R.id.view_pager)
        btnSubmit = findViewById(R.id.btn_submit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_create_onboarding_screen)

        initView()

        renderTabAndViewPager()
        header?.setNavigationOnClickListener {
            onBackPressed()
        }
        header?.actionTextView?.setOnClickListener {
            startActivity(Intent(this, AdCreationChooserActivity::class.java))
            finish()
        }

        btnSubmit?.setOnClickListener {
            if (viewPager?.currentItem == 2) {
                startActivity(Intent(this, AdCreationChooserActivity::class.java))
                finish()
            } else
                viewPager?.let { view_pager ->
                    view_pager.currentItem = view_pager.currentItem - 1
                }
        }

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
            }

            override fun onPageSelected(position: Int) {
                setTextforFragment(position)
            }

        })
    }

    private fun setTextforFragment(position: Int) {
        if (position == 2) {
            btnSubmit?.text = resources.getString(R.string.topads_create_onboarding_button2)
        } else {
            btnSubmit?.text = resources.getString(R.string.topads_create_onboarding_button1)
        }
    }

    private fun renderTabAndViewPager() {
        viewPager?.adapter = getViewPagerAdapter()
        viewPager?.currentItem = 0
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
        if (viewPager?.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager?.let { view_pager ->
                view_pager.currentItem = view_pager.currentItem - 1
            }
        }
    }
}