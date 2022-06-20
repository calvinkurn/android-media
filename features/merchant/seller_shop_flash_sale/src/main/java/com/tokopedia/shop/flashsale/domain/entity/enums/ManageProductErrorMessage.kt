package com.tokopedia.shop.flashsale.domain.entity.enums

const val ERROR_MSG_MAX_CAMPAIGN_DISCOUNTED_PRICE = "Harga campaign Maks. "
const val ERROR_MSG_MAX_CAMPAIGN_STOCK = "Stok campaign Maks. "
const val ERROR_MSG_MIN_CAMPAIGN_DISCOUNTED_PRICE = "Harga campaign Min. Rp 100"
const val ERROR_MSG_MIN_CAMPAIGN_STOCK = "Stok campaign Min. 1"
const val ERROR_MSG_MAX_CAMPAIGN_ORDER = "Maksimum pembelian tidak boleh melebihi stok utama"
const val ERROR_MSG_OTHER = " dan terjadi kesalahan lainnya"

enum class ManageProductErrorMessage(val errorMsg: String) {
    MAX_CAMPAIGN_DISCOUNTED_PRICE(ERROR_MSG_MAX_CAMPAIGN_DISCOUNTED_PRICE),
    MAX_CAMPAIGN_STOCK(ERROR_MSG_MAX_CAMPAIGN_STOCK),
    MIN_CAMPAIGN_DISCOUNTED_PRICE(ERROR_MSG_MIN_CAMPAIGN_DISCOUNTED_PRICE),
    MIN_CAMPAIGN_STOCK(ERROR_MSG_MIN_CAMPAIGN_STOCK),
    MAX_CAMPAIGN_ORDER(ERROR_MSG_MAX_CAMPAIGN_ORDER),
    OTHER(ERROR_MSG_OTHER)
}