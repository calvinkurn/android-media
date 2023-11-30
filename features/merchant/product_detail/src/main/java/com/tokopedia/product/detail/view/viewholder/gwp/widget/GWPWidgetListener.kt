package com.tokopedia.product.detail.view.viewholder.gwp.widget

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.componentization.ComponentEvent

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

interface GWPWidgetListener {

    fun getRecyclerViewPool(): RecyclerView.RecycledViewPool?

    fun getImpressionHolder(): ImpressHolder?

    fun getImpressionHolders(): MutableList<String>

    fun onImpressed()

    fun isRemoteCacheableActive(): Boolean

    fun event(event: ComponentEvent)
}
