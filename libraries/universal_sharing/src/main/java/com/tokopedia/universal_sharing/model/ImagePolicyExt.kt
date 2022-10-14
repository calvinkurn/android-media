package com.tokopedia.universal_sharing.model

import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants

private fun ImagePolicyResponse.generateToPDP(
    data: PdpParamModel
): ArrayList<ImageGeneratorRequestData> {
    return ArrayList(response.args.map { imagePolicy ->
        when (imagePolicy.key) {
            ImageGeneratorConstants.ImageGeneratorKeys.IS_BEBAS_ONGKIR -> {
                imagePolicy.toRequestParam(data.isBebasOngkir.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.BEBAS_ONGKIR_TYPE -> {
                imagePolicy.toRequestParam(data.bebasOngkirType)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_TITLE -> {
                imagePolicy.toRequestParam(data.productPrice.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_RATING -> {
                imagePolicy.toRequestParam(data.productRating.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE -> {
                imagePolicy.toRequestParam(data.productPrice.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_URL -> {
                imagePolicy.toRequestParam(data.productImageUrl)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PLATFORM -> {
                imagePolicy.toRequestParam(data.platform)
            }
            else -> {
                imagePolicy.toRequestParam("")
            }

        }
    })
}

fun ImagePolicyResponse.generateImageGeneratorParam(data: ImageGeneratorParamModel): ArrayList<ImageGeneratorRequestData> {
    return when (data) {
        is PdpParamModel -> {
            this.generateToPDP(data)
        } else -> {
            throw Exception("model is not ImageGeneratorParamModel type")
        }
    }
}
