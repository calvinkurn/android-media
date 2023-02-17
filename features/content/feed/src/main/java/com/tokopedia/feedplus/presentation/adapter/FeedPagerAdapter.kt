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

    override fun getItemCount(): Int = tabsList.size

    override fun createFragment(position: Int): Fragment =
        FeedFragment.createFeedFragment(tabsList[position])
}
