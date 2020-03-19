package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.BalanceType
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingShopInfoImpressionTrackable

class BalanceUiModel(val balanceType: BalanceType,
                     val balanceValue: String = "",
                     override val impressionEventName: String = "",
                     override val impressionEventCategory: String = "",
                     override val impressionEventAction: String = "",
                     override val impressionEventLabel: String = "",
                     override val clickEventLabel: String = "",
                     override val impressHolder: ImpressHolder = ImpressHolder())
    : SettingShopInfoClickTrackable, SettingShopInfoImpressionTrackable
{

    override val clickEventAction: String =
            when(balanceType) {
                BalanceType.SALDO -> "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.ON_SALDO}"
                BalanceType.TOPADS -> "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.ON_TOPADS_CREDIT}"
            }

}