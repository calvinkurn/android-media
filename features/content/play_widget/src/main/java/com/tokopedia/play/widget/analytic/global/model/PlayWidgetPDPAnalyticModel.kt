package com.tokopedia.play.widget.analytic.global.model

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
class PlayWidgetPDPAnalyticModel(
    override val productId: String
) : PlayWidgetAnalyticModel {

    override val prefix: String = "PDP"

    override val category: String = "product detail page"

    override val promotionsItemName: String = "play-pdp-widget"

    override val promotionsCreativeName: String = "play widget in pdp"
}
