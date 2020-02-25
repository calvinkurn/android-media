package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.text.InputFilter
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.tokopedia.purchase_platform.common.utils.QuantityTextWatcher
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderProduct
import kotlinx.android.synthetic.main.card_order_product.view.*

class OrderProductCard(private val view: View, private val listener: OrderProductCardListener) {

    private lateinit var product: OrderProduct

    private val etQty: EditText

    init {
        etQty = view.et_qty
    }

    fun setProduct(product: OrderProduct) {
        this.product = product
    }

    fun initView() {
        if (::product.isInitialized) {
            view.tv_shop.text = "WOW QREN SKLI 2"
            val min = 1
            val max = 10
            view.et_note.filters = arrayOf(InputFilter.LengthFilter(max))
            view.btn_qty_plus.setOnClickListener {
                if (product.qty < max) {
                    product.qty++
                    listener.onProductChange(product)
                }
            }
            view.btn_qty_min.setOnClickListener {
                if (product.qty > min) {
                    product.qty--
                    listener.onProductChange(product)
                }
            }
            etQty.addTextChangedListener(QuantityTextWatcher(QuantityTextWatcher.QuantityTextwatcherListener { quantity ->
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
                        product.qty = 0
                        listener.onProductChange(product)
                        return@QuantityTextwatcherListener
                    } else if (quantity.editable[0] == '0') {
                        etQty.setText(quantity.editable.toString()
                                .substring(zeroCount, quantity.editable.toString().length))
                        etQty.setSelection(etQty.length())
                    }
                } else if (TextUtils.isEmpty(etQty.text)) {
                    etQty.setText("0")
                    etQty.setSelection(etQty.length())
                    return@QuantityTextwatcherListener
                }

                var qty = 0
                try {
                    qty = Integer.parseInt(quantity.editable.toString())
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

                product.qty = qty
                listener.onProductChange(product)
            }))
        }
    }

    interface OrderProductCardListener {

        fun onProductChange(product: OrderProduct)
    }
}