package com.tokopedia.universal_sharing.model

import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants

private fun ImagePolicyResponse.generateToPDP(
    data: PdpParamModel
): ArrayList<ImageGeneratorRequestData> {
    return ArrayList(
        response.args.map { imagePolicy ->
            when (imagePolicy.key) {
                ImageGeneratorConstants.ImageGeneratorKeys.IS_BEBAS_ONGKIR -> {
                    imagePolicy.toRequestParam(data.isBebasOngkir.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.BEBAS_ONGKIR_TYPE -> {
                    imagePolicy.toRequestParam(data.bebasOngkirType)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_TITLE -> {
                    imagePolicy.toRequestParam(data.productTitle)
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
                    imagePolicy.toRequestParam(data.hasCampaign)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.CAMPAIGN_DISCOUNT -> {
                    imagePolicy.toRequestParam(data.campaignDiscount.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.CAMPAIGN_NAME_PDP -> {
                    imagePolicy.toRequestParam(data.campaignName)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PERSONALIZED_CAMPAIGN_INFO -> {
                    imagePolicy.toRequestParam(data.campaignInfo)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.HAS_RIBBON -> {
                    imagePolicy.toRequestParam(data.hasRibbon.toString())
                }
                else -> {
                    imagePolicy.toRequestParam("")
                }
            }
        }
    )
}

private fun ImagePolicyResponse.generateToWishlist(
    data: WishlistCollectionParamModel
): ArrayList<ImageGeneratorRequestData> {
    return ArrayList(
        response.args.map { imagePolicy ->
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
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_6 -> {
                    imagePolicy.toRequestParam(data.productImage6)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_6 -> {
                    imagePolicy.toRequestParam(data.productPrice6.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PLATFORM -> {
                    imagePolicy.toRequestParam(data.platform)
                }
                else -> {
                    imagePolicy.toRequestParam("")
                }
            }
        }
    )
}

private fun ImagePolicyResponse.generateToShopPage(
    data: ShopPageParamModel
): ArrayList<ImageGeneratorRequestData> {
    return ArrayList(
        response.args.map { imagePolicy ->
            when (imagePolicy.key) {
                ImageGeneratorConstants.ImageGeneratorKeys.PLATFORM -> {
                    imagePolicy.toRequestParam(data.platform)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_PROFILE_IMAGE -> {
                    imagePolicy.toRequestParam(data.shopProfileImgUrl)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.IS_PROFILE_IMAGE_EXIST -> {
                    imagePolicy.toRequestParam(data.isProfileImageExist.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME_UNDERSCORE -> {
                    imagePolicy.toRequestParam(data.shopName)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_BADGE -> {
                    imagePolicy.toRequestParam(data.shopBadge)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOCATION -> {
                    imagePolicy.toRequestParam(data.shopLocation)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_TYPE_1 -> {
                    imagePolicy.toRequestParam(data.info1Type)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_LABEL_1 -> {
                    imagePolicy.toRequestParam(data.info1Label)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_VALUE_1 -> {
                    imagePolicy.toRequestParam(data.info1Value)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_TYPE_2 -> {
                    imagePolicy.toRequestParam(data.info2Type)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_LABEL_2 -> {
                    imagePolicy.toRequestParam(data.info2Label)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_VALUE_2 -> {
                    imagePolicy.toRequestParam(data.info2Value)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_TYPE_3 -> {
                    imagePolicy.toRequestParam(data.info3Type)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_LABEL_3 -> {
                    imagePolicy.toRequestParam(data.info3Label)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_INFO_VALUE_3 -> {
                    imagePolicy.toRequestParam(data.info3Value)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_COUNT -> {
                    imagePolicy.toRequestParam(data.productCount.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_1 -> {
                    imagePolicy.toRequestParam(data.productImage1)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_2 -> {
                    imagePolicy.toRequestParam(data.productImage2)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_3 -> {
                    imagePolicy.toRequestParam(data.productImage3)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_4 -> {
                    imagePolicy.toRequestParam(data.productImage4)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_5 -> {
                    imagePolicy.toRequestParam(data.productImage5)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_IMAGE_6 -> {
                    imagePolicy.toRequestParam(data.productImage6)
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_1 -> {
                    imagePolicy.toRequestParam(data.productPrice1.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_2 -> {
                    imagePolicy.toRequestParam(data.productPrice2.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_3 -> {
                    imagePolicy.toRequestParam(data.productPrice3.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_4 -> {
                    imagePolicy.toRequestParam(data.productPrice4.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_5 -> {
                    imagePolicy.toRequestParam(data.productPrice5.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_PRICE_6 -> {
                    imagePolicy.toRequestParam(data.productPrice6.toString())
                }
                ImageGeneratorConstants.ImageGeneratorKeys.SHOP_IS_HEADLESS -> {
                    imagePolicy.toRequestParam(data.isHeadless.toString())
                }
                else -> {
                    imagePolicy.toRequestParam("")
                }
            }
        }
    )
}

fun ImagePolicyResponse.generateImageGeneratorParam(data: ImageGeneratorParamModel): ArrayList<ImageGeneratorRequestData> {
    return when (data) {
        is PdpParamModel -> {
            this.generateToPDP(data)
        }
        is WishlistCollectionParamModel -> {
            this.generateToWishlist(data)
        }
        is ShopPageParamModel -> {
            this.generateToShopPage(data)
        }
        else -> {
            throw Exception("model is not ImageGeneratorParamModel type")
        }
    }
}
