package com.tokopedia.sellerhome.settings.domain

import com.tokopedia.sellerhome.settings.domain.entity.ReputationShopsResult

fun ReputationShopsResult.mapReputationToBadgeUrl() : String {
    with(reputationShops) {
        return if (isNotEmpty()) get(0).badge
        else ""
    }
}