package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable

data class ShopAvatarUiModel(
    val shopAvatarUrl: String,
    override val impressionEventAction: String = "${SettingTrackingConstant.IMPRESSION} ${SettingTrackingConstant.SHOP_PICTURE}",
    override val clickEventAction: String = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_PICTURE}"
) : SettingShopInfoImpressionTrackable, SettingShopInfoClickTrackable