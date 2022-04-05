package com.tokopedia.createpost.view.plist

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class SearchResultPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    private val pageCount = 2
    override fun getCount(): Int {
        return pageCount
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            1 -> TokoSearchResultFragment.newInstance(Bundle())
            else  -> BarangSearchResultFragment.newInstance(Bundle())
        }

    }
}