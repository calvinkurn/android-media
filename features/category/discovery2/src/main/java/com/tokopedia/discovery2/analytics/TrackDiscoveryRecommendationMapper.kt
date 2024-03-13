package com.tokopedia.discovery2.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentSourceData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toFloatOrZero

object TrackDiscoveryRecommendationMapper {
    fun DataItem.asProductTrackModel(
        componentNames: String,
        isEligibleRecTrigger : Boolean = true
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = productId.orEmpty(),
            parentProductId = parentProductId.orEmpty(),
            position = itemPosition,
            tabName = topLevelTab.name,
            tabPosition = topLevelTab.index,
            moduleName = getAppLog()?.pageName.orEmpty(),
            isAd = isTopads.orFalse(),
            isUseCache = false,
            recSessionId = getAppLog()?.sessionId.orEmpty(),
            requestId = getAppLog()?.requestId.orEmpty(),
            recParams = getAppLog()?.recParams.orEmpty(),
            shopId = shopId.orEmpty(),
            entranceForm = componentNames.getEntranceForm(),
            originalPrice = price.toFloatOrZero(),
            salesPrice = discountedPrice.toFloatOrZero(),
            isEligibleRecTrigger = isEligibleRecTrigger,
            isTrackAsHorizontalSourceModule = componentNames.isTrackAsHorizontalSourceModule()
        )
    }

    private fun String.isTrackAsHorizontalSourceModule() : Boolean{
        return when(this){
            ComponentNames.ProductCardSingleItem.componentName,
            ComponentNames.ProductCardSingleItemReimagine.componentName -> true
            else -> false
        }
    }

    fun String.getEntranceForm(): EntranceForm {
        return when (this) {
            ComponentNames.ProductCardCarouselItem.componentName,
            ComponentNames.ProductCardCarouselItemReimagine.componentName,
            ComponentNames.ProductCardCarouselItemList.componentName,
            ComponentNames.ProductCardCarouselItemListReimagine.componentName,
            ComponentNames.ProductCardSprintSaleCarouselItem.componentName,
            ComponentNames.ProductCardSprintSaleCarouselItemReimagine.componentName,
            ComponentNames.ProductCardColumnList.componentName,
            ComponentNames.ShopOfferHeroBrandProductItem.componentName -> EntranceForm.HORIZONTAL_GOODS_CARD
            ComponentNames.ProductCardRevampItem.componentName,
            ComponentNames.MasterProductCardItemReimagine.componentName,
            ComponentNames.ProductCardSprintSaleItemReimagine.componentName -> EntranceForm.PURE_GOODS_CARD
            ComponentNames.MasterProductCardItemList.componentName,
            ComponentNames.MasterProductCardItemListReimagine.componentName,
            ComponentNames.ProductCardSingleItem.componentName,
            ComponentNames.ProductCardSingleItemReimagine.componentName -> EntranceForm.DETAIL_GOODS_CARD
            else -> EntranceForm.HORIZONTAL_GOODS_CARD
        }
    }

    fun DataItem.isEligibleToTrack(): Boolean {
        return source == ComponentSourceData.Recommendation
    }

    fun ComponentsItem.isEligibleToTrack(): Boolean {
        return getSource() == ComponentSourceData.Recommendation
    }
}
