package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostViewHolder(
    private val binding: ItemFeedPostBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardImageContentModel>(binding.root) {

    val authorView: FeedAuthorInfoView = FeedAuthorInfoView(binding.layoutAuthorInfo)

    override fun bind(element: FeedCardImageContentModel?) {
        element?.let {
            binding.apply {
                tvFeed.text = it.text
                authorView.bindData(element.author, false, !element.followers.isFollowed)

                menuButton.setOnClickListener { _ ->
                    listener.onMenuClicked(it)
                }

                btnDisableClearMode.setOnClickListener {
                    if (listener.inClearViewMode()) {
                        listener.disableClearView()
                    }
                }

                if (listener.inClearViewMode()) {
                    showClearView()
                } else {
                    hideClearView()
                }

                root.setOnClickListener {
                }
            }
        }
    }

    override fun bind(element: FeedCardImageContentModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
    }

    private fun showClearView() {
        binding.apply {
            authorView.showClearView()
            tvFeedCaption.hide()
            likeButton.hide()
            commentButton.hide()
            menuButton.hide()
            shareButton.hide()
            productTagButton.hide()

            btnDisableClearMode.show()
        }
    }

    private fun hideClearView() {
        binding.apply {
            authorView.hideClearView()
            tvFeedCaption.show()
            likeButton.show()
            commentButton.show()
            menuButton.show()
            shareButton.show()
            productTagButton.show()

            btnDisableClearMode.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post
    }
}
