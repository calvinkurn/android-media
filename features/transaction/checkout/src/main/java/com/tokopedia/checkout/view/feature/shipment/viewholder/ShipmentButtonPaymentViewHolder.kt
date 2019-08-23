package com.tokopedia.checkout.view.feature.shipment.viewholder

import android.os.Build
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.shipment.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentButtonPaymentModel
import kotlinx.android.synthetic.main.item_shipment_button_payment.view.*

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

class ShipmentButtonPaymentViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val ITEM_VIEW_PAYMENT_BUTTON = R.layout.item_shipment_button_payment
    }

    fun bindViewHolder(model: ShipmentButtonPaymentModel) {
        if (model.isCod) {
            itemView.tv_select_cod.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.tv_total_payment.setTextAppearance(R.style.button_payment)
            } else {
                itemView.tv_total_payment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.0f)
                itemView.tv_total_payment.height = itemView.resources.getDimensionPixelOffset(R.dimen.dp_40)
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(itemView.resources.getDimensionPixelOffset(R.dimen.dp_3),
                        itemView.resources.getDimensionPixelOffset(R.dimen.dp_8),
                        itemView.resources.getDimensionPixelOffset(R.dimen.dp_16),
                        itemView.resources.getDimensionPixelOffset(R.dimen.dp_8)
                )
                itemView.tv_total_payment.layoutParams = params
            }
            itemView.tv_total_payment.setPadding(itemView.resources.getDimensionPixelOffset(R.dimen.dp_8), 0,
                    itemView.resources.getDimensionPixelOffset(R.dimen.dp_8), 0)
        } else {
            itemView.tv_select_cod.visibility = View.GONE
        }
        itemView.tv_total_payment.text = model.totalPrice
        itemView.tv_select_payment_method.setOnClickListener {
            actionListener.onProcessToPayment()
        }
        itemView.tv_select_cod.setOnClickListener {
            actionListener.onProcessToPaymentCod()
        }
    }
}