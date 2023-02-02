package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedFollowingBinding
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedTabType

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedFollowingViewHolder(private val binding: ItemFeedFollowingBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        val fragment = FeedFragment.createFeedFragment(FeedTabType.FOLLOWING)
        val tag = TAG_FOLLOWING

        val activity = binding.root.context as AppCompatActivity

        if (activity.supportFragmentManager.findFragmentByTag(tag) == null) {
            activity
                .supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFeedFollowingFragment.id, fragment, tag)
                .commit()
        }
    }

    companion object {
        private const val TAG_FOLLOWING = "TAG_FOLLOWING"
    }
}
