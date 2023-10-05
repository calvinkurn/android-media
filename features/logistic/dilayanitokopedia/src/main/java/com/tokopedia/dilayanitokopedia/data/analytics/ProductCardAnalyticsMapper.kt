package com.tokopedia.dilayanitokopedia.data.analytics

import com.tokopedia.dilayanitokopedia.ui.recommendation.adapter.datamodel.HomeRecommendationItemDataModel
import com.tokopedia.home_component.model.ChannelGrid
import java.util.*

/**
 * Created by irpan on 23/05/23.
 */
object ProductCardAnalyticsMapper {
    fun fromGridChannel(position: Int, channelGrid: ChannelGrid): ProductCardAnalyticsModel {
        return ProductCardAnalyticsModel(
            index = position.toString(),
            productId = channelGrid.id.toString(),
            productName = channelGrid.name,
            price = channelGrid.price,
            productBrand = channelGrid.brandId,
            categoryBreadcrumbs = channelGrid.categoryBreadcrumbs.lowercase(Locale.getDefault())
        )
    }

    fun fromRecommendation(
        position: Int,
        homeRecommendationItemDataModel: HomeRecommendationItemDataModel
    ): ProductCardAnalyticsModel? {
        val product = homeRecommendationItemDataModel.product
        return ProductCardAnalyticsModel(
            index = position.toString(),
            productId = product.id.toString(),
            productName = product.name,
            price = product.price,
            productBrand = "",
            productVariant = "",
            categoryBreadcrumbs = homeRecommendationItemDataModel.product.categoryBreadcrumbs.lowercase(Locale.getDefault())
        )
    }
}
