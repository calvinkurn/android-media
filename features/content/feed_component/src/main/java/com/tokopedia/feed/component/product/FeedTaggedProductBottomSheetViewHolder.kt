package com.tokopedia.feed.component.product

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.databinding.ItemFeedTaggedProductBinding

/**
 *Created by shruti.agarwal on 23/02/23
 */
class FeedTaggedProductBottomSheetViewHolder(
    private val binding: ItemFeedTaggedProductBinding,
    listener: Listener
) : BaseViewHolder(binding.root) {

    init {
        binding.root.setListener(object : FeedTaggedProductBottomSheetItemView.Listener {
            override fun onProductCardClicked(
                view: FeedTaggedProductBottomSheetItemView,
                product: FeedTaggedProductUiModel
            ) {
                listener.onProductCardClicked(product, bindingAdapterPosition)
            }

            override fun onAddToCartProductButtonClicked(
                view: FeedTaggedProductBottomSheetItemView,
                product: FeedTaggedProductUiModel
            ) {
                listener.onAddToCartProductButtonClicked(product, bindingAdapterPosition)
            }

            override fun onBuyProductButtonClicked(
                view: FeedTaggedProductBottomSheetItemView,
                product: FeedTaggedProductUiModel
            ) {
                listener.onBuyProductButtonClicked(product, bindingAdapterPosition)
            }
        })
    }

    fun bind(item: FeedTaggedProductUiModel) {
        binding.root.bindData(item)
    }

    interface Listener {
        fun onProductCardClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        )

        fun onAddToCartProductButtonClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        )

        fun onBuyProductButtonClicked(
            product: FeedTaggedProductUiModel,
            itemPosition: Int
        )
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener
        ) = FeedTaggedProductBottomSheetViewHolder(
            ItemFeedTaggedProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener
        )
    }
}
