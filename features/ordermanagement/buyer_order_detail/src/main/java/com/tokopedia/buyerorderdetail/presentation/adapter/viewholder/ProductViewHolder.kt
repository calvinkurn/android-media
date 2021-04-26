package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.item_buyer_order_detail_product_list_item.view.*

class ProductViewHolder(itemView: View?) : AbstractViewHolder<ProductListUiModel.ProductUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_item
    }

    override fun bind(element: ProductListUiModel.ProductUiModel?) {
        element?.let {
            setupClaimInsuranceButton(it.showClaimInsurance)
            setupProductThumbnail(it.productThumbnailUrl, it.showClaimInsurance)
            setupProductName(it.productName)
            setupProductQuantityAndPrice(it.productQuantityAndPrice)
            setupProductNote(it.productNote)
            setupTotalPrice(it.totalPrice)
            setupBuyAgainButton(it.showBuyAgainButton)
        }
    }

    private fun setupClaimInsuranceButton(showClaimInsurance: Boolean) {
        itemView.btnProductProtection?.showWithCondition(showClaimInsurance)
    }

    private fun setupProductThumbnail(productThumbnailUrl: String, showClaimInsurance: Boolean) {
        itemView.ivBuyerOrderDetailProductThumbnail?.apply {
            setImageUrl(productThumbnailUrl)
            val marginTop = if (showClaimInsurance) getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) else 0
            setMargin(0, marginTop, 0, 0)
        }
    }

    private fun setupProductName(productName: String) {
        itemView.tvBuyerOrderDetailProductName?.text = productName
    }

    private fun setupProductQuantityAndPrice(productQuantityAndPrice: String) {
        itemView.tvBuyerOrderDetailProductPriceQuantity?.text = productQuantityAndPrice
    }

    private fun setupProductNote(productNote: String) {
        itemView.tvBuyerOrderDetailProductNote?.apply {
            text = composeItalicNote(productNote)
            showWithCondition(productNote.isNotBlank())
        }
    }

    private fun composeItalicNote(productNote: String): SpannableString {
        return SpannableString(productNote).apply {
            setSpan(StyleSpan(android.graphics.Typeface.ITALIC), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun setupTotalPrice(totalPrice: String) {
        itemView.tvBuyerOrderDetailProductPriceValue?.text = totalPrice
    }

    private fun setupBuyAgainButton(showBuyAgainButton: Boolean) {
        itemView.btnBuyerOrderDetailBuyProductAgain?.showWithCondition(showBuyAgainButton)
    }
}