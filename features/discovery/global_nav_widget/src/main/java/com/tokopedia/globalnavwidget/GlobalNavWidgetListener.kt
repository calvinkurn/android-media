package com.tokopedia.globalnavwidget

interface GlobalNavWidgetListener {

    fun onClickItem(item: GlobalNavWidgetModel.Item)

    fun onClickSeeAll(globalNavWidgetModel: GlobalNavWidgetModel)
}