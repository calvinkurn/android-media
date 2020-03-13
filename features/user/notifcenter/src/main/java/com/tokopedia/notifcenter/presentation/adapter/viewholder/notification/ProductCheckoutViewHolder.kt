package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics.Companion.LABEL_BOTTOM_SHEET_LOCATION
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.MultipleProductCardAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseProductCampaignViewHolder
import com.tokopedia.notifcenter.util.ProductCardSnapHelper
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.UnifyButton

class ProductCheckoutViewHolder(
        itemView: View,
        listener: NotificationItemListener
) : BaseProductCampaignViewHolder(itemView, listener) {

    private val cardContainer: RelativeLayout = itemView.findViewById(R.id.container_product_card)
    private val productCampaign: CampaignRedView = itemView.findViewById(R.id.cl_campaign)
    private val lstProduct: RecyclerView = itemView.findViewById(R.id.lst_products)
    private val btnCheckout: UnifyButton = itemView.findViewById(R.id.btn_checkout)
    private val campaignTag: ImageView = itemView.findViewById(R.id.img_campaign)

    private var multiProductAdapter: MultipleProductCardAdapter? = null

    override fun bindProductView(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        snapMultiProductItem()
        onProductCheckoutClick(element)
        productCardItemView(element)

        with(product) {
            if (product.shop?.freeShippingIcon != null &&
                product.shop.freeShippingIcon.isNotEmpty()) {
                campaignTag.loadImage(product.shop.freeShippingIcon)
                campaignTag.show()
            }
            productCampaign.setCampaign(campaign)
        }
    }

    private fun snapMultiProductItem() {
        if (lstProduct.onFlingListener == null) {
            ProductCardSnapHelper().attachToRecyclerView(lstProduct)
        }
    }

    private fun productCardItemView(element: NotificationItemViewBean) {
        if (element.products.size > SINGLE_PRODUCT) {
            cardContainer.hide()
            lstProduct.show()
            listener.getAnalytic().trackProductListImpression(
                    location = LABEL_BOTTOM_SHEET_LOCATION,
                    notification = element
            )
            val factory = MultipleProductCardFactoryImpl(
                    sourceView = SourceMultipleProductView.NotificationCenter,
                    listener = listener
            )
            if (multiProductAdapter == null) {
                multiProductAdapter = MultipleProductCardAdapter(
                        multipleProductCardFactory = factory,
                        isResizable = true
                )
            }
            lstProduct.adapter = multiProductAdapter
            multiProductAdapter?.removeAllItem()
            multiProductAdapter?.insertData(
                    MultipleProductCardMapper.map(element)
            )
        } else if (element.products.size == SINGLE_PRODUCT) {
            cardContainer.show()
            lstProduct.hide()
            listener.getAnalytic().trackProductListImpression(
                    notification = element
            )
        }
    }

    override fun trackProduct(element: NotificationItemViewBean) {
        if (element.totalProduct == SINGLE_PRODUCT) {
            listener.getAnalytic().trackSingleProductCheckoutCardClick(
                    notification = element
            )
        } else if (element.totalProduct > SINGLE_PRODUCT) {
            listener.getAnalytic().trackMultiProductCheckoutCardClick(
                    notification = element
            )
        }
    }

    private fun onProductCheckoutClick(element: NotificationItemViewBean) {
        btnCheckout.setOnClickListener {
            notificationItemMarkedClick(element)
            listener.getAnalytic().trackAtcOnSingleProductClick(notification = element)
            listener.addProductToCheckout(element.userInfo, element)
        }
    }

    companion object {
        private const val SINGLE_PRODUCT = 1

        @LayoutRes val LAYOUT = R.layout.item_notification_product_checkout
    }

}