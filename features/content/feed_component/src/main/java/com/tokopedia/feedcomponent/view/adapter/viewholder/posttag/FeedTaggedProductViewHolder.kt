package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.databinding.ItemFeedTaggedProductBinding
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew

/**
 *Created by shruti.agarwal on 23/02/23
 */
class FeedTaggedProductViewHolder(
    private val binding: ItemFeedTaggedProductBinding,
    private val listener: Listener?,
) : BaseViewHolder(binding.root) {

    init {
        binding.root.setListener(object : FeedTaggedProductBottomSheetCardView.Listener {
            override fun onClicked(
                view: FeedTaggedProductBottomSheetCardView,
                product: ProductPostTagModelNew
            ) {
                listener?.onProductClicked(
                    view,
                    product
                )
            }

            override fun onButtonAddToCartProduct(
                view: FeedTaggedProductBottomSheetCardView,
                product: ProductPostTagModelNew
            ) {
                listener?.onButtonAddToCartProduct(view, product)
            }

            override fun onButtonMoveToCartProduct(
                view: FeedTaggedProductBottomSheetCardView,
                product: ProductPostTagModelNew
            ) {
                listener?.onButtonMoveToCartProduct(view, product)
            }

        })
    }

    fun bind(item: ProductPostTagModelNew) {
        binding.root.setItem(item)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?,
        ) = FeedTaggedProductViewHolder(
            ItemFeedTaggedProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener
        )
    }

    interface Listener {
        fun onProductClicked(
            viewHolder: FeedTaggedProductBottomSheetCardView,
            product: ProductPostTagModelNew,
        )
        fun onButtonMoveToCartProduct(
            viewHolder: FeedTaggedProductBottomSheetCardView,
            product: ProductPostTagModelNew
        )
        fun onButtonAddToCartProduct(
            viewHolder: FeedTaggedProductBottomSheetCardView,
            product: ProductPostTagModelNew
        )
    }
}
