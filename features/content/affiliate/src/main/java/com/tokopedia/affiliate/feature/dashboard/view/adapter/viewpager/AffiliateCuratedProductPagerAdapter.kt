package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.affiliate.feature.dashboard.view.fragment.AffiliateCuratedProductFragment

/**
 * Created by jegul on 2019-09-04.
 */
class AffiliateCuratedProductPagerAdapter(
        fragmentManager: FragmentManager,
        private val fragmentListCurated: List<AffiliateCuratedProductFragment>
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentListCurated[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun getCount(): Int {
        return fragmentListCurated.size
    }
}