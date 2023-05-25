package com.tokopedia.shop.score.penalty.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
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

class ShopPenaltyPageActivity: BaseSimpleActivity(), HasComponent<PenaltyComponent> {

    private lateinit var binding: ActivityShopPenaltyPageBinding

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int = R.layout.activity_shop_penalty_page

    override fun getToolbarResourceID(): Int = R.id.toolbar_penalty_page

    override fun setupLayout(savedInstanceState: Bundle?) {
        binding = ActivityShopPenaltyPageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupView()
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
        binding.vpPenaltyPage.adapter = ShopPenaltyPageAdapter(this, getPenaltyFragments())
    }

    private fun getPenaltyFragments(): List<Fragment> {
        return listOf(ShopPenaltyPageFragment(), ShopPenaltyPageFragment())
    }

    override fun getComponent(): PenaltyComponent {
        return DaggerPenaltyComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

}
