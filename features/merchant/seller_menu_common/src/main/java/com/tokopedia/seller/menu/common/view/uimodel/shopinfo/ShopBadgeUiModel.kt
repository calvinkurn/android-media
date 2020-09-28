package com.tokopedia.seller.menu.common.view.uimodel.shopinfo

import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoClickTrackable
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingShopInfoImpressionTrackable

data class ShopBadgeUiModel(
    val shopBadgeUrl: String? = "",
    override val impressionEventName: String = "",
    override val impressionEventCategory: String = "",
    override val impressionEventAction: String = "",
    override val clickEventAction: String = "${SettingTrackingConstant.CLICK} ${SettingTrackingConstant.SHOP_BADGE}"
) : SettingShopInfoImpressionTrackable, SettingShopInfoClickTrackable
