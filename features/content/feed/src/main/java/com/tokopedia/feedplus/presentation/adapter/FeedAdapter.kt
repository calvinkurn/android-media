package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedFollowingBinding
import com.tokopedia.feedplus.databinding.ItemFeedForYouBinding
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedFollowingViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedForYouViewHolder
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            FeedBaseFragment.TAB_FOR_YOU_INDEX -> {
                val view =
                    ItemFeedForYouBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                FeedForYouViewHolder(view)
            }
            else -> {
                val view =
                    ItemFeedFollowingBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                FeedFollowingViewHolder(view)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == FeedBaseFragment.TAB_FOR_YOU_INDEX) {
            (holder as FeedForYouViewHolder).bind()
        } else if (position == FeedBaseFragment.TAB_FOLLOWING_INDEX) {
            (holder as FeedFollowingViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = ITEM_COUNT

    override fun getItemViewType(position: Int): Int = position

    companion object {
        private const val ITEM_COUNT = 2
    }
}
