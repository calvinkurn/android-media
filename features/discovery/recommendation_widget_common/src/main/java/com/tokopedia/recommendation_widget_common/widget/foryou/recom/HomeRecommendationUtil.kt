package com.tokopedia.recommendation_widget_common.widget.foryou.recom

object HomeRecommendationUtil {

    private const val LAYOUT_NAME_LIST = "infinite_list_scroll"
    private const val LAYOUT_NAME_GRID = "infinite"

    fun HomeRecommendationModel.getLayout(): Int {
        return when(layoutName) {
            LAYOUT_NAME_LIST -> HomeRecommendationListViewHolder.LAYOUT
            LAYOUT_NAME_GRID -> HomeRecommendationGridViewHolder.LAYOUT
            else -> HomeRecommendationGridViewHolder.LAYOUT
        }
    }

    fun HomeRecommendationModel.isFullSpan(): Boolean {
        return layoutName == LAYOUT_NAME_LIST
    }
}
