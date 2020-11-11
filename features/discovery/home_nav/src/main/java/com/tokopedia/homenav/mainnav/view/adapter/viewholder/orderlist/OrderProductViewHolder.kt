package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.orderlist.OrderProductModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlinx.android.synthetic.main.holder_transaction_product.view.*

class OrderProductViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderProductModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_product
    }

    override fun bind(productModel: OrderProductModel) {
        val context = itemView.context

        //title
        itemView.order_product_name.text = productModel.navProductModel.productNameText

        //image
        if (productModel.navProductModel.imageUrl.isNotEmpty()) {
            itemView.order_product_image.loadImage(productModel.navProductModel.imageUrl)
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
                    productModel.navProductModel.id)
            RouteManager.route(context, productModel.navProductModel.applink)
        }
    }
}