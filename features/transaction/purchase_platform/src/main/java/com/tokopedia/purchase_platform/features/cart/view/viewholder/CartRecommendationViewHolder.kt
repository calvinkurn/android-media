package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecommendationItemHolderData
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import kotlinx.android.synthetic.main.item_cart_recommendation.view.*

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(val view: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(view), RecommendationCardView.TrackingListener {

    val padding14 = itemView.resources.getDimension(R.dimen.dp_14).toInt()
    val padding2 = itemView.resources.getDimension(R.dimen.dp_2).toInt()

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    fun bind(element: CartRecommendationItemHolderData) {
        itemView.productCardView.setRecommendationModel(element.recommendationItem, this)

        itemView.productCardView.showAddToCartButton()
        itemView.productCardView.setAddToCartClickListener {
            actionListener.onButtonAddToCartClicked(element)
        }

        itemView.setOnClickListener {
            actionListener.onProductClicked(element.recommendationItem.productId.toString())
        }
    }

    fun clearImage() {
        ImageHandler.clearImage(itemView.productCardView.imageView)
    }

    override fun onImpressionTopAds(item: RecommendationItem) {

    }

    override fun onImpressionOrganic(item: RecommendationItem) {

    }

    override fun onClickTopAds(item: RecommendationItem) {

    }

    override fun onClickOrganic(item: RecommendationItem) {

    }

}