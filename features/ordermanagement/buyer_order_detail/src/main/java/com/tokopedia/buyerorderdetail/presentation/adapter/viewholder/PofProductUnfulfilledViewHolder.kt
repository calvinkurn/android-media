package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.PofUtils.getColorHexString
import com.tokopedia.buyerorderdetail.databinding.ItemUnfulfilledPartialOrderBinding
import com.tokopedia.buyerorderdetail.presentation.model.PofProductUnfulfilledUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ContainerUnify

class PofProductUnfulfilledViewHolder(
    view: View
) : AbstractViewHolder<PofProductUnfulfilledUiModel>(view) {

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

        val productRequestStockStr = itemView.context.getString(
            com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_item_request_stock,
            getColorHexString(itemView.context, colorHexStr),
            item.productQtyRequest.toString()
        )

        tvUnfulfilledProductRequestStock.text = MethodChecker.fromHtml(productRequestStockStr)
        tvUnfulfilledProductCheckoutStock.text = itemView.context.getString(
            com.tokopedia.buyerorderdetail.R.string.buyer_order_detail_pof_item_checkout_stock,
            item.productQtyCheckout.toString()
        )
    }

    private fun ItemUnfulfilledPartialOrderBinding.setBackgroundContainer() {
        containerUnFulfilledPof.setContainerColor(ContainerUnify.RED)
    }
}
