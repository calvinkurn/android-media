package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import kotlinx.android.synthetic.main.item_buyer_order_detail_product_list_item.view.*

class ProductViewHolder(
        itemView: View?,
        private val listener: ProductViewListener
) : AbstractViewHolder<ProductListUiModel.ProductUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_item

        const val PAYLOAD_IS_PROCESSING = "is_processing"
    }

    private var element: ProductListUiModel.ProductUiModel? = null

    init {
        setupClickListeners()
    }

    override fun bind(element: ProductListUiModel.ProductUiModel?) {
        element?.let {
            this.element = it
            setupProductThumbnail(it.productThumbnailUrl)
            setupProductName(it.productName)
            setupProductQuantityAndPrice(it.quantity, it.priceText)
            setupProductNote(it.productNote)
            setupTotalPrice(it.totalPriceText)
            setupButton(it.button, it.isProcessing)
        }
    }

    override fun bind(element: ProductListUiModel.ProductUiModel?, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            val payload = payloads.firstOrNull()
            if (payload is Bundle) {
                if (payload.containsKey(PAYLOAD_IS_PROCESSING)) {
                    itemView.btnBuyerOrderDetailBuyProductAgain?.isLoading = payload.getBoolean(PAYLOAD_IS_PROCESSING, false)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cardBuyerOrderDetailProduct -> goToProductSnapshotPage()
            R.id.btnBuyerOrderDetailBuyProductAgain -> addToCart()
        }
    }

    private fun goToProductSnapshotPage() {
        element?.let {
            BuyerOrderDetailNavigator.goToProductSnapshotPage(itemView.context, it.orderId, it.orderDetailId)
        }
    }

    private fun addToCart() {
        element?.let {
            listener.onBuyAgainButtonClicked(it)
        }
    }

    private fun setupClickListeners() {
        with(itemView) {
            cardBuyerOrderDetailProduct?.setOnClickListener(this@ProductViewHolder)
            btnBuyerOrderDetailBuyProductAgain?.setOnClickListener(this@ProductViewHolder)
        }
    }

    private fun setupProductThumbnail(productThumbnailUrl: String) {
        itemView.ivBuyerOrderDetailProductThumbnail?.apply {
            setImageUrl(productThumbnailUrl)
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

    private fun setupButton(showBuyAgainButton: ActionButtonsUiModel.ActionButton, processing: Boolean) {
        itemView.btnBuyerOrderDetailBuyProductAgain?.apply {
            isLoading = processing
            text = showBuyAgainButton.label
            buttonVariant = Utils.mapButtonVariant(showBuyAgainButton.variant)
            buttonType = Utils.mapButtonType(showBuyAgainButton.type)
            showWithCondition(showBuyAgainButton.label.isNotBlank())
        }
    }

    interface ProductViewListener {
        fun onBuyAgainButtonClicked(product: ProductListUiModel.ProductUiModel)
    }
}