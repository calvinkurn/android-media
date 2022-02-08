package com.tokopedia.shop_widget.thematicwidget.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.common.uimodel.DynamicHeaderUiModel
import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.shop_widget.thematicwidget.typefactory.ThematicWidgetTypeFactory

data class ThematicWidgetUiModel(
    val widgetId : String,
    val layoutOrder :Int,
    val name :String,
    val type :String,
    val header: DynamicHeaderUiModel,
    val productList: List<ProductCardUiModel>,
    val firstBackgroundColor: String,
    val secondBackgroundColor: String,
    var widgetState: WidgetState = WidgetState.INIT,
    var isNewData: Boolean = false,
    var widgetMasterId: String = ""
): Visitable<ThematicWidgetTypeFactory> {
    override fun type(typeFactory: ThematicWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}