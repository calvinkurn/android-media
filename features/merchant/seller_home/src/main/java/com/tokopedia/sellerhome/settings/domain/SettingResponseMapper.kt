package com.tokopedia.sellerhome.settings.domain

import com.tokopedia.sellerhome.settings.domain.entity.ReputationShopsResult
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