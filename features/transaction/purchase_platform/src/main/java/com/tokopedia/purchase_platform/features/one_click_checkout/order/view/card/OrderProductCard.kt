package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.text.*
import android.view.View
import android.widget.EditText
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.features.one_click_checkout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderProduct
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.OrderShop
import kotlinx.android.synthetic.main.card_order_product.view.*

class OrderProductCard(private val view: View, private val listener: OrderProductCardListener, val orderSummaryAnalytics: OrderSummaryAnalytics) {

    private lateinit var product: OrderProduct
    private lateinit var shop: OrderShop

    private val etQty: EditText
    private var quantityTextWatcher: QuantityTextWatcher? = null
    private var noteTextWatcher: TextWatcher? = null

    init {
        etQty = view.et_qty
    }

    fun setProduct(product: OrderProduct) {
        this.product = product
    }

    fun initView() {
        if (::product.isInitialized) {
            ImageHandler.loadImageFitCenter(view.context, view.iv_product_image, product.productImageUrl)
            view.tv_product_name.text = product.productName
            showPrice()

            if (product.cashback.isNotEmpty()) {
                view.lbl_cashback.setLabel(product.cashback)
                view.lbl_cashback.visible()
            } else {
                view.lbl_cashback.gone()
            }

            view.et_note.filters = arrayOf(InputFilter.LengthFilter(MAX_NOTES_LENGTH))
            view.et_note.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    orderSummaryAnalytics.eventClickSellerNotes(product.productId.toString(), shop.shopId.toString())
                }
            }
            if (noteTextWatcher != null) {
                view.et_note.removeTextChangedListener(noteTextWatcher)
            }
            view.et_note.setText(product.notes)
            view.tv_note_char_counter.text = view.context.getString(R.string.note_counter_format, product.notes.length, 144)
            noteTextWatcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    product.notes = s?.toString() ?: ""
                    listener.onProductChange(product, false)
                    view.tv_note_char_counter.text = view.context.getString(R.string.note_counter_format, product.notes.length, 144)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            }
            view.et_note.addTextChangedListener(noteTextWatcher)

            if (quantityTextWatcher != null) {
                etQty.removeTextChangedListener(quantityTextWatcher)
            }
            etQty.setText("${product.quantity!!.orderQuantity}")
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
                        product.quantity!!.orderQuantity = 0
                        validateQuantity()
                        listener.onProductChange(product)
                        return@QuantityTextwatcherListener
                    } else if (quantity.editable[0] == '0') {
                        etQty.setText(quantity.editable.toString()
                                .substring(zeroCount, quantity.editable.toString().length))
                        etQty.setSelection(etQty.length())
                    }
                } else if (TextUtils.isEmpty(etQty.text)) {
                    product.quantity!!.orderQuantity = 0
                    validateQuantity()
                    listener.onProductChange(product)
                    return@QuantityTextwatcherListener
                }

                var qty = 0
                try {
                    qty = Integer.parseInt(quantity.editable.toString())
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                product.quantity!!.orderQuantity = qty
                validateQuantity()
                listener.onProductChange(product)
            })
            etQty.addTextChangedListener(quantityTextWatcher)
            view.btn_qty_plus.setOnClickListener {
                if (product.quantity!!.orderQuantity < product.quantity!!.maxOrderQuantity) {
                    product.quantity!!.orderQuantity++
                    etQty.setText("${product.quantity!!.orderQuantity}")
                }
                orderSummaryAnalytics.eventEditQuantityIncrease(product.productId.toString(), shop.shopId.toString(), product.quantity!!.orderQuantity.toString())
            }
            view.btn_qty_min.setOnClickListener {
                if (product.quantity!!.orderQuantity > product.quantity!!.minOrderQuantity) {
                    product.quantity!!.orderQuantity--
                    etQty.setText("${product.quantity!!.orderQuantity}")
                }
                orderSummaryAnalytics.eventEditQuantityDecrease(product.productId.toString(), shop.shopId.toString(), product.quantity!!.orderQuantity.toString())
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
            view.tv_quantity_stock_available.text = Html.fromHtml(invenageText)
        } else {
            view.tv_quantity_stock_available.text = ""
        }
    }

    private fun validateQuantity() {
        var error: String? = null
        val element = product.quantity
        if (element != null) {
            view.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_checkout_enabled)
            view.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_checkout_enabled)

            if (element.orderQuantity <= 0 || element.orderQuantity < element.minOrderQuantity) {
                error = String.format(view.context.getString(R.string.min_order_x), element.minOrderQuantity)
            } else if (element.orderQuantity > element.maxOrderQuantity) {
                error = String.format(view.context.getString(R.string.max_order_x), element.maxOrderQuantity)
            }
            if (element.orderQuantity <= element.minOrderQuantity) {
                view.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_checkout_disabled)
            }
            if (element.orderQuantity >= element.maxOrderQuantity) {
                view.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_checkout_disabled)
            }

            if (error != null) {
                element.isStateError = true
                view.tv_error_form_validation.text = error
                view.tv_error_form_validation.visibility = View.VISIBLE
            } else {
                element.isStateError = false
                view.tv_error_form_validation.visibility = View.GONE
                showPrice()
            }
        }
    }

    private fun showPrice() {
        var productPrice = product.productPrice.toLong()
        if (product.wholesalePrice.isNotEmpty()) {
            for (wholesalePrice in product.wholesalePrice) {
                if (product.quantity!!.orderQuantity >= wholesalePrice.qtyMin) {
                    productPrice = wholesalePrice.prdPrc.toLong()
                }
            }
        }
        view.tv_product_price.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(productPrice, false)

        if (product.isFreeOngkir && product.freeOngkirImg.isNotEmpty()) {
            view.iv_free_shipping.visible()
            ImageHandler.LoadImage(view.iv_free_shipping, product.freeOngkirImg)
        } else {
            view.iv_free_shipping.gone()
        }
    }

    fun setShop(orderShop: OrderShop) {
        view.tv_shop_name.text = orderShop.shopName
        view.tv_shop_location.text = orderShop.cityName
        val error = orderShop.errors.firstOrNull()
        if (error?.isNotEmpty() == true) {
            view.label_error.setLabel(error)
            view.label_error.visible()
        } else {
            view.label_error.gone()
        }

        this.shop = orderShop
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct, shouldReloadRates: Boolean = true)
    }

    companion object {
        const val MAX_NOTES_LENGTH = 144

        const val QUANTITY_PLACEHOLDER = "{{value}}"
    }
}