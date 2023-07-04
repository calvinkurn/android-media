package com.tokopedia.checkout.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentButtonPaymentBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentButtonPaymentModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.common.utils.rxViewClickThrottle
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

/**
 * Created by Irfan Khoirul on 2019-05-06.
 */

class ShipmentButtonPaymentViewHolder(private val binding: ItemShipmentButtonPaymentBinding, private val actionListener: ShipmentAdapterActionListener, val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        @JvmStatic
        val ITEM_VIEW_PAYMENT_BUTTON = R.layout.item_shipment_button_payment
    }

    fun bindViewHolder(model: ShipmentButtonPaymentModel) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(
            binding.tvTotalPayment,
            model.totalPrice,
            itemView.context.getString(R.string.content_desc_tv_total_payment)
        )

        if (model.loading) {
            binding.tvTotalPayment.gone()
            binding.loaderTotalPayment.type = LoaderUnify.TYPE_RECT
            binding.loaderTotalPayment.visible()
        } else {
            binding.loaderTotalPayment.gone()
            binding.tvTotalPayment.visible()
            binding.btnSelectPaymentMethod.visible()
            binding.btnSelectPaymentMethod.isEnabled = model.enable
            binding.btnSelectPaymentMethod.let {
                compositeSubscription.add(
                    rxViewClickThrottle(it).subscribe(object : Subscriber<Boolean>() {
                        override fun onNext(t: Boolean) {
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
