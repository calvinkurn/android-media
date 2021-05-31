package com.tokopedia.play.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by jegul on 19/01/21
 */
class SwipeContainerStateAdapter(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        private val fragmentCreator: (channelId: String) -> Fragment
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val channelIds = mutableListOf<String>()

    override fun getItemCount(): Int {
        return channelIds.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentCreator(channelIds[position])
    }

    fun setChannelList(newChannelIds: List<String>) {
        val diffUtilCallback = SwipeContainerDiffUtil(channelIds, newChannelIds)
        val diff = DiffUtil.calculateDiff(diffUtilCallback)
        channelIds.clear()
        channelIds.addAll(newChannelIds)
        diff.dispatchUpdatesTo(this)
    }

    class SwipeContainerDiffUtil(
            private val oldList: List<String>,
            private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}