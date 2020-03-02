package com.tokopedia.shop.home.view.model

import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory

/**
 * Created by rizqiaryansa on 2020-02-21.
 */
data class WidgetModel(
        val widgetID: Int? = 0,
        val layoutOrder: Int? = 0,
        val name: String? = null,
        val type: String? = null,
        val header: HeaderWidgetModel? = null,
        val data: MutableList<WidgetDataModel>? = null
): BaseShopHomeViewModel {
    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    @Transient
    var binder = VideoBinder(this)
}

data class HeaderWidgetModel(
        val title: String? = null,
        val ctaText: String? = null,
        val ctaLink: String? = null,
        val cover: String? = null
)