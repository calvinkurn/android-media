package com.tokopedia.topads.sdk.utils

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.TopAdsConstants
import com.tokopedia.topads.sdk.domain.model.LabelGroup
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.unifycomponents.UnifyButton

object MapperUtils {
    fun getProductCardModels(products: List<Product>, hasAddProductToCartButton: Boolean): ArrayList<ProductCardModel> {
        return ArrayList<ProductCardModel>().apply {
            products.map {
                add(getProductCardViewModel(it, hasAddProductToCartButton))
            }
        }
    }

    fun getProductCardViewModel(product: Product, hasAddProductToCartButton: Boolean): ProductCardModel {
        val isAvailAble = checkIfDTAvailable(product.labelGroupList)
        val productCardModel = ProductCardModel(
            productImageUrl = product.imageProduct.imageUrl,
            productName = product.name,
            discountPercentage = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
            formattedPrice = product.priceFormat,
            reviewCount = product.countReviewFormat.toIntOrZero(),
            ratingString = product.productRatingFormat,
            freeOngkir = ProductCardModel.FreeOngkir(
                product.freeOngkir.isActive,
                product.freeOngkir.imageUrl
            ),
            hasAddToCartButton = hasAddProductToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN,
            stockBarPercentage = product.stock_info.soldStockPercentage,
            stockBarLabel = product.stock_info.stockWording,
            stockBarLabelColor = product.stock_info.stockColour,
            shopBadgeList = product.badges.map {
                ProductCardModel.ShopBadge(
                    imageUrl = it.imageUrl,
                    title = it.title,
                    isShown = it.isShow,
                )
            },
            isTopAds = true
        )
        return getProductModelOnCondition(product, isAvailAble, productCardModel)
    }

    fun getProductModelOnCondition(
        product: Product,
        isAvailAble: Boolean,
        productCardModel: ProductCardModel
    ): ProductCardModel {
        if (isAvailAble) {
            return if (!product.campaign.originalPrice.isNullOrEmpty()) {
                productCardModel.copy(
                    slashedPrice = product.campaign.originalPrice,
                    labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                        product.labelGroupList.map {
                            if (it.position != "integrity"){
                                add(
                                    ProductCardModel.LabelGroup(
                                        position = it.position,
                                        title = it.title,
                                        type = it.type,
                                        imageUrl = it.imageUrl,
                                        styleList = it.styleList.map { style ->
                                            ProductCardModel.LabelGroup.Style(style.key, style.value)
                                        },
                                    )
                                )
                            }
                        }
                    }
                )
            } else {
                productCardModel.copy(
                    labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                        product.labelGroupList.map {
                            if (it.position != "integrity") {
                                add(
                                    ProductCardModel.LabelGroup(
                                        position = it.position,
                                        title = it.title,
                                        type = it.type,
                                        imageUrl = it.imageUrl,
                                        styleList = it.styleList.map { style ->
                                            ProductCardModel.LabelGroup.Style(style.key, style.value)
                                        }
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }

        return productCardModel.copy(
            slashedPrice = product.campaign.originalPrice,
            countSoldRating = product.headlineProductRatingAverage,
            labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                product.labelGroupList.map {
                    add(
                        ProductCardModel.LabelGroup(
                            position = it.position,
                            title = it.title,
                            type = it.type,
                            imageUrl = it.imageUrl,
                            styleList = it.styleList.map { style ->
                                ProductCardModel.LabelGroup.Style(style.key, style.value)
                            }
                        )
                    )
                }
            }
        )
    }

    fun checkIfDTAvailable(labelGroupList: List<LabelGroup>): Boolean {
        var isAvailable = false
        run breaking@{
            labelGroupList.forEach {
                if (it.position == TopAdsConstants.FULFILLMENT && it.title == TopAdsConstants.DILYANI_TOKOPEDIA) {
                    isAvailable = true
                    return@breaking
                }
            }
        }
        return isAvailable
    }
}
