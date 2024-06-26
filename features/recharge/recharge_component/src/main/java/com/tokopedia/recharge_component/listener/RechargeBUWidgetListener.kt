package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel
import com.tokopedia.recharge_component.model.WidgetSource

interface RechargeBUWidgetListener {
    fun onRechargeBUWidgetImpression(data: RechargeBUWidgetDataModel)
    fun onRechargeBUWidgetBannerImpression(data: RechargeBUWidgetDataModel)
    fun onRechargeBUWidgetClickSeeAllButton(data: RechargeBUWidgetDataModel)
    fun onRechargeBUWidgetClickSeeAllCard(data: RechargeBUWidgetDataModel)
    fun onRechargeBUWidgetClickBanner(data: RechargeBUWidgetDataModel)
    fun onRechargeBUWidgetItemClick(data: RechargeBUWidgetDataModel, position: Int)
    fun getRechargeBUWidget(source: WidgetSource)
    fun onRechargeBUWidgetProductCardImpression(data: RechargeBUWidgetDataModel, position: Int)
}