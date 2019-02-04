package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.di.CampaignComponent
import com.tokopedia.flashsale.management.di.CampaignModule
import com.tokopedia.flashsale.management.di.DaggerCampaignComponent
import com.tokopedia.flashsale.management.view.fragment.MyCampaignFragment
import com.tokopedia.flashsale.management.view.fragment.UpcomingCampaignFragment

class CampaignActivity : BaseTabActivity(), HasComponent<CampaignComponent> {

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent {
            return Intent(context, CampaignActivity::class.java)
        }

        private const val LIMIT_PAGER = 2
    }

    override fun getComponent(): CampaignComponent {
        return DaggerCampaignComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun getViewPagerAdapter(): PagerAdapter {
        return object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    0 -> getString(R.string.fm_upcoming_flash_sale)
                    1 -> getString(R.string.fm_my_flash_sale)
                    else -> super.getPageTitle(position)
                }
            }

            override fun getItem(position: Int): Fragment? {
                return when (position) {
                    0 -> UpcomingCampaignFragment.createInstance()
                    1 -> MyCampaignFragment.createInstance()
                    else -> null
                }
            }

            override fun getCount(): Int {
                return pageLimit
            }
        }
    }

    override fun getPageLimit(): Int {
        return LIMIT_PAGER
    }
}
