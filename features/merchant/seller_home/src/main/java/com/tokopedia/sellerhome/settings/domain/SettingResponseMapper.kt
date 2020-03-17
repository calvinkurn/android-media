package com.tokopedia.sellerhome.settings.domain

import com.tokopedia.sellerhome.settings.domain.entity.Owner
import com.tokopedia.sellerhome.settings.domain.entity.ReputationShopsResult
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

fun Owner.getShopStatusType(): ShopType {
    isGoldMerchant?.let {
        return if (it) ShopType.OfficialStore
        else when(pmStatus) {
            Owner.STATUS_INACTIVE -> RegularMerchant.NeedUpgrade
            Owner.STATUS_ACTIVE -> PowerMerchantStatus.Active
            Owner.STATUS_IDLE -> PowerMerchantStatus.NotActive
            Owner.STATUS_PENDING -> PowerMerchantStatus.OnVerification
            else -> RegularMerchant.NeedVerification
        }
    }
    return RegularMerchant.NeedUpgrade
}

fun Any?.toDecimalRupiahCurrency(): String {
    val localeIndonesia = Locale("in", "ID")
    val numberFormatter = NumberFormat.getCurrencyInstance(localeIndonesia)
    return numberFormatter.format(this)
}