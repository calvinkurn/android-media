package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseProductCampaignViewHolder
import com.tokopedia.notifcenter.widget.CampaignGreenView

class WishListNotificationViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseProductCampaignViewHolder(itemView, listener) {

    private val productCampaign: CampaignGreenView = itemView.findViewById(R.id.cl_campaign)
    private val btnCart: ImageView = itemView.findViewById(R.id.iv_cart)

    override fun bindProductView(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        val atcDrawable = ContextCompat.getDrawable(itemView.context, R.drawable.notifcenter_ic_add_to_cart)
        listener.getAnalytic().saveProductCardImpression(element, adapterPosition)

        with(product) {
            btnCart.setImageDrawable(atcDrawable)
            productCampaign.setupCampaign(campaign)
        }

        assignClickListenerAtc(element)
    }

    private fun assignClickListenerAtc(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        btnCart.setOnClickListener {
            listener.addProductToCart(product, onSuccessAddToCart())
            listener.itemClicked(element, adapterPosition)
            element.isRead = true
        }
    }

    override fun trackProduct(element: NotificationItemViewBean) {
        listener.getAnalytic().trackAtcToPdpClick(element)
    }

    private fun onSuccessAddToCart(): (DataModel) -> Unit {
        return {
            val checkDrawable = ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.notifcenter_ic_add_to_cart_check_grey
            )
            btnCart.setImageDrawable(checkDrawable)

            // show toaster
            val message = it.message.first()
            listener.onSuccessAddToCart(message)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_update_wishlist
    }

}