package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.presentation.model.OwocProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography

class OwocPartialProductItemViewHolder(
    private val itemView: View?,
    partialProductItemViewStub: View?,
    private val navigator: BuyerOrderDetailNavigator,
    private var element: OwocProductListUiModel.ProductUiModel
) : View.OnClickListener {

    private val tvOwocProductName =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvOwocProductName)
    private val tvOwocProductPriceQuantity =
        partialProductItemViewStub?.findViewById<Typography>(R.id.tvOwocProductPriceQuantity)
    private val itemOwocProductViewStub =
        itemView?.findViewById<View>(R.id.itemOwocProductViewStub)

    private val context = itemView?.context

    init {
        setupClickListeners()
        setupProductName(element.productName)
        setupProductQuantityAndPrice(element.quantity, element.priceText)
    }

    fun bindProductItemPayload(
        oldItem: OwocProductListUiModel.ProductUiModel,
        newItem: OwocProductListUiModel.ProductUiModel
    ) {
        this.element = newItem

        if (oldItem.productName != newItem.productName) {
            setupProductName(newItem.productName)
        }
        if (oldItem.quantity != newItem.quantity || oldItem.priceText != newItem.priceText) {
            setupProductQuantityAndPrice(newItem.quantity, newItem.priceText)
        }
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
        itemOwocProductViewStub?.setOnClickListener(this)
    }

    private fun setupProductName(productName: String) {
        tvOwocProductName?.text = productName
    }

    private fun setupProductQuantityAndPrice(quantity: Int, priceText: String) {
        tvOwocProductPriceQuantity?.text =
            context?.getString(R.string.label_product_price_and_quantity, quantity, priceText)
                .orEmpty()
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.itemBomDetailProductViewStub -> goToProductSnapshotPage()
        }
    }
}

