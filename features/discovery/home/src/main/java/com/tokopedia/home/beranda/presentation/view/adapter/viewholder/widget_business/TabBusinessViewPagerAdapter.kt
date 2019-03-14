package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import com.tokopedia.digital.product.view.compoundview.DigitalWrapContentViewPager
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemFragment

class TabBusinessViewPagerAdapter(
        fm: FragmentManager?,
        private val list: List<HomeWidget.TabItem>
): FragmentStatePagerAdapter(fm){

    private var currentPosition = -1

    override fun getItem(position: Int): Fragment {
        return BusinessUnitItemFragment.newInstance(list[position])
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != currentPosition) {
            val fragment = `object` as Fragment
            val pager = container as DigitalWrapContentViewPager
            if (fragment.view != null) {
                currentPosition = position
                pager.measureCurrentView(fragment.view)
            }
        }
    }

}
