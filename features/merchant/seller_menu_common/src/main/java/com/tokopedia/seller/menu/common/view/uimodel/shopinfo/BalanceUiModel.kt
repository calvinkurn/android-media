package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.base.BalanceType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable

open class BalanceUiModel(balanceType: BalanceType,
                     val balanceValue: String = "",
                     override val impressionEventName: String = "",
                     override val impressionEventAction: String = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.ON_SALDO}")
    : SettingShopInfoClickTrackable, SettingShopInfoImpressionTrackable
{

    override val clickEventAction: String =
            when(balanceType) {
                BalanceType.SALDO -> "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.ON_SALDO}"
                BalanceType.TOPADS -> "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.ON_TOPADS_CREDIT}"
            }

}