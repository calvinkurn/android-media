package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.orderlist.OrderPaymentModel
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.holder_transaction_payment.view.*

class OrderPaymentViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderPaymentModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_payment
    }

    override fun bind(paymentModel: OrderPaymentModel) {
        val context = itemView.context

        //title
        itemView.order_payment_name.text = paymentModel.navPaymentModel.paymentAmountText

        //image
        if (paymentModel.navPaymentModel.imageUrl.isNotEmpty()) {
            itemView.order_payment_image.loadImage(paymentModel.navPaymentModel.imageUrl)
        }

        //description
        itemView.order_payment_description.text = paymentModel.navPaymentModel.descriptionText

        //status
        itemView.order_payment_status.text =
                if (paymentModel.navPaymentModel.statusText.isNotEmpty())
                    paymentModel.navPaymentModel.statusText
                else
                    context.getString(R.string.transaction_item_default_status)

        itemView.order_payment_status.setTextColor(
                ContextCompat.getColor(context, R.color.Unify_Y400)
        )

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    paymentModel.navPaymentModel.statusText)
            RouteManager.route(context, paymentModel.navPaymentModel.applink)
        }
    }
}