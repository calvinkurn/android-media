package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationItemGridViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.recommendation.HomeRecommendationItemListViewHolder

object HomeRecommendationUtil {
    const val LAYOUT_NAME_LIST = "Infinite_list_scroll"
    const val LAYOUT_NAME_GRID = "Infinite"

    fun HomeRecommendationItemDataModel.getLayout(): Int {
        return when(layoutName) {
            LAYOUT_NAME_LIST -> HomeRecommendationItemListViewHolder.LAYOUT
            LAYOUT_NAME_GRID -> HomeRecommendationItemGridViewHolder.LAYOUT
            else -> HomeRecommendationItemGridViewHolder.LAYOUT
        }
    }

    fun HomeRecommendationItemDataModel.isFullSpan(): Boolean {
        return layoutName == LAYOUT_NAME_LIST
    }
}
