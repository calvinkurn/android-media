package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.base.*
import com.tokopedia.user.session.UserSessionInterface

class ShopStatusUiModel(val shopType: ShopType,
                        val user: UserSessionInterface?,
                        val thematicIllustrationUrl: String = "") :
        SettingShopInfoImpressionTrackable,
    SettingShopInfoClickTrackable
{

    override val impressionEventAction: String
        get() = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.SHOP_STATE} - ${shopType.mapToEventCategory()}"

    override val clickEventAction: String
        get() = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_STATE} - ${shopType.mapToEventCategory()}"

    override val clickEventShopId: String
        get() = user?.shopId.toString()

    override val clickEventUserId: String
        get() = user?.userId.toString()

    override val clickEventShopType: String
        get() = when(shopType) {
            is RegularMerchant -> SettingTrackingConstant.SHOP_REGULAR_MERCHANT
            is PowerMerchantStatus -> SettingTrackingConstant.SHOP_POWER_MERCHANT
            is ShopType.OfficialStore -> SettingTrackingConstant.SHOP_OFFICIAL_STORE
        }

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