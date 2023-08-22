package com.tokopedia.stories.uimodel

/**
 * @author by astidhiyaa on 21/08/23
 */
enum class StoriesCampaignStatus (val value: String) {
    Upcoming("upcoming"),
    Ongoing("ongoing"),
    Unknown("");

    companion object {
        fun convertValue(value: String): StoriesCampaignStatus {
            return StoriesCampaignStatus.values()
                .firstOrNull { it.value == value } ?: Unknown
        }
    }
}

enum class StoriesCampaignType (val value: String) {
    ASGCFlashSale("asgc_flash_sale_toko"),
    RilisanSpesial("Rilisan Spesial"),
    ASGCRilisanSpesial("asgc_rilisan_spesial"),
    FlashSaleToko("Flash Sale Toko"),
    Unknown("");

    companion object {
        fun convertValue(value: String): StoriesCampaignType {
            return StoriesCampaignType.values()
                .firstOrNull { it.value == value } ?: StoriesCampaignType.Unknown
        }
    }
}
