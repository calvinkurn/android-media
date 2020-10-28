package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.SerializedName

data class ReputationShopsResult(
        @SerializedName("reputation_shops")
        val reputationShops : List<ReputationShop> = listOf()
)

data class ReputationShop(
        @SerializedName("badge_hd")
        val badge: String = ""
)