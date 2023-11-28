package com.tokopedia.product.detail.view.viewholder.gwp.widget

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

interface GWPWidgetRouter {
    fun goToAppLink(appLink: String)

    fun goToWebView(link: String)
}

interface GWPWidgetTracker {

    fun getImpressionHolder(): ImpressHolder

    fun getImpressionHolders(): MutableList<String>

    fun onImpressed()

    fun isCacheable(): Boolean

    fun onClickTracking(data: GWPWidgetUiModel)
}
