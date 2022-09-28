package com.tokopedia.universal_sharing.model

import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants

fun ImagePolicyResponse.generateToPDP(
    isBebasOngkir: String,
    bebasOngkirType: String,
    productImageUrl: String,
    productPrice: String,
    productRating: String,
    productTitle: String,
    platform: String
): ArrayList<ImageGeneratorRequestData> {
    return ArrayList(response.map { imagePolicy ->
        when (imagePolicy.key) {
            ImageGeneratorConstants.ImageGeneratorKeys.IS_BEBAS_ONGKIR -> {
                imagePolicy.toRequestParam(isBebasOngkir)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.BEBAS_ONGKIR_TYPE -> {
                imagePolicy.toRequestParam(bebasOngkirType)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PLATFORM -> {
                imagePolicy.toRequestParam(productImageUrl)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_TITLE -> {
                imagePolicy.toRequestParam(productPrice)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_RATING -> {
                imagePolicy.toRequestParam(productRating)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE -> {
                imagePolicy.toRequestParam(productTitle)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_URL -> {
                imagePolicy.toRequestParam(platform)
            }
            else -> {
                imagePolicy.toRequestParam("")
            }

        }
    })
}
