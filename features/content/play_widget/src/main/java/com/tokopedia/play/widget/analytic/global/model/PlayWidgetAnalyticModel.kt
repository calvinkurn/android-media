package com.tokopedia.play.widget.analytic.global.model

/**
 * Created by kenny.hadisaputra on 31/05/22
 */
interface PlayWidgetAnalyticModel {

    val prefix: String
    val category: String
    val promotionsItemName: String
    val promotionsCreativeName: String

    fun eventActionChannel(action: String): String {
        return action
    }
}
