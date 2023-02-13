package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.getColorHexString
import com.tokopedia.buyerorderdetail.databinding.ItemUnfulfilledPartialOrderBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ContainerUnify

class PofProductUnfulfilledViewHolder(
    view: View
) : CustomPayloadViewHolder<PofProductUnfulfilledUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_unfulfilled_partial_order
    }

    private val binding = ItemUnfulfilledPartialOrderBinding.bind(itemView)

    override fun bind(element: PofProductUnfulfilledUiModel) {
        with(binding) {
            setBackgroundContainer()
            setProductImageUrl(element.productPictureUrl)
            setProductName(element.productName)
            setProductQtyPrice(element.getProductPriceQty())
            setProductRequestStock(element)
        }
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is PofProductUnfulfilledUiModel && newItem is PofProductUnfulfilledUiModel) {
                if (oldItem.productPictureUrl != newItem.productPictureUrl) {
                    binding.setProductImageUrl(newItem.productPictureUrl)
                }
                if (oldItem.productName != newItem.productName) {
                    binding.setProductName(newItem.productName)
                }
                if (oldItem.getProductPriceQty() != newItem.getProductPriceQty()) {
                    binding.setProductQtyPrice(newItem.getProductPriceQty())
                }
                if ((oldItem.productQtyRequest != newItem.productQtyRequest) ||
                    (oldItem.productQtyCheckout != newItem.productQtyCheckout)
                ) {
                    binding.setProductRequestStock(newItem)
                }
            }
        }
    }

    private fun ItemUnfulfilledPartialOrderBinding.setProductImageUrl(imageUrl: String) {
        ivUnfulfilledProductThumbnail.loadImage(imageUrl)
    }

    private fun ItemUnfulfilledPartialOrderBinding.setProductName(productName: String) {
        tvUnfulfilledProductName.text = productName
    }

    private fun ItemUnfulfilledPartialOrderBinding.setProductQtyPrice(productQtyPrice: String) {
        tvUnfulfilledProductQtyPrice.text = productQtyPrice
    }

    private fun ItemUnfulfilledPartialOrderBinding.setProductRequestStock(item: PofProductUnfulfilledUiModel) {
        val colorHexStr =
            if (item.hasQtyGreenColor()) com.tokopedia.unifyprinciples.R.color.Unify_GN500 else com.tokopedia.unifyprinciples.R.color.Unify_RN500
        val productRequestStock = itemView.context.getString(
            com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_item_request_stock,
            getColorHexString(itemView.context, colorHexStr),
            item.productQtyRequest.toString(),
            item.productQtyCheckout.toString()
        )

        tvUnfulfilledProductRequestStock.text = MethodChecker.fromHtml(productRequestStock)
    }

    private fun ItemUnfulfilledPartialOrderBinding.setBackgroundContainer() {
        containerUnFulfilledPof.setContainerColor(ContainerUnify.RED)
    }
}
