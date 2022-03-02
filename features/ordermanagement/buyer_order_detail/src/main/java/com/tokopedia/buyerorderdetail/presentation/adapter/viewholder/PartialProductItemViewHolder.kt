package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailActionButtonKey
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class PartialProductItemViewHolder(
    private val itemView: View?,
    partialProductItemViewStub: View?,
    private val listener: ProductViewListener,
    private val navigator: BuyerOrderDetailNavigator,
    private var element: ProductListUiModel.ProductUiModel
) : View.OnClickListener {

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)

    private val tvBuyerOrderDetailProductName =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductName)
    private val tvBuyerOrderDetailProductPriceQuantity =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductPriceQuantity)
    private val tvBuyerOrderDetailProductNote =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductNote)
    private val tvBuyerOrderDetailProductPriceValue =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductPriceValue)
    private val itemBomDetailProductViewStub =
        partialProductItemViewStub?.findViewById<View>(R.id.itemBomDetailProductViewStub)

    private val context = itemView?.context

    init {
        setupClickListeners()
        setupProductName(element.productName)
        setupProductQuantityAndPrice(element.quantity, element.priceText)
        setupProductNote(element.productNote)
        setupTotalPrice(element.totalPriceText)
    }

    fun bindProductItemPayload(
        oldItem: ProductListUiModel.ProductUiModel,
        newItem: ProductListUiModel.ProductUiModel
    ) {
        container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
        this.element = newItem

        if (oldItem.productName != newItem.productName) {
            setupProductName(newItem.productName)
        }
        if (oldItem.quantity != newItem.quantity || oldItem.priceText != newItem.priceText) {
            setupProductQuantityAndPrice(newItem.quantity, newItem.priceText)
        }
        if (oldItem.productNote != newItem.productNote) {
            setupProductNote(newItem.productNote)
        }
        if (oldItem.totalPriceText != newItem.totalPriceText) {
            setupTotalPrice(newItem.totalPriceText)
        }
        container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
    }

    private fun goToProductSnapshotPage() {
        element.let {
            if (it.orderId != BuyerOrderDetailMiscConstant.WAITING_INVOICE_ORDER_ID) {
                navigator.goToProductSnapshotPage(it.orderId, it.orderDetailId)
                BuyerOrderDetailTracker.eventClickProduct(it.orderStatusId, it.orderId)
            } else {
                showToaster(
                    context?.getString(R.string.buyer_order_detail_error_message_cant_open_snapshot_when_waiting_invoice)
                        .orEmpty()
                )
            }
        }
    }

    private fun setupClickListeners() {
        itemBomDetailProductViewStub?.setOnClickListener(this)
    }

    private fun setupProductName(productName: String) {
        tvBuyerOrderDetailProductName?.text = productName
    }

    private fun setupProductQuantityAndPrice(quantity: Int, priceText: String) {
        tvBuyerOrderDetailProductPriceQuantity?.text =
            context?.getString(R.string.label_product_price_and_quantity, quantity, priceText)
                .orEmpty()
    }

    private fun setupProductNote(productNote: String) {
        tvBuyerOrderDetailProductNote?.apply {
            text = Utils.composeItalicNote(productNote)
            showWithCondition(productNote.isNotBlank())
        }
    }

    private fun setupTotalPrice(totalPrice: String) {
        tvBuyerOrderDetailProductPriceValue?.text = totalPrice
    }

    private fun showToaster(message: String) {
        itemView?.parent?.parent?.parent?.let {
            if (it is View) {
                if (message.isNotBlank()) {
                    Toaster.build(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
                }
            }
        }
    }

    private fun onActionButtonClicked() {
        when (element.button.key) {
            BuyerOrderDetailActionButtonKey.BUY_AGAIN -> addToCart()
            BuyerOrderDetailActionButtonKey.SEE_SIMILAR_PRODUCTS -> seeSimilarProducts()
        }
    }

    private fun addToCart() {
        listener.onBuyAgainButtonClicked(element)
    }

    private fun seeSimilarProducts() {
        navigator.openAppLink(element.button.url, false)
        BuyerOrderDetailTracker.eventClickSimilarProduct(
            element.orderStatusId,
            element.orderId
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.itemBomDetailProductViewStub -> goToProductSnapshotPage()
            R.id.btnBuyerOrderDetailBuyProductAgain -> onActionButtonClicked()
        }
    }

    interface ProductViewListener {
        fun onBuyAgainButtonClicked(product: ProductListUiModel.ProductUiModel)
    }
}