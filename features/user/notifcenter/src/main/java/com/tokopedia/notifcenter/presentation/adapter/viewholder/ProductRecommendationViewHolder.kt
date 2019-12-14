package com.tokopedia.notifcenter.presentation.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.domain.pojo.ProductData
import com.tokopedia.notifcenter.presentation.view.listener.NotificationUpdateItemListener

class ProductRecommendationViewHolder(itemView: View, val listener: NotificationUpdateItemListener)
    : RecyclerView.ViewHolder(itemView) {

    private val thumbnail = itemView.findViewById<ImageView>(R.id.iv_product)

    fun bind(productData: ProductData) {
        ImageHandler.loadImage2(thumbnail, productData.imageUrl, R.drawable.ic_loading_toped_new)

        itemView.setOnClickListener {
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productData.productId
            )
            trackOnProductThumbnailToPdp(productData)
        }
    }

    private fun trackOnProductThumbnailToPdp(productData: ProductData) {
        listener.getAnalytic().trackOnProductThumbnailToPdp(productData, layoutPosition)
    }

    companion object {

        fun create(parent: ViewGroup, listener: NotificationUpdateItemListener): ProductRecommendationViewHolder {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_notification_product_recommendation, parent, false)
            return ProductRecommendationViewHolder(layout, listener)
        }

    }
}