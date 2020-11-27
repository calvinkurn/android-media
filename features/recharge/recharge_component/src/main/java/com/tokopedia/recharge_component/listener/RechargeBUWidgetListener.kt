package com.tokopedia.recharge_component.listener

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.recharge_component.model.RechargePerso
import com.tokopedia.recharge_component.model.RechargePersoItem
import com.tokopedia.recharge_component.model.WidgetSource

interface RechargeBUWidgetListener {
    fun onRechargeBUWidgetImpression(rechargePerso: RechargePerso, channelModel: ChannelModel)
    fun onRechargeBUWidgetBannerImpression(rechargePerso: RechargePerso, channelModel: ChannelModel)
    fun onRechargeBUWidgetClickSeeAllButton(rechargePerso: RechargePerso, channelModel: ChannelModel)
    fun onRechargeBUWidgetClickSeeAllCard(rechargePerso: RechargePerso, channelModel: ChannelModel)
    fun onRechargeBUWidgetClickBanner(rechargePerso: RechargePerso, channelModel: ChannelModel)
    fun onRechargeBUWidgetItemClick(rechargePerso: RechargePerso, position: Int, channelModel: ChannelModel)
    fun getRechargeBUWidget(source: WidgetSource)
}