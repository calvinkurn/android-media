package com.tokopedia.affiliate

const val AFFILIATE_LOGIN_REQUEST_CODE = 1023
const val AFFILIATE_REGISTER_REQUEST_CODE = 1024
const val AFFILIATE_HELP_URL = "https://affiliate.tokopedia.com/help"
const val AFFILIATE_LIHAT_KATEGORI = "tokopedia://affiliate/help"
const val PAGE_ZERO = 0
const val PAGE_SEGMENT_HELP = "help"

const val ANNOUNCEMENT__TYPE_NO_ANNOUNCEMENT = "noAnnouncement"
const val ANNOUNCEMENT__TYPE_CCA = "cca"
const val ANNOUNCEMENT__TYPE_SERVICE_STATUS = "serviceStatus"
const val ANNOUNCEMENT__TYPE_USER_BLACKLIST = "userBlacklisted"
const val ANNOUNCEMENT__TYPE_SUCCESS = 1
val PERFORMA_MAP = mapOf(
    "Pendapatan" to "Jumlah komisi yang kamu dapatkan ketika ada orang lain membeli produk dari link affiliate kamu."
    , "Klik" to "Jumlah orang yang klik link affiliate kamu."
    , "Terjual" to "Total penjualan produk dari link affiliate kamu."
    , "Konversi" to "Total penjualan produk dari link affiliate kamu.")