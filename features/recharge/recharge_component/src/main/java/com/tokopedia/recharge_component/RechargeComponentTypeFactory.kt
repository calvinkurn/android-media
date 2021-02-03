package com.tokopedia.recharge_component

import com.tokopedia.recharge_component.model.RechargeBUWidgetDataModel

interface RechargeComponentTypeFactory {
    fun type(rechargeBUWidgetDataModel: RechargeBUWidgetDataModel): Int
}