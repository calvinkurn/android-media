package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopWidgetTypeFactory
import com.tokopedia.shop_widget.common.util.WidgetState

abstract class BaseShopHomeWidgetUiModel : Visitable<ShopWidgetTypeFactory> {
    abstract val widgetId: String
    abstract val layoutOrder: Int
    abstract val name: String
    abstract val type: String
    abstract val header: Header
    abstract val isFestivity: Boolean
    var widgetState: WidgetState = WidgetState.INIT
    var isNewData: Boolean = false
    var widgetMasterId = ""
    data class Header(
        val title: String = "",
        val subtitle: String = "",
        val ctaText: String = "",
        val ctaLink: String = "",
        val cover: String = "",
        val ratio: String = "",
        val isATC: Int = 0,
        val etalaseId: String = "",
        val data: List<Data> = listOf()
    ){
        data class Data(
            val linkType: String = "",
            val linkId: Long = 0L,
            val link: String = ""
        )
    }

    fun isWidgetShowPlaceholder(): Boolean {
        return widgetState == WidgetState.PLACEHOLDER || widgetState == WidgetState.LOADING
    }
}
