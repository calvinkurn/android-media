package com.tokopedia.affiliate.feature.dashboard.view.adapter.viewpager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
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