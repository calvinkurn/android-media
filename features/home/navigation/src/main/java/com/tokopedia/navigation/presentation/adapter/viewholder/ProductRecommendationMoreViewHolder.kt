package com.tokopedia.navigation.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.navigation.presentation.adapter.NotifcenterProductRecommendationAdapter
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

class ProductRecommendationMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val moreCount = itemView.findViewById<TextView>(R.id.tvMoreCount)
    private val thumbnail = itemView.findViewById<ImageView>(R.id.iv_product)

    fun bind(products: List<ProductData>, position: Int, totalProducts: Int) {
        val productData = products[position]
        val more = getMoreProductCount(products, totalProducts)

        ImageHandler.loadImage2(thumbnail, productData.imageUrl, R.drawable.ic_loading_toped_new)
        moreCount.text = more

        itemView.setOnClickListener {
            RouteManager.route(
                    itemView.context,
                    ApplinkConst.RECOMMENDATION_PAGE,
                    productData.productId,
                    NotificationUpdateItemViewModel.SOURCE
            )
        }
    }

    private fun getMoreProductCount(products: List<ProductData>, totalProducts: Int): String {
        return "+${totalProducts - getShowedProductCount(products)}"
    }

    private fun getShowedProductCount(products: List<ProductData>): Int {
        val showCount = NotifcenterProductRecommendationAdapter.MAX_ITEM - 1
        return if (products.size >= showCount) {
            showCount
        } else {
            products.size
        }
    }

    companion object {
        fun create(parent: ViewGroup): ProductRecommendationMoreViewHolder {
            val layout = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_notification_product_recommendation_more, parent, false)
            return ProductRecommendationMoreViewHolder(layout)
        }
    }

}