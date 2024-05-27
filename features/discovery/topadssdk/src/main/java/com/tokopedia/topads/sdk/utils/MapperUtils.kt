package com.tokopedia.topads.sdk.utils

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.reimagine.LABEL_REIMAGINE_CREDIBILITY
import com.tokopedia.productcard.reimagine.LabelGroupStyle
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants
import com.tokopedia.topads.sdk.domain.model.LabelGroup
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.unifycomponents.R.color
import com.tokopedia.unifycomponents.UnifyButton
import java.util.*

object MapperUtils {

    private const val RATING_SCALE = 100.0
    private const val MAX_RATING = 5.0
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

        val labelGroupList = mappingLabelGroupList(product)
        if (isAvailAble) {
            return if (!product.campaign.originalPrice.isNullOrEmpty()) {
                productCardModel.copy(
                    slashedPrice = product.campaign.originalPrice,
                    labelGroupList = labelGroupList
                )
            } else {
                productCardModel.copy(
                    labelGroupList = labelGroupList
                )
            }
        }
        return productCardModel.copy(
            slashedPrice = product.campaign.originalPrice,
            countSoldRating = product.headlineProductRatingAverage.ifEmpty {
                if (product.productRating > 0) {
                    convertRatingScaleToString(product.productRating)
                } else {
                    String.EMPTY
                }
            },
            labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                product.labelGroupList.map {
                    if (it.position == "integrity") {
                        add(
                            ProductCardModel.LabelGroup(
                                position = LABEL_REIMAGINE_CREDIBILITY,
                                title = it.title,
                                type = it.type,
                                imageUrl = it.imageUrl,
                                styleList = listOf(),
                            )
                        )
                    } else add(
                        ProductCardModel.LabelGroup(
                            position = it.position,
                            title = it.title,
                            type = it.type,
                            imageUrl = it.imageUrl,
                            styleList = listOf(
                                ProductCardModel.LabelGroup.Style(
                                    key = LabelGroupStyle.BACKGROUND_COLOR,
                                    value = color.Unify_Static_Black.toString()
                                ),
                                ProductCardModel.LabelGroup.Style(
                                    key = LabelGroupStyle.BACKGROUND_OPACITY,
                                    value = LabelGroupStyle.BACKGROUND_OPACITY_VALUE
                                ),
                                ProductCardModel.LabelGroup.Style(
                                    key = LabelGroupStyle.TEXT_COLOR,
                                    value = color.Unify_Static_White.toString()
                                )
                            ),
                        )
                    )
                }
            }
        )
    }

    private fun mappingLabelGroupList(product: Product): ArrayList<ProductCardModel.LabelGroup> {
        return ArrayList<ProductCardModel.LabelGroup>().apply {
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
                            },
                        )
                    )
                }
            }
        }
    }

    private fun checkIfDTAvailable(labelGroupList: List<LabelGroup>): Boolean {
        return labelGroupList.find {
            it.position == TopAdsConstants.FULFILLMENT && it.title == TopAdsConstants.DILYANI_TOKOPEDIA
        } != null
    }

    private fun convertRatingScaleToString(rating: Int): String {
        val convertedRating = (rating / RATING_SCALE) * MAX_RATING
        val result = String.format(Locale.US, "%.1f", convertedRating)
        return result
    }

}
