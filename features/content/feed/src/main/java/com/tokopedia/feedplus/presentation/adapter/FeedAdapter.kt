package com.tokopedia.feedplus.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedRootBinding
import com.tokopedia.feedplus.presentation.activity.FeedActivity
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedViewHolder
import com.tokopedia.feedplus.presentation.model.FeedTabType

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedAdapter : RecyclerView.Adapter<FeedViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        context = parent.context
        val view = ItemFeedRootBinding.inflate(LayoutInflater.from(context), parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        context?.let {
            holder.bind(
                it,
                if (position == FeedActivity.TAB_FOR_YOU_INDEX) FeedTabType.FOR_YOU
                else FeedTabType.FOLLOWING
            )
        }
    }

    override fun getItemCount(): Int = ITEM_COUNT

    companion object {
        private const val ITEM_COUNT = 2
    }
}
