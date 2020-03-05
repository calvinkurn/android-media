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
import com.tokopedia.notifcenter.data.mapper.MultipleProductCardMapper
import com.tokopedia.notifcenter.data.state.SourceMultipleProductView
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.listener.NotificationItemListener
import com.tokopedia.notifcenter.presentation.adapter.MultipleProductCardAdapter
import com.tokopedia.notifcenter.presentation.adapter.typefactory.product.MultipleProductCardFactoryImpl
import com.tokopedia.notifcenter.presentation.adapter.viewholder.base.BaseProductCampaignViewHolder
import com.tokopedia.notifcenter.util.ProductCardSnapHelper
import com.tokopedia.notifcenter.util.isSingleItem
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

    private val multiProductAdapter by lazy {
        val factory = MultipleProductCardFactoryImpl(
                sourceView = SourceMultipleProductView.NotificationCenter,
                listener = listener
        )
        MultipleProductCardAdapter(factory, true)
    }

    override fun bindProductView(element: NotificationItemViewBean) {
        val product = element.getAtcProduct() ?: return
        listener.getAnalytic().trackProductListImpression(notification = element)
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

    private fun productCardItemView(element: NotificationItemViewBean) {
        if (element.products.isSingleItem()) {
            cardContainer.show()
        } else {
            cardContainer.hide()
            lstProduct.adapter = multiProductAdapter
            multiProductAdapter.insertData(
                    MultipleProductCardMapper.map(element)
            )
            if (lstProduct.onFlingListener == null) {
                ProductCardSnapHelper().attachToRecyclerView(lstProduct)
            }
        }
    }

    override fun bindProductCardClick(element: NotificationItemViewBean) {
        listener.getAnalytic().trackProductCheckoutCardClick(notification = element)
    }

    private fun onProductCheckoutClick(element: NotificationItemViewBean) {
        btnCheckout.setOnClickListener {
            baseItemMarkedClick(element)
            listener.getAnalytic().trackProductCheckoutBuyClick(notification = element)
            listener.addProductToCheckout(element.userInfo, element.getAtcProduct())
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_product_checkout
    }

}