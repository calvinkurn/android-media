package com.tokopedia.recharge_component.listener

import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.RechargePersoItem
import com.tokopedia.recharge_component.model.WidgetSource

interface RechargeBUWidgetListener {
    fun onRechargeBUWidgetClickMore(rechargePerso: RechargePerso)
    fun onRechargeBUWidgetItemClick(rechargePersoItem: RechargePersoItem)
    fun onRechargeBUWidgetImpression(rechargePerso: RechargePerso)
    fun getRechargeBUWidget(source: WidgetSource)
}