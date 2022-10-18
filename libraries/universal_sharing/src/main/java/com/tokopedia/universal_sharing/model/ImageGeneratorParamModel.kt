package com.tokopedia.universal_sharing.model

abstract class ImageGeneratorParamModel(var platform: String = "wa")

data class PdpParamModel(
    val isBebasOngkir: Boolean = false,
    val bebasOngkirType: String = "",
    val productImageUrl: String = "",
    val productPrice: Long = 0,
    val productRating: Float = 0f,
    val productTitle: String = "",
) : ImageGeneratorParamModel()

data class ShopPageParamModel(
    val shopProfileImgUrl: String = "",
    val isProfileImageExist: Boolean = false,
    val shopName: String = "",
    val shopBadge: String = "",
    val shopLocation: String = "",
    val info1Type: String,
    val info1Value: String = "",
    val info1Label: String = "",
    val info2Type: String = "",
    val info2Value: String = "",
    val info2Label: String = "",
    val info3Type: String = "",
    val info3Value: String = "",
    val info3Label: String = "",
    val productCount: Int = 0,
    val productImage1: String = "",
    val productPrice1: Long = 0,
    val productImage2: String = "",
    val productPrice2: Long = 0,
    val productImage3: String = "",
    val productPrice3: Long = 0,
    val productImage4: String = "",
    val productPrice4: Long = 0,
    val productImage5: String = "",
    val productPrice5: Long = 0,
    val productImage6: String = "",
    val productPrice6: Long = 0,
): ImageGeneratorParamModel()

enum class BoTypeImageGeneratorParam(val value: String) {
    NONE("none"),
    BEBAS_ONGKIR("normal"),
    BEBAS_ONGKIR_EXTRA("extra"),
    BEBAS_ONGKIR_10Rb("10k")
}



