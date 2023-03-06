package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.presentation.adapter.FeedPostImageAdapter
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedMediaModel
import com.tokopedia.feedplus.presentation.uiview.FeedAuthorInfoView
import com.tokopedia.feedplus.presentation.uiview.FeedCaptionView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostViewHolder(
    private val binding: ItemFeedPostBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardImageContentModel>(binding.root) {

    private val authorView: FeedAuthorInfoView = FeedAuthorInfoView(binding.layoutAuthorInfo)
    private val captionView: FeedCaptionView = FeedCaptionView(binding.tvFeedCaption)

    private val layoutManager =
        LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)

    init {
        with(binding) {
            indicatorFeedContent.activeColor = ContextCompat.getColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
            indicatorFeedContent.inactiveColor = ContextCompat.getColor(
                binding.root.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White_44
            )

            rvFeedPostImageContent.layoutManager = layoutManager
            LinearSnapHelper().attachToRecyclerView(rvFeedPostImageContent)
            rvFeedPostImageContent.addOnScrollListener(object : OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    indicatorFeedContent.setCurrentIndicator(layoutManager.findFirstVisibleItemPosition())
                }
            })
        }
    }

    override fun bind(element: FeedCardImageContentModel?) {
        element?.let {
            binding.apply {
                authorView.bindData(element.author, false, !element.followers.isFollowed)
                captionView.bind(element.text)

                bindImagesContent(element.media)
                bindIndicators(element.media.size)

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

    private fun bindIndicators(imageSize: Int) {
        with(binding) {
            indicatorFeedContent.setIndicator(imageSize)
            indicatorFeedContent.showWithCondition(imageSize > 1)
        }
    }

    private fun bindImagesContent(media: List<FeedMediaModel>) {
        with(binding) {
            val adapter = FeedPostImageAdapter(media.map { it.mediaUrl })
            rvFeedPostImageContent.adapter = adapter
        }
    }

    private fun showClearView() {
        with(binding) {
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
        with(binding) {
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
