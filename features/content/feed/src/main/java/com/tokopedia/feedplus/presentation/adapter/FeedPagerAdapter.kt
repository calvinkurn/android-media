package com.tokopedia.feedplus.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedDataModel

/**
 * Created By : Muhammad Furqan on 13/02/23
 */
class FeedPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val tabsList: List<FeedDataModel>) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = tabsList.size

    override fun createFragment(position: Int): Fragment =
        FeedFragment.createFeedFragment(tabsList[position])
}
