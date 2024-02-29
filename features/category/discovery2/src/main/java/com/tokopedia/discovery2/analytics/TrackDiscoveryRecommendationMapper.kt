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
        position: Int,
        componentNames: String
    ): AppLogRecommendationProductModel {
        return AppLogRecommendationProductModel.create(
            productId = productId.orEmpty(),
            position = position,
            tabName = tabName.orEmpty(),
            tabPosition = tabIndex?.firstOrNull().orZero(),
            moduleName = componentPromoName.orEmpty(),
            isAd = isTopads.orFalse(),
            isUseCache = false,
            recSessionId = getAppLog()?.sessionId.orEmpty(),
            requestId = getAppLog()?.requestId.orEmpty(),
            recParams = getAppLog()?.recParams.orEmpty(),
            shopId = shopId.orEmpty(),
            entranceForm = componentNames.getEntranceForm(),
            originalPrice = price.toFloatOrZero(),
            salesPrice = discountedPrice.toFloatOrZero()
        )
    }

    private fun String.getEntranceForm(): EntranceForm {
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
//            && !(getAppLog()?.pageName?.contains("injection").orFalse())
    }

    fun ComponentsItem.isEligibleToTrack(): Boolean {
        return getSource() == ComponentSourceData.Recommendation
//            && !(getComponentAdditionalInfo()?.tracker?.recommendationPageName?.contains("injection").orFalse())
    }
}
