package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlinx.android.synthetic.main.holder_transaction_product.view.*

class OrderProductViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderProductModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_product
    }

    override fun bind(element: OrderProductModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(productModel: OrderProductModel) {
        val context = itemView.context

        itemView.addOnImpressionListener(productModel)  {
            mainNavListener.putEEToTrackingQueue(
                    TrackingTransactionSection.getImpressionOnOrderStatus(
                        userId = mainNavListener.getUserId(),
                        orderLabel = productModel.navProductModel.statusText,
                        position = adapterPosition,
                        orderId = productModel.navProductModel.id)
            )
        }
        //title
        itemView.order_product_name.text = productModel.navProductModel.productNameText

        //image
        if (productModel.navProductModel.imageUrl.isNotEmpty()) {
            val imageView = itemView.order_product_image
            val shimmer = itemView.order_product_image_shimmer
            Glide.with(imageView.context)
                    .load(productModel.navProductModel.imageUrl)
                    .placeholder(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                    .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                    .dontAnimate()
                    .fitCenter()
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            imageView.setImageDrawable(resource)
                            shimmer.gone()
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            shimmer.visible()
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            shimmer.gone()
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            shimmer.gone()
                        }
                    })
        }

        //description
        itemView.order_product_description.text = productModel.navProductModel.descriptionText
        if (productModel.navProductModel.descriptionTextColor.isNotEmpty()) {
            itemView.order_product_description.setTextColor(
                   Color.parseColor(productModel.navProductModel.descriptionTextColor)
            )
        }

        //status
        itemView.order_product_status.text = productModel.navProductModel.statusText
        itemView.order_product_status.setTextColor(
                ContextCompat.getColor(context, R.color.Unify_Y400)
        )

        //more than 1 product
        if (productModel.navProductModel.additionalProductCount != 0) {
            itemView.order_product_image_layer.visibility = View.VISIBLE
            itemView.order_product_count.visibility = View.VISIBLE
            itemView.order_product_count.text = String.format(
                    context.getString(R.string.transaction_item_total_product),
                    productModel.navProductModel.additionalProductCount
            )
        } else {
            itemView.order_product_image_layer.visibility = View.GONE
            itemView.order_product_count.visibility = View.GONE
        }

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    productModel.navProductModel.statusText)
            RouteManager.route(context, productModel.navProductModel.applink)
        }
    }
}