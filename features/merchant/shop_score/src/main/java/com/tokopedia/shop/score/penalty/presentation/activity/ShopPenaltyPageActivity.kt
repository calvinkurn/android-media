package com.tokopedia.shop.score.penalty.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ActivityShopPenaltyPageBinding
import com.tokopedia.shop.score.penalty.di.component.DaggerPenaltyComponent
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.adapter.ShopPenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageType

class ShopPenaltyPageActivity: BaseSimpleActivity(), HasComponent<PenaltyComponent> {

    private lateinit var binding: ActivityShopPenaltyPageBinding

    private val vpPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                binding.tabsPenaltyPage.tabLayout.let {
                    it.selectTab(it.getTabAt(position))
                }
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_shop_penalty_page

    override fun getToolbarResourceID(): Int = R.id.toolbar_penalty_page

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityShopPenaltyPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupView()
    }

    override fun onDestroy() {
        binding.vpPenaltyPage.unregisterOnPageChangeCallback(vpPageChangeCallback)
        super.onDestroy()
    }

    private fun setupView() {
        setupToolbar()
        setupViewPager()
        setupTabs()
    }

    private fun setupToolbar() {
        toolbar = binding.toolbarPenaltyPage
        setUpActionBar(toolbar)
    }

    private fun setupTabs() {
        binding.tabsPenaltyPage.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.vpPenaltyPage.setCurrentItem(tab?.position.orZero(), true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun setupViewPager() {
        binding.vpPenaltyPage.run {
            adapter = ShopPenaltyPageAdapter(this@ShopPenaltyPageActivity, getPenaltyFragments())
            registerOnPageChangeCallback(vpPageChangeCallback)
        }
    }

    private fun getPenaltyFragments(): List<Fragment> {
        return listOf(
            ShopPenaltyPageFragment.createInstance(ShopPenaltyPageType.ONGOING),
            ShopPenaltyPageFragment.createInstance(ShopPenaltyPageType.HISTORY)
        )
    }

    override fun getComponent(): PenaltyComponent {
        return DaggerPenaltyComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

}
