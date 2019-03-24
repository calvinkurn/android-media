package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemFragment

class TabBusinessViewPagerAdapter(
        fm: FragmentManager?,
        private val list: List<HomeWidget.TabItem>,
        private val positionWidget : Int
): FragmentStatePagerAdapter(fm){

    private var currentPosition = -1

    override fun getItem(position: Int): Fragment {
        return BusinessUnitItemFragment.newInstance(list[position], positionWidget)
    }

    override fun getCount(): Int {
        return list.size
    }

}
