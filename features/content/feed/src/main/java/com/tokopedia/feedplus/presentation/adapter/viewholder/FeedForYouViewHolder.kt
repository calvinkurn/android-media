package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.databinding.ItemFeedForYouBinding
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedTabType

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedForYouViewHolder(private val binding: ItemFeedForYouBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        val fragment = FeedFragment.createFeedFragment(FeedTabType.FOR_YOU)

        val activity = binding.root.context as AppCompatActivity

        if (activity.supportFragmentManager.findFragmentByTag(TAG_FOR_YOU) == null) {
            activity
                .supportFragmentManager
                .beginTransaction()
                .replace(binding.containerFeedForYouFragment.id, fragment, TAG_FOR_YOU)
                .commit()
        }
    }

    companion object {
        private const val TAG_FOR_YOU = "TAG_FOR_YOU"
    }
}
