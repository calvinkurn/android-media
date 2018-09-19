package com.tokopedia.flashsale.management.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.tokopedia.abstraction.base.view.activity.BaseTabActivity
import com.tokopedia.flashsale.management.R
import com.tokopedia.flashsale.management.view.fragment.TokopediaCampaignFragment

class CampaignActivity : BaseTabActivity(){

    companion object {
        @JvmStatic
        fun createIntent(context: Context): Intent {
            return Intent(context, CampaignActivity::class.java)
        }
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
                    0 -> getString(R.string.fm_tokopedia_campaign)
                    1 -> getString(R.string.fm_my_campaign)
                    else -> super.getPageTitle(position)
                }
            }

            override fun getItem(position: Int): Fragment? {
                return when (position) {
                    0 -> TokopediaCampaignFragment.createInstance()
                    1 -> TokopediaCampaignFragment.createInstance()
                    else -> null
                }
            }

            override fun getCount(): Int {
                return pageLimit
            }
        }
    }

    override fun getPageLimit(): Int {
        return 2
    }
}
