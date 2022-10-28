package com.tokopedia.tokopedianow.home.analytic

import com.tokopedia.play.widget.analytic.global.model.PlayWidgetAnalyticModel

object HomePlayWidgetAnalyticModel : PlayWidgetAnalyticModel {

    override val prefix: String = "NOW"
    override val category: String = "tokonow - homepage"
    override val promotionsItemName: String = "play-home-widget"
    override val promotionsCreativeName: String = "play widget in homepage"
}