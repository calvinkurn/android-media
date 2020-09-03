package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.sellerhomecommon.presentation.adapter.WidgetAdapterFactory
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created By @ilhamsuaib on 19/05/20
 */

interface BaseWidgetUiModel<T : BaseDataUiModel> : Visitable<WidgetAdapterFactory> {
    val widgetType: String
    val title: String
    val subtitle: String
    val tooltip: TooltipUiModel?
    val url: String
    val appLink: String
    val dataKey: String
    val ctaText: String
    var data: T?
    val impressHolder: ImpressHolder
    var isLoaded: Boolean
}