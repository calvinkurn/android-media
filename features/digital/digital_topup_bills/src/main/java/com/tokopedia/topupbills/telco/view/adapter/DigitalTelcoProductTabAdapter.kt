package com.tokopedia.topupbills.telco.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.topupbills.telco.view.model.DigitalTabTelcoItem

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductTabAdapter(val tabList: List<DigitalTabTelcoItem>, fm: FragmentManager)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return tabList[position].fragment
    }

    override fun getCount(): Int {
        return tabList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabList[position].title
    }
}