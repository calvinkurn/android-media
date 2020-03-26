package com.tokopedia.sellerhome.settings.domain

import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.sellerhome.settings.domain.entity.ReputationShopsResult
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import com.tokopedia.sellerhome.settings.view.uimodel.shopinfo.SettingShopInfoUiModel
import java.text.NumberFormat
import java.util.*

fun ReputationShopsResult.mapReputationToBadgeUrl() : String {
    with(reputationShops) {
        return if (isNotEmpty()) get(0).badge
        else ""
    }
}

fun Any?.toDecimalRupiahCurrency(): String {
    val localeIndonesia = Locale("in", "ID")
    val numberFormatter = NumberFormat.getCurrencyInstance(localeIndonesia)
    return numberFormatter.format(this)
}

fun mapToSettingShopInfo(shopInfo: ShopInfo,
                         shopStatusType: ShopType,
                         topAdsBalance: Float,
                         isTopAdsAutoTopup: Boolean,
                         totalFollowers: Int,
                         shopBadge: String): SettingShopInfoUiModel {
    shopInfo.shopInfoMoengage?.run {
        return SettingShopInfoUiModel(
                info?.shopName.toEmptyStringIfNull(),
                info?.shopAvatar.toEmptyStringIfNull(),
                shopStatusType,
                shopInfo.balance?.sellerBalance ?: "",
                topAdsBalance.toDecimalRupiahCurrency(),
                isTopAdsAutoTopup,
                shopBadge,
                totalFollowers)
    }
    return SettingShopInfoUiModel()
}