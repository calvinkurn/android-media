package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.util.TrackingUtil
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

sealed class ShopCredibilityTracker {

    data class ImpressionShopTicker(
        private val productInfo: DynamicProductInfoP1,
        private val componentTrackDataModel: ComponentTrackDataModel,
        private val tickerDataResponse: ShopInfo.TickerDataResponse,
        val userId: String
    ) {
        private val productBasic = productInfo.basic
        private val basicCategory = productBasic.category

        val shopId = productBasic.shopID
        val componentName = componentTrackDataModel.componentName
        val componentType = componentTrackDataModel.componentType
        val componentPosition = componentTrackDataModel.adapterPosition.toString()
        val layoutName = productInfo.layoutName
        val categoryName = basicCategory.name
        val categoryId = basicCategory.id
        val productId = productBasic.productID
        val shopType = productInfo.shopTypeString
        val title = tickerDataResponse.title
        val message = tickerDataResponse.message
        val tickerType = TrackingUtil.getTickerTypeInfoString(tickerDataResponse.tickerType)
    }

    data class ClickShopTicker(
        private val productInfo: DynamicProductInfoP1,
        private val componentTrackDataModel: ComponentTrackDataModel,
        private val tickerDataResponse: ShopInfo.TickerDataResponse,
        val userId: String
    ) {
        private val productBasic = productInfo.basic
        private val basicCategory = productBasic.category

        val shopId = productBasic.shopID
        val componentName = componentTrackDataModel.componentName
        val componentType = componentTrackDataModel.componentType
        val componentPosition = componentTrackDataModel.adapterPosition
        val layoutName = productInfo.layoutName
        val categoryName = basicCategory.name
        val categoryId = basicCategory.id
        val productId = productBasic.productID
        val shopType = productInfo.shopTypeString
        val title = tickerDataResponse.title
        val message = tickerDataResponse.message
        val tickerType = TrackingUtil.getTickerTypeInfoString(tickerDataResponse.tickerType)
        val buttonText = tickerDataResponse.link
    }
}
