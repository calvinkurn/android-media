package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostViewHolder(
    private val binding: ItemFeedPostBinding,
    private val listener: FeedListener
) : AbstractViewHolder<FeedCardModel>(binding.root) {

    override fun bind(element: FeedCardModel?) {
        element?.let {
            binding.apply {
                tvFeed.text = it.text

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
                productTagButton.root.setOnClickListener {
                    listener.onProductTagClicked(element)
                }
                setUpProductTagButtonText(feedCardModel = element)

                root.setOnClickListener {
                }
            }
        }
    }

    private fun setUpProductTagButtonText(feedCardModel: FeedCardModel){
        val productData = feedCardModel.tags
        when (val numberOfTaggedProducts = feedCardModel.totalProducts) {
            0 -> {
                binding.productTagView.root.hide()
                binding.productTagButton.root.hide()
            }
            1 -> {
                binding.productTagView.tvTagProduct.text = productData.firstOrNull()?.name
                binding.productTagButton.tvPlayProductCount.text = "1"
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

    private fun showClearView() {
        binding.apply {
            imgFeedOwnerProfile.hide()
            imgFeedOwnerBadge.hide()
            tvFeedOwnerName.hide()
            labelFeedLive.hide()
            btnFeedFollow.hide()
            tvFeedCaption.hide()
            likeButton.hide()
            commentButton.hide()
            menuButton.hide()
            shareButton.hide()
            productTagButton.root.hide()
            btnDisableClearMode.show()
        }
    }

    private fun hideClearView() {
        binding.apply {
            imgFeedOwnerProfile.show()
            imgFeedOwnerBadge.show()
            tvFeedOwnerName.show()
            labelFeedLive.show()
            btnFeedFollow.show()
            tvFeedCaption.show()
            likeButton.show()
            commentButton.show()
            menuButton.show()
            shareButton.show()
            productTagButton.root.show()

            btnDisableClearMode.hide()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_post

        fun create(
            parent: ViewGroup,
            listener: FeedListener
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
