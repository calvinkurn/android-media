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
import com.tokopedia.feedplus.presentation.uiview.FeedProductButtonView
import com.tokopedia.feedplus.presentation.uiview.FeedProductTagView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostImageViewHolder(
    private val binding: ItemFeedPostBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardImageContentModel>(binding.root) {

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
        element?.let { data ->
            binding.apply {
                bindAuthor(data)
                bindCaption(data)
                bindImagesContent(data.media)
                bindIndicators(data.media.size)
                bindProductTag(data)

                menuButton.setOnClickListener { _ ->
                    listener.onMenuClicked(data.id)
                }
                shareButton.setOnClickListener {
                    listener.onSharePostClicked(
                        id = data.id,
                        authorName = data.author.name,
                        applink = data.applink,
                        weblink = data.weblink,
                        imageUrl = data.media.firstOrNull()?.mediaUrl ?: ""
                    )
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

    private fun bindAuthor(model: FeedCardImageContentModel) {
        val authorView =
            FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
        authorView.bindData(model.author, false, !model.followers.isFollowed)
    }

    private fun bindCaption(model: FeedCardImageContentModel) {
        val captionView = FeedCaptionView(binding.tvFeedCaption)
        captionView.bind(model.text)
    }

    private fun bindProductTag(model: FeedCardImageContentModel) {
        val productTagView = FeedProductTagView(binding.productTagView, listener)
        productTagView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            hasVoucher = model.hasVoucher,
            products = model.products,
            totalProducts = model.totalProducts
        )

        val productButtonView = FeedProductButtonView(binding.productTagButton, listener)
        productButtonView.bindData(
            postId = model.id,
            author = model.author,
            postType = model.typename,
            isFollowing = model.followers.isFollowed,
            campaign = model.campaign,
            hasVoucher = model.hasVoucher,
            totalProducts = model.totalProducts
        )
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
            layoutAuthorInfo.root.hide()
            tvFeedCaption.hide()
            likeButton.hide()
            commentButton.hide()
            menuButton.hide()
            shareButton.hide()
            productTagButton.root.hide()
            productTagView.root.hide()
            btnDisableClearMode.show()
        }
    }

    private fun hideClearView() {
        with(binding) {
            layoutAuthorInfo.root.show()
            tvFeedCaption.show()
            likeButton.show()
            commentButton.show()
            menuButton.show()
            shareButton.show()
            productTagButton.root.show()
            productTagView.root.show()
            btnDisableClearMode.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post
    }
}
