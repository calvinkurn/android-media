package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.di.CampaignComponent
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

    object DeeplinkIntents {
        @DeepLink(ApplinkConst.SellerApp.FLASHSALE_MANAGEMENT)
        @JvmStatic
        fun createDeeplinkIntent(context: Context, extras: Bundle): Intent {
            return createIntent(context)
        }
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
