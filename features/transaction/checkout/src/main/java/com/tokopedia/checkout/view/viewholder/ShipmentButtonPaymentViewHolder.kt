package com.tokopedia.checkout.view.viewholder

import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
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
            itemView.btn_select_cod.buttonType = model.abTestButton.getUnifyButtonType()
            itemView.btn_select_cod.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.tv_total_payment.setTextAppearance(R.style.button_payment)
            } else {
                itemView.tv_total_payment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.0f)
                itemView.tv_total_payment.height = itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_40)
                val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(itemView.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_3),
                        itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_8),
                        itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_16),
                        itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_8)
                )
                itemView.tv_total_payment.layoutParams = params
            }
            itemView.tv_total_payment.setPadding(itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_8), 0,
                    itemView.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_8), 0)
        } else {
            itemView.btn_select_cod.visibility = View.GONE
        }
        TextAndContentDescriptionUtil.setTextAndContentDescription(itemView.tv_total_payment, model.totalPrice, itemView.tv_total_payment.context.getString(R.string.content_desc_tv_total_payment))
        itemView.tv_total_payment.text = model.totalPrice
        itemView.btn_select_payment_method.buttonType = model.abTestButton.getUnifyButtonType()
        itemView.btn_select_payment_method.setOnClickListener {
            actionListener.onProcessToPayment()
        }
        itemView.btn_select_cod.setOnClickListener {
            actionListener.onProcessToPaymentCod()
        }
    }
}