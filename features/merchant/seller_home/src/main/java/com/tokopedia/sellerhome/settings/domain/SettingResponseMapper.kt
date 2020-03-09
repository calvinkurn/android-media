package com.tokopedia.sellerhome.settings.domain

import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.sellerhome.settings.domain.entity.Owner
import com.tokopedia.sellerhome.settings.domain.entity.ReputationShopsResult
import com.tokopedia.sellerhome.settings.domain.entity.ShopInfo
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.RegularMerchant
import com.tokopedia.sellerhome.settings.view.uimodel.base.ShopType
import java.text.NumberFormat
import java.util.*

fun ReputationShopsResult.mapReputationToBadgeUrl() : String {
    with(reputationShops) {
        return if (isNotEmpty()) get(0).badge
        else ""
    }
}

fun ShopInfo.mapToGeneralShopInfo(): GeneralShopInfoUiModel {
    shopInfoMoengage?.run {
        val shopName = info?.shopName
        val shopAvatar = info?.shopAvatar
        val shopType = owner?.getShopStatusType()?: RegularMerchant.NeedUpdate
        return GeneralShopInfoUiModel(
                shopName.toEmptyStringIfNull(),
                shopAvatar.toEmptyStringIfNull(),
                shopType,
                balance?.sellerBalance.toDecimalRupiahCurrency(),
                topadsDeposit.topadsAmount.toDecimalRupiahCurrency())
    }
    return GeneralShopInfoUiModel()
}

fun Owner.getShopStatusType(): ShopType {
    isGoldMerchant?.let {
        return if (it) ShopType.OfficialStore
        else when(pmStatus) {
            Owner.STATUS_INACTIVE -> RegularMerchant.NeedUpdate
            Owner.STATUS_ACTIVE -> PowerMerchantStatus.Active
            Owner.STATUS_IDLE -> PowerMerchantStatus.NotActive
            Owner.STATUS_PENDING -> PowerMerchantStatus.OnVerification
            else -> RegularMerchant.OnVerification
        }
    }
    return RegularMerchant.NeedUpdate
}

fun Any?.toDecimalRupiahCurrency(): String {
    val localeIndonesia = Locale("in", "ID")
    val numberFormatter = NumberFormat.getCurrencyInstance(localeIndonesia)
    return numberFormatter.format(this)
}