package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedRootBinding
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedTabType

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedViewHolder(private val binding: ItemFeedRootBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, type: FeedTabType) {
        val fragment = FeedFragment.createFeedFragment(type)
        val tag = if (type == FeedTabType.FOR_YOU) TAG_FOR_YOU else TAG_FOLLOWING

        val activity = context as AppCompatActivity

        if (activity.supportFragmentManager.findFragmentByTag(tag) == null) {
            activity
                .supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFeedFragment.id, fragment, tag)
                .commit()
        }
    }

    companion object {
        private const val TAG_FOR_YOU = "TAG_FOR_YOU"
        private const val TAG_FOLLOWING = "TAG_FOLLOWING"
    }
}
