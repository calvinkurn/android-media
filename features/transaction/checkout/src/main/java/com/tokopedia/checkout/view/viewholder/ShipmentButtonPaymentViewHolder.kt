package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.item_shipment_button_payment.view.*
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

class ShipmentButtonPaymentViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val ITEM_VIEW_PAYMENT_BUTTON = R.layout.item_shipment_button_payment
    }

    fun bindViewHolder(model: ShipmentButtonPaymentModel) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(itemView.tv_total_payment, model.totalPrice, itemView.tv_total_payment.context.getString(R.string.content_desc_tv_total_payment))
        itemView.btn_select_payment_method.buttonType = model.abTestButton.getUnifyButtonType()

        itemView.btn_select_payment_method?.let {
            compositeSubscription.add(
                    rxViewClickDebounce(it).subscribe(object : Subscriber<Boolean>() {
                        override fun onNext(t: Boolean?) {
                            actionListener.onProcessToPayment()
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                        }
                    })
            )
        }
    }
}