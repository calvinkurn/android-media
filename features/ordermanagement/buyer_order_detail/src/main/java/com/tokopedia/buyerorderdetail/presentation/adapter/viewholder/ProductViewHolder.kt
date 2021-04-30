package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.item_buyer_order_detail_product_list_item.view.*

class ProductViewHolder(
        itemView: View?,
        private val listener: ProductViewListener
) : AbstractViewHolder<ProductListUiModel.ProductUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_item
    }

    private var element: ProductListUiModel.ProductUiModel? = null

    init {
        setupClickListeners()
    }

    override fun bind(element: ProductListUiModel.ProductUiModel?) {
        element?.let {
            this.element = it
            setupClaimInsuranceButton(it.showClaimInsurance)
            setupProductThumbnail(it.productThumbnailUrl, it.showClaimInsurance)
            setupProductName(it.productName)
            setupProductQuantityAndPrice(it.quantity, it.priceText)
            setupProductNote(it.productNote)
            setupTotalPrice(it.totalPriceText)
            setupButton(it.button)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnProductProtection -> goToBuyProductProtection()
            R.id.cardBuyerOrderDetailProduct -> goToProductSnapshotPage()
            R.id.btnBuyerOrderDetailBuyProductAgain -> addToCart()
        }
    }

    private fun goToBuyProductProtection() {
        //TODO: Implement ATC after knowing the applink
    }

    private fun goToProductSnapshotPage() {
        element?.let {
            BuyerOrderDetailNavigator.goToProductSnapshotPage(itemView.context, it.productId, it.orderDetailId)
        }
    }

    private fun addToCart() {
        listener.onBuyAgainButtonClicked()
    }

    private fun setupClickListeners() {
        with(itemView) {
            btnProductProtection?.setOnClickListener(this@ProductViewHolder)
            cardBuyerOrderDetailProduct?.setOnClickListener(this@ProductViewHolder)
            btnBuyerOrderDetailBuyProductAgain?.setOnClickListener(this@ProductViewHolder)
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

    private fun setupProductQuantityAndPrice(quantity: Int, priceText: String) {
        itemView.tvBuyerOrderDetailProductPriceQuantity?.text = itemView.context.getString(R.string.label_product_price_and_quantity, quantity, priceText)
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

    private fun setupButton(showBuyAgainButton: ActionButtonsUiModel.ActionButton) {
        itemView.btnBuyerOrderDetailBuyProductAgain?.apply {
            text = showBuyAgainButton.label
            showWithCondition(showBuyAgainButton.label.isNotBlank())
        }
    }

    interface ProductViewListener {
        fun onBuyAgainButtonClicked()
    }
}