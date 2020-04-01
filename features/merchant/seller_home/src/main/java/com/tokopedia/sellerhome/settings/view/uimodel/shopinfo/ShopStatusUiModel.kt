package com.tokopedia.sellerhome.settings.view.uimodel.shopinfo

import com.tokopedia.sellerhome.settings.analytics.SettingTrackingConstant
import com.tokopedia.sellerhome.settings.view.uimodel.base.*

class ShopStatusUiModel(val shopType: ShopType) :
        SettingShopInfoImpressionTrackable,
        SettingShopInfoClickTrackable
{

    override val impressionEventAction: String
        get() = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.SHOP_STATE} - ${shopType.mapToEventCategory()}"

    override val clickEventAction: String
        get() = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_STATE} - ${shopType.mapToEventCategory()}"

    private fun ShopType.mapToEventCategory(): String {
        val shopType = when(this) {
            is PowerMerchantStatus -> SettingTrackingConstant.GOLD_MERCHANT
            is RegularMerchant -> SettingTrackingConstant.REGULAR
            is ShopType.OfficialStore -> SettingTrackingConstant.OFFICIAL_STORE
        }
        val shopStatus = when(this) {
            is PowerMerchantStatus.Active -> SettingTrackingConstant.ACTIVE
            is PowerMerchantStatus.NotActive -> SettingTrackingConstant.INACTIVE
            is RegularMerchant.NeedUpgrade -> SettingTrackingConstant.UPGRADE
            is RegularMerchant.NeedVerification -> SettingTrackingConstant.VERIFICATION
            is PowerMerchantStatus.OnVerification -> SettingTrackingConstant.ON_VERIFICATION
            else -> ""
        }
        return "$shopType $shopStatus"
    }
}