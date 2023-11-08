package com.tokopedia.shop.info.view.model

class ShopEpharmacyDetailData(
    val gMapsUrl: String = "",
    val address: String = "",
    val errorCode: Int = 0,
    val errMessages: List<String> = listOf(),
    val apj: String = "",
    val siaNumber: String = "",
    val sipaNumber: String = "",
    val epharmacyWorkingHoursFmt: List<String> = listOf()
)
