package com.tokopedia.topads.dashboard.view.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.tokopedia.topads.dashboard.R

/**
 * Created by zulfikarrahman on 1/5/17.
 */

class TopAdsStatisticPagerAdapter(private val context: Context, fm: FragmentManager, private val fragmentList: List<Fragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        when (position) {
            0 -> return context.getString(R.string.label_top_ads_impression)
            1 -> return context.getString(R.string.label_top_ads_click)
            2 -> return context.getString(R.string.label_top_ads_ctr)
            3 -> return context.getString(R.string.label_top_ads_conversion)
            4 -> return context.getString(R.string.label_top_ads_average)
            5 -> return context.getString(R.string.label_top_ads_cost)
            else -> return ""
        }
    }

}
