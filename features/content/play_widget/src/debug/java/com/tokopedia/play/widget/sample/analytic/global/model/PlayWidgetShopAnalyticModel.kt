package com.tokopedia.play.widget.sample.analytic.global.model

import com.tokopedia.play.widget.sample.analytic.global.model.PlayWidgetAnalyticModel

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetShopAnalyticModel(
    isOwnShop: Boolean,
) : PlayWidgetAnalyticModel {

    override val prefix: String = "SHOP_PAGE"

    override val category: String = "shop page - ${if (isOwnShop) "seller" else "buyer"}"

    override val promotionsItemName: String = "shop-play-widget"

    override val promotionsCreativeName: String = "play widget in shop"
}