package com.tokopedia.recommendation_widget_common.widget.viewtoview

interface ViewToViewItemListener {
    fun onViewToViewItemImpressed(item: ViewToViewItemData, position: Int)
    fun onViewToViewItemClicked(item: ViewToViewItemData, position: Int)
}
