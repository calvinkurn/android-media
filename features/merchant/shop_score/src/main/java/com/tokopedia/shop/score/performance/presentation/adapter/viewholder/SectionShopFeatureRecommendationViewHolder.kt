package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.ShopScoreItemDecoration
import com.tokopedia.shop.score.performance.presentation.adapter.ItemFeatureRecommendationAdapter
import com.tokopedia.shop.score.performance.presentation.adapter.ItemRecommendationFeatureListener
import com.tokopedia.shop.score.performance.presentation.model.SectionShopRecommendationUiModel
import kotlinx.android.synthetic.main.shop_feature_recommendation_section.view.*

class SectionShopFeatureRecommendationViewHolder(view: View,
                                                 private val itemRecommendationFeatureListener: ItemRecommendationFeatureListener):
        AbstractViewHolder<SectionShopRecommendationUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.shop_feature_recommendation_section
    }

    private var itemFeatureRecommendationAdapter: ItemFeatureRecommendationAdapter? = null

    override fun bind(element: SectionShopRecommendationUiModel?) {
        itemFeatureRecommendationAdapter = ItemFeatureRecommendationAdapter(itemRecommendationFeatureListener)
        with(itemView) {
            rvShopScoreCreation?.apply {
                if(itemDecorationCount.isZero()) {
                    addItemDecoration(ShopScoreItemDecoration())
                }
                adapter = itemFeatureRecommendationAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
        element?.recommendationShopList?.let { itemFeatureRecommendationAdapter?.setItemRecommendationList(it) }
    }
}