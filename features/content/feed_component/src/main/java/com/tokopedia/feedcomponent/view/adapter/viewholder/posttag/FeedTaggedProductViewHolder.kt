package com.tokopedia.feedcomponent.view.adapter.viewholder.posttag

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.feedcomponent.databinding.ItemFeedTaggedProductBinding
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew

/**
 *Created by shruti.agarwal on 23/02/23
 */
class FeedTaggedProductViewHolder(
    private val binding: ItemFeedTaggedProductBinding,
    private val listener: ProductItemInfoBottomSheet.Listener,
) : BaseViewHolder(binding.root) {

    init {
        binding.root.setListener(object : FeedTaggedProductBottomSheetCardView.Listener {
            override fun onClicked(
                view: FeedTaggedProductBottomSheetCardView,
                product: FeedXProduct
            ) {
//                listener.onProductClicked(
//                    this@FeedTaggedProductViewHolder,
//                    product
//                )
            }

            override fun onButtonTransactionProduct(
                view: FeedTaggedProductBottomSheetCardView,
                product: FeedXProduct,
            ) {
//                listener.onButtonTransactionProduct(
//                    this@FeedTaggedProductViewHolder,
//                    product
//                )
            }
        })
    }

    fun bind(item: ProductPostTagModelNew) {
        binding.root.setItem(item)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: ProductItemInfoBottomSheet.Listener,
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
            viewHolder: FeedTaggedProductViewHolder,
            product: FeedXProduct,
        )
        fun onButtonTransactionProduct(
            viewHolder: FeedTaggedProductViewHolder,
            product: FeedXProduct
        )
    }
}
