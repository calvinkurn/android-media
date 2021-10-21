package com.tokopedia.home.beranda.data.mapper.factory

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.productcard.ProductCardModel

/**
 * Created by Lukas on 2/16/21.
 */
fun DynamicHomeChannel.Grid.toProductCardModel(): ProductCardModel =
        ProductCardModel(
                slashedPrice = slashedPrice,
                productName = name,
                formattedPrice = price,
                productImageUrl = imageUrl,
                discountPercentage = discount,
                pdpViewCount = productViewCountFormatted,
                stockBarLabel = label,
                stockBarPercentage = soldPercentage,
                labelGroupList = labelGroup.map {
                    ProductCardModel.LabelGroup(
                            position = it.position,
                            title = it.title,
                            type = it.type,
                            imageUrl = it.imageUrl
                    )
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                        freeOngkir.isActive,
                        freeOngkir.imageUrl
                ),
                isOutOfStock = isOutOfStock,
                ratingCount = rating,
                reviewCount = countReview,
                countSoldRating = ratingFloat
        )