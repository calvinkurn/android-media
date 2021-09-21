package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 03/03/21
 */

object WidgetDividerUiModel : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}