package com.tokopedia.recommendation_widget_common.infinite.foryou.recom

object HomeRecommendationUtil {

    const val LAYOUT_NAME_LIST = "infinite_list_scroll"
    const val LAYOUT_NAME_GRID = "infinite"

    fun RecommendationCardModel.getLayout(): Int {
        return when(layoutName) {
            LAYOUT_NAME_LIST -> RecommendationCardListViewHolder.LAYOUT
            LAYOUT_NAME_GRID -> RecommendationCardGridViewHolder.LAYOUT
            else -> RecommendationCardGridViewHolder.LAYOUT
        }
    }

    fun RecommendationCardModel.isFullSpan(): Boolean {
        return layoutName == LAYOUT_NAME_LIST
    }
}
