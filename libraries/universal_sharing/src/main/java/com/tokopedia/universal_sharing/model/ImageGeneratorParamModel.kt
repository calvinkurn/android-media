package com.tokopedia.universal_sharing.model

abstract class ImageGeneratorParamModel(var platform: String = "wa")

data class PdpParamModel(
    val productId: String = "",
    val isBebasOngkir: Boolean = false,
    val bebasOngkirType: String = "",
    val productImageUrl: String = "",
    val productPrice: Long = 0,
    val productRating: Float = 0f,
    val productTitle: String = "",
    val hasCampaign: Boolean = false,
    val campaignName: String = "",
    val campaignDiscount: Int = 0,
    val newProductPrice: Int = 0
) : ImageGeneratorParamModel()

enum class BoTypeImageGeneratorParam(val value: String) {
    NONE("none"),
    BEBAS_ONGKIR("normal"),
    BEBAS_ONGKIR_EXTRA("extra"),
    BEBAS_ONGKIR_10Rb("10k")
}



