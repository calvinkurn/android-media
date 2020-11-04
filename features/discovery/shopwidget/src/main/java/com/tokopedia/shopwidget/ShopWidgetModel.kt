package com.tokopedia.shopwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable

class ShopWidgetModel(
        var template: String = "",
        var title: String = "",
        var position: Int = -1,
        var shopCardList: List<ShopCardModel> = listOf()
) {
    data class ShopCardModel(
        val type: String = "",
        val id: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val subtitle: String = "",
        val iconTitle: String = "",
        val iconSubtitle: String = "",
        val urlTracker: String = "",
        val imageUrl: String = "",
        val products: List<ShopCardProductModel> = listOf()
    ): Visitable<ShopCardAdapterTypeFactory> {
        data class ShopCardProductModel(
                val imageUrl: String = ""
        )

        override fun type(typeFactory: ShopCardAdapterTypeFactory): Int {
            return typeFactory.type(type)
        }
    }
}