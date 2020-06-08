package com.tokopedia.sellerhome.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

interface BaseWidgetUiModel<T: BaseDataUiModel> : Visitable<SellerHomeAdapterTypeFactory> {
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