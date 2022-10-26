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
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_ID -> {
                imagePolicy.toRequestParam(data.productId)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.NEW_PRODUCT_PRICE -> {
                imagePolicy.toRequestParam(data.newProductPrice.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.HAS_CAMPAIGN -> {
                imagePolicy.toRequestParam(data.hasCampaign.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.CAMPAIGN_DISCOUNT -> {
                imagePolicy.toRequestParam(data.campaignDiscount.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.CAMPAIGN_NAME_PDP-> {
                imagePolicy.toRequestParam(data.campaignName)
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
