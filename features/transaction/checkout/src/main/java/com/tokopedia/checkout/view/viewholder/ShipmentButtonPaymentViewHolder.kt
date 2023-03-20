package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.rxViewClickThrottle
import com.tokopedia.unifycomponents.LoaderUnify
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
    private val loaderBtnPayment: LoaderUnify = itemView.findViewById(R.id.loader_btn_payment)

    companion object {
        @JvmStatic
        val ITEM_VIEW_PAYMENT_BUTTON = R.layout.item_shipment_button_payment
    }

    fun bindViewHolder(model: ShipmentButtonPaymentModel) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(
            tvTotalPayment,
            model.totalPrice,
            itemView.context.getString(R.string.content_desc_tv_total_payment)
        )

        if (model.loading) {
            btnSelectPaymentMethod.gone()
            loaderBtnPayment.type = LoaderUnify.TYPE_RECT
            loaderBtnPayment.visible()
        } else {
            loaderBtnPayment.gone()
            btnSelectPaymentMethod.visible()
            btnSelectPaymentMethod.isEnabled = model.enable
            btnSelectPaymentMethod.let {
                compositeSubscription.add(
                    rxViewClickThrottle(it).subscribe(object : Subscriber<Boolean>() {
                        override fun onNext(t: Boolean?) {
                            actionListener.onProcessToPayment()
                        }

                        override fun onCompleted() {
                            /* no-op */
                        }

                        override fun onError(e: Throwable?) {
                            /* no-op */
                        }
                    })
                )
            }
        }
    }
}
