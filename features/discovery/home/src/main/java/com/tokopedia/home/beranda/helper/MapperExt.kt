package com.tokopedia.home.beranda.helper

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.util.ConstantABTesting
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.remoteconfig.RemoteConfigInstance

/**
 * Created by Lukas on 2/5/21.
 */
fun DynamicHomeChannel.Grid.convertData(): ProductCardModel {
    return ProductCardModel(
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
                        type = it.type
                )
            },
            freeOngkir = ProductCardModel.FreeOngkir(
                    freeOngkir.isActive,
                    freeOngkir.imageUrl
            ),
            isOutOfStock = isOutOfStock,
            ratingCount = if(RemoteConfigInstance.getInstance().abTestPlatform.getString(ConstantABTesting.EXPERIMENT_NAME) == ConstantABTesting.EXPERIMENT_RATING_ONLY) rating else 0,
            reviewCount = if(RemoteConfigInstance.getInstance().abTestPlatform.getString(ConstantABTesting.EXPERIMENT_NAME) == ConstantABTesting.EXPERIMENT_RATING_ONLY) countReview else 0,
            countSoldRating = if(RemoteConfigInstance.getInstance().abTestPlatform.getString(ConstantABTesting.EXPERIMENT_NAME) == ConstantABTesting.EXPERIMENT_SALES_RATING) ratingFloat.toString() else ""
    )
}