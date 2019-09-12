package com.tokopedia.navigation.presentation.adapter.viewholder.notificationupdate

import android.content.Intent
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.widget.CampaignLayout
import com.tokopedia.navigation.widget.ProductVariantLayout

class WishListNotificationViewHolder(itemView: View, listener: NotificationUpdateItemListener)
    : NotificationUpdateItemViewHolder(itemView, listener) {

    private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    private val productName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val productContainer: ConstraintLayout = itemView.findViewById(R.id.cl_product)
    private val productCampaign: CampaignLayout = itemView.findViewById(R.id.cl_campaign)
    private val productVariant: ProductVariantLayout = itemView.findViewById(R.id.pvl_variant)
    private val btnCart: ImageView = itemView.findViewById(R.id.iv_cart)

    override fun bindNotificationPayload(element: NotificationUpdateItemViewModel) {
        val product = element.getAtcProduct() ?: return

        with(product) {
            productName.text = name
            productPrice.text = priceFormat
            productCampaign.setupCampaign(campaign)
            productVariant.setupVariant(variant)
            ImageHandler.loadImage2(thumbnail, imageUrl, R.drawable.ic_loading_toped_new)
        }

        assignClickListenerAtc(product)
        assignProductClickListener(product)
    }

    private fun assignClickListenerAtc(product: ProductData) {
        btnCart.setOnClickListener {
            listener.getAnalytic().trackAtcOnClick(product)
            listener.addProductToCart(product)
        }
    }

    override fun bindOnNotificationClick(element: NotificationUpdateItemViewModel) {
        val product = element.getAtcProduct() ?: return
        container.setOnClickListener(getItemClickListener(product))
    }

    private fun assignProductClickListener(product: ProductData) {
        productContainer.setOnClickListener(getItemClickListener(product))
    }

    private fun getItemClickListener(product: ProductData): View.OnClickListener {
        return View.OnClickListener {
            listener.getAnalytic().trackAtcToPdpClick(product)
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    product.productId
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_notification_update_wishlist
    }

}