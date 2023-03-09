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
                val authorView =
                    FeedAuthorInfoView(binding.layoutAuthorInfo, listener)
                val captionView = FeedCaptionView(binding.tvFeedCaption)

                authorView.bindData(element.author, false, !element.followers.isFollowed)
                captionView.bind(element.text)

                bindImagesContent(element.media)
                bindIndicators(element.media.size)

                menuButton.setOnClickListener { _ ->
                    listener.onMenuClicked(it)
                }
                shareButton.setOnClickListener {
                    listener.onSharePostClicked(element)
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
                productTagButton.root.setOnClickListener {
                    listener.onProductTagItemClicked(element)
                }
                productTagView.root.setOnClickListener {
                    listener.onProductTagViewClicked(element)
                }
                setUpProductTagButtonText(feedCardModel = element)

                root.setOnClickListener {
                }
            }
        }
    }

    override fun bind(element: FeedCardImageContentModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
    }

    private fun setUpProductTagButtonText(feedCardModel: FeedCardImageContentModel) {
        val productData =
            if (feedCardModel.isTypeProductHighlight) feedCardModel.products else feedCardModel.tags

        when (val numberOfTaggedProducts = feedCardModel.totalProducts) {
            PRODUCT_COUNT_ZERO -> {
                binding.productTagView.root.hide()
                binding.productTagButton.root.hide()
            }
            PRODUCT_COUNT_ONE -> {
                binding.productTagView.tvTagProduct.text = productData.firstOrNull()?.name
                binding.productTagButton.tvPlayProductCount.text = numberOfTaggedProducts.toString()
            }
            else -> {
                binding.productTagView.tvTagProduct.text =
                    itemView.context.getString(
                        R.string.feeds_tag_product_text,
                        numberOfTaggedProducts
                    )
                binding.productTagButton.tvPlayProductCount.text = numberOfTaggedProducts.toString()
            }
        }
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
            layoutAuthorInfo.hide()
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
            layoutAuthorInfo.hideClearView()
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
        const val PRODUCT_COUNT_ZERO = 0
        const val PRODUCT_COUNT_ONE = 1

        @LayoutRes
        val LAYOUT = R.layout.item_feed_post
    }
}
