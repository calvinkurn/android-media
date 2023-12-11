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
}
