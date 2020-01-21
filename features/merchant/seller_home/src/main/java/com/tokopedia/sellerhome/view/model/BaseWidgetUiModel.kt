package com.tokopedia.sellerhome.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.view.adapter.SellerHomeAdapterTypeFactory

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

interface BaseWidgetUiModel : Visitable<SellerHomeAdapterTypeFactory> {

    val widgetType: String
    val title: String
    val url: String
    val appLink: String
}