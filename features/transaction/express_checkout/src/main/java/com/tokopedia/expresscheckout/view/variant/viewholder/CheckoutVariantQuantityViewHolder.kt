package com.tokopedia.expresscheckout.view.variant.viewholder

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.expresscheckout.R
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantActionListener
import com.tokopedia.expresscheckout.view.variant.viewmodel.CheckoutVariantQuantityViewModel
import kotlinx.android.synthetic.main.item_quantity_detail_product_page.view.*

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

class CheckoutVariantQuantityViewHolder(val view: View, val listener: CheckoutVariantActionListener) : AbstractViewHolder<CheckoutVariantQuantityViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_quantity_detail_product_page
    }

    override fun bind(element: CheckoutVariantQuantityViewModel?) {
        if (element != null) {
            itemView.et_qty.setText(element.minOrderQuantity.toString())
            itemView.et_qty.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    try {
                        element.orderQuantity = s.toString().toInt()

                        if (validateQuantity(element) && adapterPosition != RecyclerView.NO_POSITION) {
                            listener.onNeedToNotifySingleItem(adapterPosition)
                        }
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            })

            itemView.tv_quantity_stock_available.text = element.availableStock
            if (validateQuantity(element) && adapterPosition != RecyclerView.NO_POSITION) {
                listener.onNeedToNotifySingleItem(adapterPosition)
            }

            if (element.orderQuantity > element.minOrderQuantity && element.orderQuantity > 0) {
                itemView.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_enabled)
                itemView.btn_qty_min.setOnClickListener {
                    element.orderQuantity -= 1
                    if (validateQuantity(element) && adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onNeedToNotifySingleItem(adapterPosition)
                    }
                }
            } else {
                itemView.btn_qty_min.setImageResource(R.drawable.bg_button_counter_minus_disabled)
                itemView.btn_qty_min.setOnClickListener { }
            }

            if (element.orderQuantity < element.maxOrderQuantity) {
                itemView.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_enabled)
                itemView.btn_qty_plus.setOnClickListener {
                    element.orderQuantity += 1
                    if (validateQuantity(element) && adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onNeedToNotifySingleItem(adapterPosition)
                    }
                }
            } else {
                itemView.btn_qty_plus.setImageResource(R.drawable.bg_button_counter_plus_disabled)
                itemView.btn_qty_plus.setOnClickListener { }
            }

        }
    }

    private fun validateQuantity(element: CheckoutVariantQuantityViewModel): Boolean {
        var error: String? = null
        var needToUpdateView: Boolean = false

        if (element.orderQuantity <= 0 || element.orderQuantity < element.minOrderQuantity) {
            error = element.errorProductMinQuantity.replace("{{value}}", "${element.minOrderQuantity}", false)
        } else if (element.orderQuantity > element.maxOrderQuantity) {
            error = element.errorProductMinQuantity.replace("{{value}}", "${element.maxOrderQuantity}", false)
        }

        if (error != null) {
            if (element.isStateError) {
                needToUpdateView = false
            }
            element.isStateError = true
            itemView.tv_error_form_validation.text = error
            itemView.tv_error_form_validation.visibility = View.VISIBLE
        } else {
            if (element.isStateError) {
                needToUpdateView = true
            }
            element.isStateError = false
            itemView.tv_error_form_validation.visibility = View.GONE
        }

        return needToUpdateView
    }

}