package com.tokopedia.recommendation_widget_common.widget.viewtoview

interface ViewToViewWidgetBasicListener {
    fun onViewToViewItemImpressed(
        data: ViewToViewItemData,
        itemPosition: Int,
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
