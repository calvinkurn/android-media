package com.tokopedia.notifcenter.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics.Companion.LABEL_BOTTOM_SHEET_LOCATION
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper as Mapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.MultipleProductCardViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.notifcenter.widget.ProductVariantLayout
import com.tokopedia.unifycomponents.UnifyButton

class MultipleProductCardViewHolder(
        itemView: View,
        private val sourceView: SourceMultipleProductView,
        val listener: NotificationItemListener
): AbstractViewHolder<MultipleProductCardViewBean>(itemView) {

    private val thumbnail: ImageView = itemView.findViewById(R.id.iv_thumbnail)
    private val productName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
    private val productVariant: ProductVariantLayout = itemView.findViewById(R.id.pvl_variant)
    private val productCampaign: CampaignRedView = itemView.findViewById(R.id.cl_campaign)
    private val productContainer: ConstraintLayout = itemView.findViewById(R.id.cl_product)
    private val btnCheckout: UnifyButton = itemView.findViewById(R.id.btn_checkout)
    private val campaignTag: ImageView = itemView.findViewById(R.id.img_campaign)

    override fun bind(element: MultipleProductCardViewBean?) {
        if (element == null) return
        val product = element.product
        impressionTracker(element)
        productCheckoutClicked(element)
        isCampaignActive(product)

        with(product) {
            ImageHandler.loadImage2(thumbnail, imageUrl, R.drawable.ic_notifcenter_loading_toped)
            productCampaign.setCampaign(campaign)
            productVariant.setupVariant(variant)
            productPrice.text = priceFormat
            productName.text = name
        }
    }

    private fun impressionTracker(element: MultipleProductCardViewBean) {
        when(sourceView) {
            is SourceMultipleProductView.NotificationCenter -> {
                listener.getAnalytic().trackMultiProductListImpression(
                        userId = element.userInfo.userId,
                        productNumber = adapterPosition,
                        notification = element
                )
            }
            is SourceMultipleProductView.BottomSheetDetail -> {
                listener.getAnalytic().trackMultiProductListImpression(
                        userId = element.userInfo.userId,
                        productNumber = adapterPosition,
                        location = LABEL_BOTTOM_SHEET_LOCATION,
                        notification = element
                )
            }
        }
    }

    private fun isCampaignActive(product: ProductData) {
        if (product.shop?.freeShippingIcon != null && product.shop.freeShippingIcon.isNotEmpty()) {
            campaignTag.loadImage(product.shop.freeShippingIcon)
            campaignTag.show()
        }
    }

    private fun productCheckoutClicked(element: MultipleProductCardViewBean) {
        val notification = Mapper.map(element)
        productContainer.setOnClickListener {
            listener.itemClicked(notification, adapterPosition)
            when(sourceView) {
                is SourceMultipleProductView.NotificationCenter -> {
                    listener.getAnalytic().trackMultiProductCheckoutCardClick(
                            notification = element
                    )
                }
                is SourceMultipleProductView.BottomSheetDetail -> {
                    listener.getAnalytic().trackMultiProductCheckoutCardClick(
                            eventLocation = LABEL_BOTTOM_SHEET_LOCATION,
                            notification = element
                    )
                }
            }
            RouteManager.route(
                    itemView.context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    element.product.productId
            )
        }

        btnCheckout.setOnClickListener {
            when(sourceView) {
                is SourceMultipleProductView.NotificationCenter -> {
                    listener.getAnalytic().trackAtcOnMultiProductClick(
                            notification = element
                    )
                }
                is SourceMultipleProductView.BottomSheetDetail -> {
                    listener.getAnalytic().trackAtcOnMultiProductClick(
                            eventLocation = LABEL_BOTTOM_SHEET_LOCATION,
                            notification = element
                    )
                }
            }
            listener.itemClicked(notification, adapterPosition)
            listener.addProductToCheckout(element.userInfo, Mapper.map(element))
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_product_card
    }

}