package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedModel

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostViewHolder(
    private val binding: ItemFeedPostBinding,
    private val listener: FeedListener,
) : AbstractViewHolder<Visitable<*>>(binding.root) {

    override fun bind(element: Visitable<*>?) {
        binding.apply {
            if (element is FeedModel) {
                tvFeed.text = element.text
                menuButton.setOnClickListener {
                    listener.onMenuClicked(element)
                }

                root.setOnClickListener {

                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post

        fun create(
            parent: ViewGroup,
            listener: FeedListener,
        ): FeedPostViewHolder {
            return FeedPostViewHolder(
                ItemFeedPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

}
