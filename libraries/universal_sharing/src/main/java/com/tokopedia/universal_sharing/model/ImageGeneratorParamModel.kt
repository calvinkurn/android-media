package com.tokopedia.universal_sharing.model

import android.annotation.SuppressLint

abstract class ImageGeneratorParamModel(var platform: String = "wa")

data class PdpParamModel(
    val productId: String = "",
    val isBebasOngkir: Boolean = false,
    val bebasOngkirType: String = "",
    var productImageUrl: String = "",
    val productPrice: Long = 0,
    val productRating: Float = 0f,
    val productTitle: String = "",
    val hasCampaign: String = "0",
    val campaignName: String = "",
    val campaignDiscount: Int = 0,
    val newProductPrice: Long = 0
) : ImageGeneratorParamModel()

//this data class can be ignored for ParamFieldAnnotation, since this class is being used to map the request on ImagePolicyResponse.generateToShopPage function
@SuppressLint("ParamFieldAnnotation")
data class ShopPageParamModel(
    var shopProfileImgUrl: String = "",
    var isProfileImageExist: Boolean = true,
    var shopName: String = "",
    var shopBadge: String = "",
    var shopLocation: String = "",
    var info1Type: String = "",
    var info1Value: String = "",
    var info1Label: String = "",
    var info2Type: String = "",
    var info2Value: String = "",
    var info2Label: String = "",
    var info3Type: String = "",
    var info3Value: String = "",
    var info3Label: String = "",
    var productCount: Int = 0,
    var productImage1: String = "",
    var productPrice1: Long = 0,
    var productImage2: String = "",
    var productPrice2: Long = 0,
    var productImage3: String = "",
    var productPrice3: Long = 0,
    var productImage4: String = "",
    var productPrice4: Long = 0,
    var productImage5: String = "",
    var productPrice5: Long = 0,
    var productImage6: String = "",
    var productPrice6: Long = 0,
    var isHeadless: Boolean = false
): ImageGeneratorParamModel() {
    enum class ShopInfoType(val typeName: String = "") {
        SHOP_PERFORMANCE("shop_performance"),
        BADGE_TEXT("badge_text_value"),
        IMAGE_ONLY("image_only")
    }
    enum class ShopInfoName(val infoName: String = "", val infoNameValue: String = "") {
        SHOP_RATING("shop_rating", "rating"),
        FREE_SHIPPING("bebas_ongkir", "bo"),
        FREE_TEXT(infoNameValue = "free-text")
    }
    enum class ShopTier(val tierId: String) {
        OFFICIAL_STORE("os"),
        POWER_MERCHANT("pm"),
        POWER_MERCHANT_PRO("pro"),
        REGULAR("regular")
    }
    enum class ShopPerformanceLabel(val labelName: String) {
        OPERATIONAL_HOURS("Jam operasi toko")
    }
}

enum class BoTypeImageGeneratorParam(val value: String) {
    NONE("none"),
    BEBAS_ONGKIR("normal"),
    BEBAS_ONGKIR_EXTRA("extra"),
    BEBAS_ONGKIR_10Rb("10k")
}
