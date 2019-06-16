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

class CartRecommendationViewHolder(val view: View, val listener: ActionListener) : RecyclerView.ViewHolder(view), RecommendationCardView.TrackingListener {

    companion object {
        val LAYOUT = R.layout.item_cart_recommendation
    }

    fun bind(element: CartRecommendationItemHolderData) {
        itemView.productCardView.setRecommendationModel(element.recommendationItem, this);
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