package com.tokopedia.product.detail.view.viewholder.bmgm.widget

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel

/**
 * Created by yovi.putra on 27/07/23"
 * Project name: android-tokopedia-core
 **/

interface BMGMWidgetTracker {

    fun getImpressionHolder(): ImpressHolder

    fun onImpressed()

    fun onClick(data: BMGMWidgetUiModel)
}
