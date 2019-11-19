package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemFragment

class TabBusinessViewPagerAdapter(
        fm: FragmentManager?,
        private val list: List<HomeWidget.TabItem>,
        private val backgroundColor: String,
        private val positionWidget : Int
): FragmentStatePagerAdapter(fm){

    private var currentPosition = -1

    override fun getItem(position: Int): Fragment {
        return BusinessUnitItemFragment.newInstance(
                list[position],
                backgroundColor,
                positionWidget,
                list[position].name
        )
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (list.isNotEmpty()) {
            list[position].name
        } else ""
    }
}
