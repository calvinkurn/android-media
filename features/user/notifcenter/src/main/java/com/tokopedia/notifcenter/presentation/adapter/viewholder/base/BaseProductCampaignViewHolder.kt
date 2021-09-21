package com.tokopedia.notifcenter.presentation.adapter.viewholder.base

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.state.BottomSheetType
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.util.isSingleItem
import com.tokopedia.notifcenter.widget.ProductVariantLayout
import timber.log.Timber

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
    abstract fun trackProduct(element: NotificationItemViewBean)

    override fun bindNotificationPayload(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        bindProductView(element)

        with(product) {
            productName.text = name
            productPrice.text = priceFormat
            productVariant.setupVariant(variant)
            ImageHandler.loadImage2(thumbnail, imageUrl, R.drawable.ic_notifcenter_loading_toped)
        }
    }

    override fun bindOnNotificationClick(element: NotificationItemViewBean) {
        productContainer.setOnClickListener { onItemClicked(element) }
        container.setOnClickListener { onItemClicked(element) }
    }

    private fun onItemClicked(element: NotificationItemViewBean) {
        isProductStockHandler(element)
        trackProduct(element)
        notificationItemMarkedClick(element)
    }

    private fun isProductStockHandler(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        if (element.products.isSingleItem() && !element.isShowBottomSheet) {
            productDetailClicked(product)
        } else {
            onBindDetailProductClick(element)
        }
    }

    private fun productDetailClicked(product: ProductData) {
        RouteManager.route(
                itemView.context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                product.productId
        )
    }

    private fun onBindDetailProductClick(element: NotificationItemViewBean) {
        val bottomSheetType = BottomSheetType.map(element.typeBottomSheet)
        if (element.isShowBottomSheet) {
            when (bottomSheetType) {
                is BottomSheetType.StockHandler -> {
                    listener.onItemStockHandlerClick(element)
                }
                else -> {
                    if(bottomSheetType != null) {
                        listener.showNotificationDetail(bottomSheetType, element)
                    } else {
                        ServerLogger.log(Priority.P2, "ACCOUNT_HOME_ERROR", mapOf("type" to "NotifCenter",
                                "bottomSheetType" to "bottomSheetType is Null",
                                "element" to element.toString()
                        ))
                    }
                }
            }
        }
    }

}