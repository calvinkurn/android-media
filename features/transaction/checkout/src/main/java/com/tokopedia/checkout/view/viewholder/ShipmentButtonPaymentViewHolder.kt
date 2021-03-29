package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

class ShipmentButtonPaymentViewHolder(val view: View, val actionListener: ShipmentAdapterActionListener, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(view) {

    private val tvTotalPayment: Typography = itemView.findViewById(R.id.tv_total_payment)
    private val btnSelectPaymentMethod: UnifyButton = itemView.findViewById(R.id.btn_select_payment_method)

    companion object {
        @JvmStatic
        val ITEM_VIEW_PAYMENT_BUTTON = R.layout.item_shipment_button_payment
    }

    fun bindViewHolder(model: ShipmentButtonPaymentModel) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(tvTotalPayment, model.totalPrice, itemView.context.getString(R.string.content_desc_tv_total_payment))

        btnSelectPaymentMethod.let {
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