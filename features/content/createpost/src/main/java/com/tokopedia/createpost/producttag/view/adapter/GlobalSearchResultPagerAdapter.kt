package com.tokopedia.createpost.producttag.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
class GlobalSearchResultPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val pageCount = 2

    override fun getCount(): Int {
        return pageCount
    }

    override fun getItem(position: Int): Fragment {
        TODO("handle this")
//        return when(position){
//            1 -> TokoSearchResultFragment.newInstance(Bundle())
//            else  -> BarangSearchResultFragment.newInstance(Bundle())
//        }
    }
}