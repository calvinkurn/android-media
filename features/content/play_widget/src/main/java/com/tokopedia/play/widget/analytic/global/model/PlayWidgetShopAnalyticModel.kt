package com.tokopedia.play.widget.analytic.global.model

import com.tokopedia.play.widget.analytic.const.trackerMultiFields

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetShopAnalyticModel(
    private val isOwnShop: Boolean,
) : PlayWidgetAnalyticModel {

    override val prefix: String = "SHOP_PAGE"

    override val category: String = "shop page - buyer"

    override val promotionsItemName: String = "play-shop-widget"

    override val promotionsCreativeName: String = "play widget in shop"

    override fun eventActionChannel(action: String): String {
        return if (isOwnShop) trackerMultiFields(action, "own shop")
        else action
    }
}