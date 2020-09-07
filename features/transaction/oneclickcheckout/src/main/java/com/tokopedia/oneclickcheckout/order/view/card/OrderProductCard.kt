package com.tokopedia.oneclickcheckout.order.view.card

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.QuantityEditorUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderProductCard(private val view: View, private val listener: OrderProductCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var product: OrderProduct
    private lateinit var shop: OrderShop

//    private val etQty by lazy { view.findViewById<EditText>(R.id.et_qty) }
    private val tvProductName by lazy { view.findViewById<Typography>(R.id.tv_product_name) }
    private val ivProductImage by lazy { view.findViewById<ImageUnify>(R.id.iv_product_image) }
    private val lblCashback by lazy { view.findViewById<Label>(R.id.lbl_cashback) }
    private val tfNote by lazy { view.findViewById<TextFieldUnify>(R.id.tf_note) }
//    private val btnQtyPlus by lazy { view.findViewById<ImageView>(R.id.btn_qty_plus) }
//    private val btnQtyMin by lazy { view.findViewById<ImageView>(R.id.btn_qty_min) }
    private val tvQuantityStockAvailable by lazy { view.findViewById<Typography>(R.id.tv_quantity_stock_available) }
//    private val tvErrorFormValidation by lazy { view.findViewById<Typography>(R.id.tv_error_form_validation) }
    private val qtyEditorProduct by lazy { view.findViewById<QuantityEditorUnify>(R.id.qty_editor_product) }
    private val tvShopLocation by lazy { view.findViewById<Typography>(R.id.tv_shop_location) }
    private val tvShopName by lazy { view.findViewById<Typography>(R.id.tv_shop_name) }
    private val tvProductPrice by lazy { view.findViewById<Typography>(R.id.tv_product_price) }
    private val tvProductSlashPrice by lazy { view.findViewById<Typography>(R.id.tv_product_slash_price) }
    private val ivFreeShipping by lazy { view.findViewById<ImageUnify>(R.id.iv_free_shipping) }
    private val labelError by lazy { view.findViewById<Label>(R.id.label_error) }

    private var quantityTextWatcher: TextWatcher? = null
    private var noteTextWatcher: TextWatcher? = null

    private var oldQtyValue: Int = 0

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
                    if (oldQtyValue != newValue) {
                        oldQtyValue = newValue
                        qtyEditorProduct?.setValue(newValue)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            }
            qtyEditorProduct?.editText?.addTextChangedListener(quantityTextWatcher)

//            if (quantityTextWatcher != null) {
//                etQty?.removeTextChangedListener(quantityTextWatcher)
//            }
//            etQty?.setText("${product.quantity.orderQuantity}")
//            quantityTextWatcher = QuantityTextWatcher(QuantityTextWatcher.QuantityTextwatcherListener { quantity ->
//                if (quantity.editable.isNotEmpty()) {
//                    var zeroCount = 0
//                    for (element in quantity.editable) {
//                        if (element == '0') {
//                            zeroCount++
//                        } else {
//                            break
//                        }
//                    }
//                    if (zeroCount == quantity.editable.length) {
//                        product.quantity.orderQuantity = 0
//                        validateQuantity()
//                        listener.onProductChange(product)
//                        return@QuantityTextwatcherListener
//                    } else if (quantity.editable[0] == '0') {
//                        etQty?.setText(quantity.editable.toString()
//                                .substring(zeroCount, quantity.editable.toString().length))
//                        etQty?.setSelection(etQty?.length() ?: 0)
//                    }
//                } else if (TextUtils.isEmpty(etQty?.text)) {
//                    product.quantity.orderQuantity = 0
//                    validateQuantity()
//                    listener.onProductChange(product)
//                    return@QuantityTextwatcherListener
//                }
//
//                var qty = 0
//                try {
//                    qty = Integer.parseInt(quantity.editable.toString())
//                } catch (e: NumberFormatException) {
//                }
//
//                product.quantity.orderQuantity = qty
//                validateQuantity()
//                listener.onProductChange(product)
//            })
//            etQty?.addTextChangedListener(quantityTextWatcher)
//            btnQtyPlus?.setOnClickListener {
//                if (product.quantity.orderQuantity < product.quantity.maxOrderQuantity && product.quantity.orderQuantity < product.quantity.maxOrderStock) {
//                    product.quantity.orderQuantity++
//                    etQty?.setText("${product.quantity.orderQuantity}")
//                }
//                orderSummaryAnalytics.eventEditQuantityIncrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
//            }
//            btnQtyMin?.setOnClickListener {
//                if (product.quantity.orderQuantity > product.quantity.minOrderQuantity) {
//                    product.quantity.orderQuantity--
//                    etQty?.setText("${product.quantity.orderQuantity}")
//                }
//                orderSummaryAnalytics.eventEditQuantityDecrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
//            }

//            validateQuantity()
            renderProductTickerMessage()
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

//    private fun validateQuantity() {
//        var error: String? = null
//        val element = product.quantity
//        btnQtyMin?.setImageResource(com.tokopedia.purchase_platform.common.R.drawable.bg_button_counter_minus_checkout_enabled)
//        btnQtyPlus?.setImageResource(com.tokopedia.purchase_platform.common.R.drawable.bg_button_counter_plus_checkout_enabled)
//
//        if (element.orderQuantity <= 0 || element.orderQuantity < element.minOrderQuantity) {
//            error = element.errorProductMinQuantity.replace(QuantityUiModel.VALUE_REPLACE_STRING, element.minOrderQuantity.toString())
//        } else if (element.orderQuantity > element.maxOrderQuantity) {
//            error = element.errorProductMaxQuantity.replace(QuantityUiModel.VALUE_REPLACE_STRING, element.maxOrderQuantity.toString())
//            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_MAX_QTY)
//        } else if (element.orderQuantity > element.maxOrderStock) {
//            error = element.errorProductAvailableStock.replace(QuantityUiModel.VALUE_REPLACE_STRING, element.maxOrderStock.toString())
//            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_MAX_QTY)
//        }
//        if (element.orderQuantity <= element.minOrderQuantity) {
//            btnQtyMin?.setImageResource(com.tokopedia.purchase_platform.common.R.drawable.bg_button_counter_minus_checkout_disabled)
//        }
//        if (element.orderQuantity >= element.maxOrderQuantity || element.orderQuantity >= element.maxOrderStock) {
//            btnQtyPlus?.setImageResource(com.tokopedia.purchase_platform.common.R.drawable.bg_button_counter_plus_checkout_disabled)
//        }
//
//        if (error != null) {
//            element.isStateError = true
//            tvErrorFormValidation?.text = error
//            tvErrorFormValidation?.visibility = View.VISIBLE
//        } else {
//            element.isStateError = false
//            tvErrorFormValidation?.visibility = View.GONE
//            showPrice()
//        }
//    }

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
    }

    companion object {
        const val MAX_NOTES_LENGTH = 144
    }
}