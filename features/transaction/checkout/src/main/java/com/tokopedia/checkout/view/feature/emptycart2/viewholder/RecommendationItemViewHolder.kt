package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.RecommendationItemUiModel
import com.tokopedia.recommendation_widget_common.presentation.RecommendationCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * Created by Irfan Khoirul on 2019-05-27.
 */

class RecommendationItemViewHolder(val view: View, val listener: ActionListener, val itemWidth: Int)
    : RecyclerView.ViewHolder(view), RecommendationCardView.TrackingListener {

    private val recommendationCardView: RecommendationCardView = itemView.findViewById(R.id.productCardView)

    override fun onImpressionTopAds(item: RecommendationItem) {

    }

    override fun onImpressionOrganic(item: RecommendationItem) {

    }

    override fun onClickTopAds(item: RecommendationItem) {

    }

    override fun onClickOrganic(item: RecommendationItem) {

    }

    companion object {
        val LAYOUT = R.layout.item_checkout_product_recommendation_inner
    }

    fun bind(element: RecommendationItemUiModel) {
        if (element.isLastItem) {
            element.isLastItem = false
            listener.onLoadMoreRecommendation()
        }
        if (element.recommendationItem != null) {
            recommendationCardView.setRecommendationModel(element.recommendationItem!!, this)
            val item = element.recommendationItem
            if (item?.isTopAds == true) {
                recommendationCardView.setRecommendationModel(item, this)
            }
        }
    }

}