package com.tokopedia.recommendation_widget_common.widget.viewtoview

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

interface ViewToViewWidgetBasicListener {
    fun onViewToViewBannerImpressed(
        data: RecommendationWidget,
        adapterPosition: Int,
    )

    fun onViewToViewItemClicked(
        data: ViewToViewItemData,
        itemPosition: Int,
        adapterPosition: Int,
    )

    fun onViewToViewReload(pageName: String)
    fun onWidgetFail(pageName: String, e: Throwable)
    fun onShowError(pageName: String, e: Throwable)
}
