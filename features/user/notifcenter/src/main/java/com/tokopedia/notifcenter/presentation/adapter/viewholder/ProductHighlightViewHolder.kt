package com.tokopedia.notifcenter.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.mapper.ProductHighlightMapper.mapToCampaign
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.listener.ProductStockListener
import com.tokopedia.notifcenter.widget.CampaignRedView
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ProductHighlightViewHolder(
        view: View,
        private val listener: ProductStockListener
): RecyclerView.ViewHolder(view) {

    private val container: CardUnify = view.findViewById(R.id.container)
    private val imgProduct: ImageView = view.findViewById(R.id.imgProduct)
    private val txtProductName: TextView = view.findViewById(R.id.txtProductName)
    private val viewCampaignTag: CampaignRedView = view.findViewById(R.id.viewCampaignTag)
    private val txtProductPrice: Typography = view.findViewById(R.id.txtProductPrice)
    private val imgCampaign: ImageView = view.findViewById(R.id.imgCampaign)
    private val btnAddToCart: UnifyButton = view.findViewById(R.id.btnAddToCart)

    fun bind(element: ProductHighlightViewBean?) {
        if (element == null) return
        // impression tracker
        listener.productStockListCardImpression(element, adapterPosition)

        // content
        imgProduct.loadImage(element.imageUrl)
        txtProductName.text = element.name
        txtProductPrice.text = element.price
        viewCampaignTag.setCampaign(mapToCampaign(element))

        if (element.isFreeOngkir && element.freeOngkirIcon.isNotEmpty()) {
            imgCampaign.loadImage(element.freeOngkirIcon)
            imgCampaign.show()
        }

        container.setOnClickListener { productDetailClicked(element) }
        btnAddToCart.setOnClickListener { listener.onAddToCartProduct(element) }
    }

    private fun productDetailClicked(element: ProductHighlightViewBean) {
        listener.productStockListCardClicked(element, adapterPosition)

        RouteManager.route(
                itemView.context,
                ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                element.id.toString()
        )
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_notification_product_highlight

        fun builder(parent: ViewGroup, listener: ProductStockListener): ProductHighlightViewHolder {
            return ProductHighlightViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(
                            LAYOUT,
                            parent,
                            false
                    ), listener)
        }
    }

}