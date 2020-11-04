package com.tokopedia.home.beranda.listener

import com.tokopedia.home.beranda.domain.interactor.GetRechargeBUWidgetUseCase
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePerso
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargePersoItem

interface RechargeBUWidgetListener {
    fun onRechargeBUWidgetClickMore(rechargePerso: RechargePerso)
    fun onRechargeBUWidgetItemClick(rechargePersoItem: RechargePersoItem)
    fun onRechargeBUWidgetImpression(rechargePerso: RechargePerso)
    fun getRechargeBUWidget(source: GetRechargeBUWidgetUseCase.WidgetSource)
}