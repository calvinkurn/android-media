package com.tokopedia.notifcenter.presentation.adapter.viewholder.base

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.widget.ProductVariantLayout

abstract class BaseProductCampaignViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseNotificationItemViewHolder(itemView, listener) {

    private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    private val productName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val productContainer: ConstraintLayout = itemView.findViewById(R.id.cl_product)
    private val productVariant: ProductVariantLayout = itemView.findViewById(R.id.pvl_variant)

    abstract fun bindProductView(element: NotificationItemViewBean)

    abstract fun bindProductClickTrack(element: NotificationItemViewBean)

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        bindProductView(element)
        assignProductClickListener(element)

        with(product) {
            productName.text = name
            productPrice.text = priceFormat
            productVariant.setupVariant(variant)
            ImageHandler.loadImage2(thumbnail, imageUrl, R.drawable.ic_notifcenter_loading_toped)
        }
    }

    override fun bindOnNotificationClick(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        container.setOnClickListener(getItemClickListener(product, element))
    }

    private fun assignProductClickListener(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        productContainer.setOnClickListener(getItemClickListener(product, element))
    }

    private fun getItemClickListener(product: ProductData, element: NotificationItemViewBean): View.OnClickListener {
        return View.OnClickListener {
            listener.itemClicked(element, adapterPosition)
            bindProductClickTrack(element)
            element.isRead = true
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    product.productId
            )
        }
    }

}