package com.tokopedia.power_merchant.subscribe.view.model

import com.tokopedia.power_merchant.subscribe.view.adapter.WidgetAdapterFactory

/**
 * Created By @ilhamsuaib on 27/03/21
 */

object WidgetLoadingStateUiModel : BaseWidgetUiModel {

    override fun type(typeFactory: WidgetAdapterFactory): Int {
        return typeFactory.type(this)
    }
}