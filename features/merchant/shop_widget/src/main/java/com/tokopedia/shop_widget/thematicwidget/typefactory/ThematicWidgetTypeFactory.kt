package com.tokopedia.shop_widget.thematicwidget.typefactory

import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel

interface ThematicWidgetTypeFactory {
    fun type(uiModel: ThematicWidgetUiModel): Int
}