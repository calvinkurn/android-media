package com.tokopedia.content.common.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.databinding.ItemContentTaggedProductBinding
import com.tokopedia.content.common.view.ContentTaggedProductBottomSheetItemView
import com.tokopedia.content.common.view.ContentTaggedProductUiModel

/**
 *Created by shruti.agarwal on 23/02/23
 */
class ContentTaggedProductBottomSheetViewHolder(
    private val binding: ItemContentTaggedProductBinding,
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
        ) = ContentTaggedProductBottomSheetViewHolder(
            ItemContentTaggedProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener
        )
    }
}
