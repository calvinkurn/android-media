package com.tokopedia.feedplus.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedDataModel

/**
 * Created By : Muhammad Furqan on 13/02/23
 */
class FeedPagerAdapter(activity: FragmentActivity, private val tabsList: List<FeedDataModel>) :
    FragmentStateAdapter(activity) {

    private val fragmentList: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int = tabsList.size

    override fun createFragment(position: Int): Fragment {
        if (position >= fragmentList.size) {
            fragmentList.add(
                FeedFragment.createFeedFragment(tabsList[position])
            )
        }

        return fragmentList[position]
    }
}
