package com.tokopedia.home_component.model

data class TrackingAttributionModel(
        val galaxyAttribution: String = "",
        val persona: String = "",
        val brandId: String = "",
        val categoryPersona: String = "",
        val categoryId: String = "",
        val persoType: String = "",
        val campaignCode: String = "",
        val homeAttribution: String = "",
        val campaignId: String = ""
) {
    val promoName: String = ""
}