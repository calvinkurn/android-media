package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseProductCampaignViewHolder
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.UnifyButton

class ProductCheckoutViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseProductCampaignViewHolder(itemView, listener) {

    private val productCampaign: CampaignRedView = itemView.findViewById(R.id.cl_campaign)
    private val campaignTag: ImageView = itemView.findViewById(R.id.img_campaign)
    private val btnCheckout: UnifyButton = itemView.findViewById(R.id.btn_checkout)

    override fun bindProductView(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        onProductCheckoutClick(element)

        with(product) {
            if (product.shop?.freeShippingIcon != null) {
                campaignTag.loadImage(product.shop.freeShippingIcon)
                campaignTag.show()
            }
            productCampaign.setCampaign(campaign)
        }

    }

    private fun onProductCheckoutClick(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        btnCheckout.setOnClickListener {
            listener.addProductToCheckout(product)
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_product_checkout
    }

}