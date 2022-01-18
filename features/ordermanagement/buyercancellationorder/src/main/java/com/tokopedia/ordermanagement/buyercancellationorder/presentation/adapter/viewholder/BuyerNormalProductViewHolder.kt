package com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.ordermanagement.buyercancellationorder.R
import com.tokopedia.ordermanagement.buyercancellationorder.databinding.BottomsheetCancelProductItemBinding
import com.tokopedia.ordermanagement.buyercancellationorder.presentation.adapter.uimodel.BuyerNormalProductUiModel

class BuyerNormalProductViewHolder(itemView: View): AbstractViewHolder<BuyerNormalProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.bottomsheet_cancel_product_item
    }

    private val binding = BottomsheetCancelProductItemBinding.bind(itemView)

    override fun bind(element: BuyerNormalProductUiModel) {
        with(binding) {
            tvItemBuyerOrderBundlingProductName.text = element.productName
            tvItemBuyerOrderBundlingProductPrice.text = element.productPrice
            ivItemBuyerOrderBundlingThumbnail.setImageUrl(element.productThumbnailUrl)
        }
    }

}