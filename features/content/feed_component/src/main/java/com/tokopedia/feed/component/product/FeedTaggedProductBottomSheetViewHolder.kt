package com.tokopedia.feed.component.product

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.view.ContentTaggedProductBottomSheetItemView
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.feedcomponent.databinding.ItemFeedTaggedProductBinding

/**
 *Created by shruti.agarwal on 23/02/23
 */
class FeedTaggedProductBottomSheetViewHolder(
    private val binding: ItemFeedTaggedProductBinding,
    listener: Listener
) : BaseViewHolder(binding.root) {

    init {
        binding.root.setListener(object : ContentTaggedProductBottomSheetItemView.Listener {
            override fun onProductCardClicked(
                view: ContentTaggedProductBottomSheetItemView,
                product: ContentTaggedProductUiModel
            ) {
                listener.onProductCardClicked(product, bindingAdapterPosition)
            }

            override fun onAddToCartProductButtonClicked(
                view: ContentTaggedProductBottomSheetItemView,
                product: ContentTaggedProductUiModel
            ) {
                listener.onAddToCartProductButtonClicked(product, bindingAdapterPosition)
            }

            override fun onBuyProductButtonClicked(
                view: ContentTaggedProductBottomSheetItemView,
                product: ContentTaggedProductUiModel
            ) {
                listener.onBuyProductButtonClicked(product, bindingAdapterPosition)
            }
        })
    }

    fun bind(item: ContentTaggedProductUiModel) {
        binding.root.bindData(item)
    }

    interface Listener {
        fun onProductCardClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        )

        fun onAddToCartProductButtonClicked(
            product: ContentTaggedProductUiModel,
            itemPosition: Int
        )

        fun onBuyProductButtonClicked(
            product: ContentTaggedProductUiModel,
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
