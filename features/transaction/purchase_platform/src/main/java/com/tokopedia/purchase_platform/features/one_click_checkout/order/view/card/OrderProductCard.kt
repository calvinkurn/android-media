package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.text.*
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.image.RoundedCornerImageView
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderShop
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class OrderProductCard(private val view: View, private val listener: OrderProductCardListener, private val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var product: OrderProduct
    private lateinit var shop: OrderShop

    private val etQty by lazy { view.findViewById<EditText>(R.id.et_qty) }
    private val tvProductName by lazy { view.findViewById<Typography>(R.id.tv_product_name) }
    private val ivProductImage by lazy { view.findViewById<RoundedCornerImageView>(R.id.iv_product_image) }
    private val lblCashback by lazy { view.findViewById<Label>(R.id.lbl_cashback) }
    private val etNote by lazy { view.findViewById<EditText>(R.id.et_note) }
    private val tvNoteCharCounter by lazy { view.findViewById<Typography>(R.id.tv_note_char_counter) }
    private val btnQtyPlus by lazy { view.findViewById<ImageView>(R.id.btn_qty_plus) }
    private val btnQtyMin by lazy { view.findViewById<ImageView>(R.id.btn_qty_min) }
    private val tvQuantityStockAvailable by lazy { view.findViewById<Typography>(R.id.tv_quantity_stock_available) }
    private val tvErrorFormValidation by lazy { view.findViewById<Typography>(R.id.tv_error_form_validation) }
    private val tvShopLocation by lazy { view.findViewById<Typography>(R.id.tv_shop_location) }
    private val tvShopName by lazy { view.findViewById<Typography>(R.id.tv_shop_name) }
    private val tvProductPrice by lazy { view.findViewById<Typography>(R.id.tv_product_price) }
    private val ivFreeShipping by lazy { view.findViewById<ImageView>(R.id.iv_free_shipping) }
    private val labelError by lazy { view.findViewById<Label>(R.id.label_error) }

    private var quantityTextWatcher: QuantityTextWatcher? = null
    private var noteTextWatcher: TextWatcher? = null

    fun setProduct(product: OrderProduct) {
        this.product = product
    }

    fun initView() {
        if (::product.isInitialized) {
            ivProductImage?.let {
                ImageHandler.loadImageFitCenter(view.context, it, product.productImageUrl)
            }
            tvProductName?.text = product.productName
            showPrice()

            if (product.cashback.isNotEmpty()) {
                lblCashback?.setLabel(product.cashback)
                lblCashback?.visible()
            } else {
                lblCashback?.gone()
            }

            etNote?.filters = arrayOf(InputFilter.LengthFilter(MAX_NOTES_LENGTH))
            etNote?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    orderSummaryAnalytics.eventClickSellerNotes(product.productId.toString(), shop.shopId.toString())
                }
            }
            if (noteTextWatcher != null) {
                etNote?.removeTextChangedListener(noteTextWatcher)
            }
            etNote?.setText(product.notes)
            tvNoteCharCounter?.text = view.context.getString(R.string.note_counter_format, product.notes.length, 144)
            noteTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    product.notes = s?.toString() ?: ""
                    listener.onProductChange(product, false)
                    tvNoteCharCounter?.text = view.context.getString(R.string.note_counter_format, product.notes.length, 144)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            }
            etNote?.addTextChangedListener(noteTextWatcher)

            if (quantityTextWatcher != null) {
                etQty?.removeTextChangedListener(quantityTextWatcher)
            }
            etQty?.setText("${product.quantity.orderQuantity}")
            quantityTextWatcher = QuantityTextWatcher(QuantityTextWatcher.QuantityTextwatcherListener { quantity ->
                if (quantity.editable.isNotEmpty()) {
                    var zeroCount = 0
                    for (element in quantity.editable) {
                        if (element == '0') {
                            zeroCount++
                        } else {
                            break
                        }
                    }
                    if (zeroCount == quantity.editable.length) {
                        product.quantity.orderQuantity = 0
                        validateQuantity()
                        listener.onProductChange(product)
                        return@QuantityTextwatcherListener
                    } else if (quantity.editable[0] == '0') {
                        etQty?.setText(quantity.editable.toString()
                                .substring(zeroCount, quantity.editable.toString().length))
                        etQty?.setSelection(etQty?.length() ?: 0)
                    }
                } else if (TextUtils.isEmpty(etQty?.text)) {
                    product.quantity.orderQuantity = 0
                    validateQuantity()
                    listener.onProductChange(product)
                    return@QuantityTextwatcherListener
                }

                var qty = 0
                try {
                    qty = Integer.parseInt(quantity.editable.toString())
                } catch (e: NumberFormatException) { }

                product.quantity.orderQuantity = qty
                validateQuantity()
                listener.onProductChange(product)
            })
            etQty?.addTextChangedListener(quantityTextWatcher)
            btnQtyPlus?.setOnClickListener {
                if (product.quantity.orderQuantity < product.quantity.maxOrderQuantity) {
                    product.quantity.orderQuantity++
                    etQty?.setText("${product.quantity.orderQuantity}")
                }
                orderSummaryAnalytics.eventEditQuantityIncrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
            }
            btnQtyMin?.setOnClickListener {
                if (product.quantity.orderQuantity > product.quantity.minOrderQuantity) {
                    product.quantity.orderQuantity--
                    etQty?.setText("${product.quantity.orderQuantity}")
                }
                orderSummaryAnalytics.eventEditQuantityDecrease(product.productId.toString(), shop.shopId.toString(), product.quantity.orderQuantity.toString())
            }

            validateQuantity()
            renderProductPropertiesInvenage()
        }
    }

    private fun renderProductPropertiesInvenage() {
        if (product.productResponse.productInvenageTotal.byUserText.complete.isNotEmpty()) {
            val completeText = product.productResponse.productInvenageTotal.byUserText.complete
            val totalInOtherCart = product.productResponse.productInvenageTotal.byUser.inCart
            val totalRemainingStock = product.productResponse.productInvenageTotal.byUser.lastStockLessThan
            val invenageText = completeText.replace(view.context?.getString(R.string.product_invenage_remaining_stock)
                    ?: "", "" + totalRemainingStock)
                    .replace(view.context?.getString(R.string.product_invenage_in_other_cart)
                            ?: "", "" + totalInOtherCart)
            tvQuantityStockAvailable?.text = Html.fromHtml(invenageText)
        } else {
            tvQuantityStockAvailable?.text = ""
        }
    }

    private fun validateQuantity() {
        var error: String? = null
        val element = product.quantity
        btnQtyMin?.setImageResource(R.drawable.bg_button_counter_minus_checkout_enabled)
        btnQtyPlus?.setImageResource(R.drawable.bg_button_counter_plus_checkout_enabled)

        if (element.orderQuantity <= 0 || element.orderQuantity < element.minOrderQuantity) {
            error = String.format(view.context.getString(R.string.min_order_x), element.minOrderQuantity)
        } else if (element.orderQuantity > element.maxOrderQuantity) {
            error = String.format(view.context.getString(R.string.max_order_x), element.maxOrderQuantity)
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_MAX_QTY)
        }
        if (element.orderQuantity <= element.minOrderQuantity) {
            btnQtyMin?.setImageResource(R.drawable.bg_button_counter_minus_checkout_disabled)
        }
        if (element.orderQuantity >= element.maxOrderQuantity) {
            btnQtyPlus?.setImageResource(R.drawable.bg_button_counter_plus_checkout_disabled)
        }

        if (error != null) {
            element.isStateError = true
            tvErrorFormValidation?.text = error
            tvErrorFormValidation?.visibility = View.VISIBLE
        } else {
            element.isStateError = false
            tvErrorFormValidation?.visibility = View.GONE
            showPrice()
        }
    }

    private fun showPrice() {
        var productPrice = product.productPrice
        if (product.wholesalePrice.isNotEmpty()) {
            for (wholesalePrice in product.wholesalePrice) {
                if (product.quantity.orderQuantity >= wholesalePrice.qtyMin) {
                    productPrice = wholesalePrice.prdPrc.toLong()
                }
            }
        }
        tvProductPrice?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(productPrice, false)

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