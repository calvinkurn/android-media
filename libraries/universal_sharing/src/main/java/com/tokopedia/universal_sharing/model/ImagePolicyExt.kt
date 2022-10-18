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

private fun ImagePolicyResponse.generateToWishlist(
    data: WishlistCollectionParamModel
): ArrayList<ImageGeneratorRequestData> {
    return ArrayList(response.args.map { imagePolicy ->
        when (imagePolicy.key) {
            ImageGeneratorConstants.ImageGeneratorKeys.COLLECTION_NAME -> {
                imagePolicy.toRequestParam(data.collectionName.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.COLLECTION_OWNER -> {
                imagePolicy.toRequestParam(data.collectionOwner)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_COUNT -> {
                imagePolicy.toRequestParam(data.productCount.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_1 -> {
                imagePolicy.toRequestParam(data.productImage1)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_1 -> {
                imagePolicy.toRequestParam(data.productPrice1.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_2 -> {
                imagePolicy.toRequestParam(data.productImage2)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_2 -> {
                imagePolicy.toRequestParam(data.productPrice2.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_3 -> {
                imagePolicy.toRequestParam(data.productImage3)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_3 -> {
                imagePolicy.toRequestParam(data.productPrice3.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_4 -> {
                imagePolicy.toRequestParam(data.productImage4)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_4 -> {
                imagePolicy.toRequestParam(data.productPrice4.toString())
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_5 -> {
                imagePolicy.toRequestParam(data.productImage5)
            }
            ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_5 -> {
                imagePolicy.toRequestParam(data.productPrice5.toString())
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
        }
        is WishlistCollectionParamModel -> {
            this.generateToWishlist(data)
        }
        else -> {
            throw Exception("model is not ImageGeneratorParamModel type")
        }
    }
}
