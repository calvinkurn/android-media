package com.tokopedia.oneclickcheckout.order.view.card

import android.graphics.Paint
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.Space
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class OrderProductCard(private val view: View, private val listener: OrderProductCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) : CoroutineScope {

    private lateinit var product: OrderProduct
    private lateinit var shop: OrderShop

    private val tvProductName by lazy { view.findViewById<Typography>(R.id.tv_product_name) }
    private val ivProductImage by lazy { view.findViewById<ImageUnify>(R.id.iv_product_image) }
    private val lblCashback by lazy { view.findViewById<Label>(R.id.lbl_cashback) }
    private val tfNote by lazy { view.findViewById<TextFieldUnify>(R.id.tf_note) }
    private val tvQuantityStockAvailable by lazy { view.findViewById<Typography>(R.id.tv_quantity_stock_available) }
    private val qtyEditorProduct by lazy { view.findViewById<QuantityEditorUnify>(R.id.qty_editor_product) }
    private val tvShopLocation by lazy { view.findViewById<Typography>(R.id.tv_shop_location) }
    private val tvShopName by lazy { view.findViewById<Typography>(R.id.tv_shop_name) }
    private val ivShop by lazy { view.findViewById<ImageUnify>(R.id.iv_shop) }
    private val tvProductPrice by lazy { view.findViewById<Typography>(R.id.tv_product_price) }
    private val tvProductSlashPrice by lazy { view.findViewById<Typography>(R.id.tv_product_slash_price) }
    private val ivFreeShipping by lazy { view.findViewById<ImageUnify>(R.id.iv_free_shipping) }
    private val labelError by lazy { view.findViewById<Label>(R.id.label_error) }
    private val dividerTop by lazy { view.findViewById<View>(R.id.divider_top) }
    private val cbPurchaseProtection by lazy { view.findViewById<CheckboxUnify>(R.id.cb_purchase_protection) }
    private val tvProtectionTitle by lazy { view.findViewById<Typography>(R.id.tv_protection_title) }
    private val tvProtectionDescription by lazy { view.findViewById<Typography>(R.id.tv_protection_description) }
    private val btnProtectionInfo by lazy { view.findViewById<UnifyImageButton>(R.id.btn_protection_info) }
    private val tvProtectionPrice by lazy { view.findViewById<Typography>(R.id.tv_protection_price) }
    private val tvProtectionUnit by lazy { view.findViewById<Typography>(R.id.tv_protection_unit) }
    private val spacePurchaseProtection by lazy { view.findViewById<Space>(R.id.space_purchase_protection) }
    private val groupPurchaseProtection by lazy { view.findViewById<Group>(R.id.group_purchase_protection) }

    private var quantityTextWatcher: TextWatcher? = null
    private var noteTextWatcher: TextWatcher? = null

    private var oldQtyValue: Int = 0

    private var resetQuantityJob: Job? = null

    fun setProduct(product: OrderProduct) {
        this.product = product
    }

    fun isProductInitialized(): Boolean {
        return ::product.isInitialized
    }

    fun initView() {
        if (isProductInitialized()) {
            ivProductImage?.setImageUrl(product.productImageUrl)
            tvProductName?.text = product.productName
            showPrice()

            if (product.cashback.isNotEmpty()) {
                lblCashback?.setLabel(product.cashback)
                lblCashback?.visible()
            } else {
                lblCashback?.gone()
            }

            tfNote?.textFieldInput?.textSize = 16f
            tfNote?.textFieldInput?.isSingleLine = false
            tfNote?.setCounter(MAX_NOTES_LENGTH)
            tfNote?.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            tfNote?.textFieldInput?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    orderSummaryAnalytics.eventClickSellerNotes(product.productId.toString(), shop.shopId.toString())
                }
            }
            if (noteTextWatcher != null) {
                tfNote?.textFieldInput?.removeTextChangedListener(noteTextWatcher)
            }
            tfNote?.textFieldInput?.setText(product.notes)
            noteTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    product.notes = s?.toString() ?: ""
                    listener.onProductChange(product, false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            }
            tfNote?.textFieldInput?.addTextChangedListener(noteTextWatcher)

            if (quantityTextWatcher != null) {
                // reset listener
                qtyEditorProduct?.editText?.removeTextChangedListener(quantityTextWatcher)
                qtyEditorProduct?.setValueChangedListener { _, _, _ -> }
            }
            qtyEditorProduct?.autoHideKeyboard = true
            qtyEditorProduct?.minValue = product.quantity.minOrderQuantity
            qtyEditorProduct?.maxValue = product.quantity.maxOrderStock
            oldQtyValue = product.quantity.orderQuantity
            qtyEditorProduct?.setValue(product.quantity.orderQuantity)
            qtyEditorProduct?.setValueChangedListener { newValue, _, _ ->
                // prevent multiple callback with same newValue
                if (product.quantity.orderQuantity != newValue) {
                    product.quantity.orderQuantity = newValue
                    listener.onProductChange(product)
                    showPrice()
                }
            }
            qtyEditorProduct?.setAddClickListener {
                orderSummaryAnalytics.eventEditQuantityIncrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
            }
            qtyEditorProduct?.setSubstractListener {
                orderSummaryAnalytics.eventEditQuantityDecrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
            }
            quantityTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    // for automatic reload rates when typing
                    val newValue = s.toString().replace("[^0-9]".toRegex(), "").toIntOrZero()
                    if (newValue > 0 && oldQtyValue != newValue) {
                        resetQuantityJob?.cancel()
                        oldQtyValue = newValue
                        qtyEditorProduct?.setValue(newValue)
                    } else if (newValue <= 0) {
                        // trigger reset quantity debounce to prevent empty quantity edit text
                        resetQuantityJob?.cancel()
                        resetQuantityJob = launch {
                            delay(DEBOUNCE_RESET_QUANTITY_MS)
                            if (isActive) {
                                qtyEditorProduct?.setValue(product.quantity.minOrderQuantity)
                            }
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            }
            qtyEditorProduct?.editText?.addTextChangedListener(quantityTextWatcher)

            renderProductTickerMessage()
            renderPurchaseProtection()
        }
    }

    private fun renderPurchaseProtection() {
        if (product.purchaseProtectionPlanData.isProtectionAvailable) {
            tvProtectionTitle.text = product.purchaseProtectionPlanData.protectionTitle
            tvProtectionDescription.text = product.purchaseProtectionPlanData.protectionSubtitle
            tvProtectionPrice.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.purchaseProtectionPlanData.protectionPrice, false).removeDecimalSuffix()
            btnProtectionInfo.setOnClickListener {
                val url = product.purchaseProtectionPlanData.protectionLinkUrl
                if (url.isNotBlank()) {
                    listener.onPurchaseProtectionInfoClicked(url)
                }
            }
            cbPurchaseProtection.isEnabled = !product.purchaseProtectionPlanData.isProtectionCheckboxDisabled
            cbPurchaseProtection.setOnCheckedChangeListener { buttonView, isChecked ->
                product.purchaseProtectionPlanData.stateChecked = isChecked
                listener.onProductChange(product, false)
                listener.onPurchaseProtectionCheckedChange()
            }
            cbPurchaseProtection.isChecked = product.purchaseProtectionPlanData.isProtectionOptIn
            tvProtectionUnit.text = product.purchaseProtectionPlanData.unit

            groupPurchaseProtection.show()
        } else {
            groupPurchaseProtection.gone()
        }
    }

    private fun renderProductTickerMessage() {
        if (product.tickerMessage.message.isNotEmpty()) {
            var completeText = product.tickerMessage.message
            for (replacement in product.tickerMessage.replacement) {
                completeText = completeText.replace("{{${replacement.identifier}}}", replacement.value)
            }
            tvQuantityStockAvailable?.text = MethodChecker.fromHtml(completeText)
        } else {
            tvQuantityStockAvailable?.text = ""
        }
    }

    private fun showPrice() {
        tvProductPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(product.getPrice(), false).removeDecimalSuffix()

        if (product.originalPrice.isNotBlank()) {
            tvProductSlashPrice.text = product.originalPrice
            tvProductSlashPrice.paintFlags = tvProductSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            tvProductSlashPrice.visible()
        } else {
            tvProductSlashPrice.gone()
        }

        if (product.isFreeOngkir && product.freeOngkirImg.isNotEmpty()) {
            ivFreeShipping?.let {
                ImageHandler.LoadImage(it, product.freeOngkirImg)
                it.visible()
            }
        } else {
            ivFreeShipping?.gone()
        }
    }

    fun setShop(orderShop: OrderShop) {
        tvShopName?.text = orderShop.shopName
        if (orderShop.shopBadge.isNotEmpty()) {
            ivShop?.setImageUrl(orderShop.shopBadge)
            ivShop?.visible()
        } else {
            ivShop?.gone()
        }
        tvShopLocation?.text = orderShop.cityName
        val error = orderShop.errors.firstOrNull()
        if (error?.isNotEmpty() == true) {
            labelError.setLabel(error)
            labelError.visible()
        } else {
            labelError.gone()
        }

        this.shop = orderShop
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct, shouldReloadRates: Boolean = true)

        fun onPurchaseProtectionInfoClicked(url: String)

        fun onPurchaseProtectionCheckedChange()
    }

    companion object {
        const val MAX_NOTES_LENGTH = 144

        private const val DEBOUNCE_RESET_QUANTITY_MS = 1000L
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    fun clearJob() {
        resetQuantityJob?.cancel()
    }
}