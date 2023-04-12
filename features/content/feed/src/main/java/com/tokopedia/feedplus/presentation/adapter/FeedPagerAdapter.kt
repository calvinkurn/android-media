package com.tokopedia.feedplus.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedDataModel

/**
 * Created By : Muhammad Furqan on 13/02/23
 */
class FeedPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val appLinkExtras: Bundle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val differ = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<FeedDataModel>() {
            override fun areItemsTheSame(oldItem: FeedDataModel, newItem: FeedDataModel): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(
                oldItem: FeedDataModel,
                newItem: FeedDataModel
            ): Boolean {
                return oldItem == newItem
            }
        },
    )

    fun setTabsList(tabsList: List<FeedDataModel>) {
        differ.submitList(tabsList)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun createFragment(position: Int): Fragment =
        FeedFragment.createFeedFragment(differ.currentList[position], appLinkExtras)
}
