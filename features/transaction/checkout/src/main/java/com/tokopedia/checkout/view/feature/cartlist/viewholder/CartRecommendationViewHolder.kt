package com.tokopedia.checkout.view.feature.cartlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartRecommendationItemHolderData
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
        val LAYOUT = R.layout.item_cart_recommendation
    }

    fun bind(element: CartRecommendationItemHolderData) {
        itemView.productCardView.setRecommendationModel(element.recommendationItem, this)

        itemView.productCardView.showAddToCartButton()
        itemView.productCardView.setAddToCartClickListener {
            actionListener.onButtonAddToCartClicked(
                    element.recommendationItem.productId.toString(),
                    element.recommendationItem.shopId.toString(),
                    element.recommendationItem.minOrder)
        }

        if (element.rightPosition) {
            itemView.setPadding(padding2, 0, padding14, 0)
        } else {
            itemView.setPadding(padding14, 0, padding2, 0)
        }

        itemView.setOnClickListener {
            actionListener.onProductClicked(element.recommendationItem.productId.toString())
        }
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