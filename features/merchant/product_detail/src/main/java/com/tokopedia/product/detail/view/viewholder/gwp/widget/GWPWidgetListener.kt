package com.tokopedia.product.detail.view.viewholder.gwp.widget

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

interface GWPWidgetListener {
    fun getRecyclerViewPool(): RecyclerView.RecycledViewPool?

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
