package com.tokopedia.topupbills.telco.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.topupbills.telco.view.model.DigitalProductTelcoItem

/**
 * Created by nabillasabbaha on 09/05/19.
 */
class DigitalTelcoProductTabAdapter(val productTabList: List<DigitalProductTelcoItem>, fm: FragmentManager)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return productTabList.get(position).fragment
    }

    override fun getCount(): Int {
        return productTabList.size;
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return productTabList.get(position).title
    }
}