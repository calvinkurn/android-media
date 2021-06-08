package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.animation.LayoutTransition
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.analytic.tracker.BuyerOrderDetailTracker
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.common.utils.Utils
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductViewHolder(
        itemView: View?,
        private val listener: ProductViewListener,
        private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<ProductListUiModel.ProductUiModel>(itemView), View.OnClickListener {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_product_list_item
    }

    private val container = itemView?.findViewById<ConstraintLayout>(R.id.container)
    private val cardBuyerOrderDetailProduct = itemView?.findViewById<CardUnify>(R.id.cardBuyerOrderDetailProduct)
    private val btnBuyerOrderDetailBuyProductAgain = itemView?.findViewById<UnifyButton>(R.id.btnBuyerOrderDetailBuyProductAgain)
    private val ivBuyerOrderDetailProductThumbnail = itemView?.findViewById<ImageUnify>(R.id.ivBuyerOrderDetailProductThumbnail)
    private val tvBuyerOrderDetailProductName = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductName)
    private val tvBuyerOrderDetailProductPriceQuantity = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductPriceQuantity)
    private val tvBuyerOrderDetailProductNote = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductNote)
    private val tvBuyerOrderDetailProductPriceValue = itemView?.findViewById<Typography>(R.id.tvBuyerOrderDetailProductPriceValue)

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
        payloads.firstOrNull()?.let {
            if (it is Pair<*, *>) {
                val (oldItem, newItem) = it
                if (oldItem is ProductListUiModel.ProductUiModel && newItem is ProductListUiModel.ProductUiModel) {
                    container?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
                    this.element = newItem
                    if (oldItem.productThumbnailUrl != newItem.productThumbnailUrl) {
                        setupProductThumbnail(newItem.productThumbnailUrl)
                    }
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
                    if (oldItem.button != newItem.button || oldItem.isProcessing != newItem.isProcessing) {
                        setupButton(newItem.button, newItem.isProcessing)
                    }
                    container?.layoutTransition?.disableTransitionType(LayoutTransition.CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cardBuyerOrderDetailProduct -> goToProductSnapshotPage()
            R.id.btnBuyerOrderDetailBuyProductAgain -> onActionButtonClicked()
        }
    }

    private fun goToProductSnapshotPage() {
        element?.let {
            navigator.goToProductSnapshotPage(it.orderId, it.orderDetailId)
            BuyerOrderDetailTracker.eventClickProduct(it.orderStatusId, it.orderId)
        }
    }

    private fun onActionButtonClicked() {
        element?.let {
            when (it.button.key) {
                BuyerOrderDetailConst.ACTION_BUTTON_KEY_BUY_AGAIN -> addToCart()
                BuyerOrderDetailConst.ACTION_BUTTON_KEY_SEE_SIMILAR_PRODUCTS -> seeSimilarProducts()
            }
        }
    }

    private fun addToCart() {
        element?.let {
            listener.onBuyAgainButtonClicked(it)
        }
    }

    private fun seeSimilarProducts() {
        element?.let {
            navigator.openAppLink(it.button.url)
            BuyerOrderDetailTracker.eventClickSimilarProduct(it.orderStatusId, it.orderId)
        }
    }

    private fun setupClickListeners() {
        cardBuyerOrderDetailProduct?.setOnClickListener(this@ProductViewHolder)
        btnBuyerOrderDetailBuyProductAgain?.setOnClickListener(this@ProductViewHolder)
    }

    private fun setupProductThumbnail(productThumbnailUrl: String) {
        ivBuyerOrderDetailProductThumbnail?.apply {
            setImageUrl(productThumbnailUrl)
        }
    }

    private fun setupProductName(productName: String) {
        tvBuyerOrderDetailProductName?.text = productName
    }

    private fun setupProductQuantityAndPrice(quantity: Int, priceText: String) {
        tvBuyerOrderDetailProductPriceQuantity?.text = itemView.context.getString(R.string.label_product_price_and_quantity, quantity, priceText)
    }

    private fun setupProductNote(productNote: String) {
        tvBuyerOrderDetailProductNote?.apply {
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
        tvBuyerOrderDetailProductPriceValue?.text = totalPrice
    }

    private fun setupButton(showBuyAgainButton: ActionButtonsUiModel.ActionButton, processing: Boolean) {
        btnBuyerOrderDetailBuyProductAgain?.apply {
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