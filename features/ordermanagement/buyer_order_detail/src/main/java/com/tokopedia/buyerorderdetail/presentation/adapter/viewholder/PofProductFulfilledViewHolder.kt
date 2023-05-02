package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.databinding.ItemFulfilledPartialOrderBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofProductFulfilledUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage

class PofProductFulfilledViewHolder(view: View) :
    CustomPayloadViewHolder<PofProductFulfilledUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_fulfilled_partial_order
    }

    private val binding = ItemFulfilledPartialOrderBinding.bind(itemView)

    override fun bind(element: PofProductFulfilledUiModel) {
        with(binding) {
            setImageProductUrl(element.productPictureUrl)
            setProductName(element.productName)
            setProductQtyPrice(element.getProductPriceQty())
            setFulfilledDivider(element.isShowDivider)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofProductFulfilledUiModel && newItem is PofProductFulfilledUiModel) {
                if (oldItem.productPictureUrl != newItem.productPictureUrl) {
                    binding.setImageProductUrl(newItem.productPictureUrl)
                }
                if (oldItem.productName != newItem.productName) {
                    binding.setProductName(newItem.productName)
                }
                if (oldItem.getProductPriceQty() != newItem.getProductPriceQty()) {
                    binding.setProductQtyPrice(newItem.getProductPriceQty())
                }
                if (oldItem.isShowDivider != newItem.isShowDivider) {
                    binding.setFulfilledDivider(newItem.isShowDivider)
                }
            }
        }
    }

    private fun ItemFulfilledPartialOrderBinding.setImageProductUrl(imageUrl: String) {
        ivUnfulfilledProductThumbnail.loadImage(imageUrl)
    }

    private fun ItemFulfilledPartialOrderBinding.setProductName(productName: String) {
        tvUnfulfilledProductName.text = productName
    }

    private fun ItemFulfilledPartialOrderBinding.setProductQtyPrice(productQtyPrice: String) {
        tvUnfulfilledProductPriceQuantity.text = productQtyPrice
    }

    private fun ItemFulfilledPartialOrderBinding.setFulfilledDivider(isShowDivider: Boolean) {
        dividerFulfilledProduct.showWithCondition(isShowDivider)
    }
}

