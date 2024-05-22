package com.tokopedia.discovery2.analytics

import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.ProductType
import com.tokopedia.analytics.byteio.TrackConfirmCart
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
import com.tokopedia.analytics.byteio.recommendation.AppLogAdditionalParam
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationProductModel
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentSourceData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

object TrackDiscoveryRecommendationMapper {
    fun DataItem.asProductTrackModel(
        componentNames: String
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
            salesPrice = price.cleanUpCurrencyValue().toFloat(),
            originalPrice = getSalesPrice().cleanUpCurrencyValue().toFloat(),
            isTrackAsHorizontalSourceModule = componentNames.isTrackAsHorizontalSourceModule(),
            isEligibleForRecTrigger = isEligibleToTrackRecTrigger(componentNames),
            additionalParam = anchorProductId.toLongOrZero().getAdditionalParam()
        )
    }

    fun DataItem.asTrackConfirmCart(): TrackConfirmCart {
        return TrackConfirmCart(
            productId = getAppLogSPUId(),
            productCategory = String.EMPTY,
            productType = getProductType(),
            salePrice = price.cleanUpCurrencyValue(),
            originalPrice = getSalesPrice().cleanUpCurrencyValue(),
            buttonType = "able_to_cart",
            skuId = productId.orEmpty(),
            currency = "IDR",
            addSkuNum = minQuantity
        )
    }

    fun DataItem.asTrackConfirmCartSucceed(
        cartId: String?
    ): TrackConfirmCartResult {
        return TrackConfirmCartResult(
            productId = getAppLogSPUId(),
            productCategory = String.EMPTY,
            productType = getProductType(),
            salePrice = price.cleanUpCurrencyValue(),
            originalPrice = getSalesPrice().cleanUpCurrencyValue(),
            buttonType = "able_to_cart",
            skuId = productId.orEmpty(),
            currency = "IDR",
            addSkuNum = minQuantity,
            cartItemId = cartId.orEmpty(),
            isSuccess = true,
            failReason = String.EMPTY
        )
    }

    fun DataItem.asTrackConfirmCartFailed(
        reason: String
    ): TrackConfirmCartResult {
        return TrackConfirmCartResult(
            productId = getAppLogSPUId(),
            productCategory = String.EMPTY,
            productType = getProductType(),
            salePrice = price.cleanUpCurrencyValue(),
            originalPrice = getSalesPrice().cleanUpCurrencyValue(),
            buttonType = "able_to_cart",
            skuId = productId.orEmpty(),
            currency = "IDR",
            addSkuNum = minQuantity,
            cartItemId = String.EMPTY,
            isSuccess = false,
            failReason = reason
        )
    }


    private fun DataItem.getProductType(): ProductType {
        val productType = if (isActiveProductCard == true) {
            ProductType.AVAILABLE
        } else {
            if (stock.toIntOrZero().isMoreThanZero()) {
                ProductType.NOT_AVAILABLE
            } else {
                ProductType.SOLD_OUT
            }
        }
        return productType
    }

    private fun Long.getAdditionalParam(): AppLogAdditionalParam {
        return if (isMoreThanZero()) {
            AppLogAdditionalParam.DiscoveryWithAnchorProduct(toString())
        } else {
            AppLogAdditionalParam.None
        }
    }

    private fun String.isTrackAsHorizontalSourceModule(): Boolean {
        return when (this) {
            ComponentNames.ProductCardSingle.componentName,
            ComponentNames.ProductCardSingleReimagine.componentName,
            ComponentNames.ProductCardSingleItem.componentName,
            ComponentNames.ProductCardSingleItemReimagine.componentName -> true

            else -> false
        }
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
            ComponentNames.ShopOfferHeroBrandProductItem.componentName,
            ComponentNames.ShopOfferHeroBrandProductItemReimagine.componentName -> EntranceForm.HORIZONTAL_GOODS_CARD

            ComponentNames.ProductCardRevampItem.componentName,
            ComponentNames.MasterProductCardItemReimagine.componentName,
            ComponentNames.ProductCardSprintSaleItemReimagine.componentName -> EntranceForm.PURE_GOODS_CARD

            ComponentNames.MasterProductCardItemList.componentName,
            ComponentNames.MasterProductCardItemListReimagine.componentName,
            ComponentNames.ProductCardSingleItem.componentName,
            ComponentNames.ProductCardSingleItemReimagine.componentName,
            ComponentNames.ProductCardSingle.componentName,
            ComponentNames.ProductCardSingleReimagine.componentName -> EntranceForm.DETAIL_GOODS_CARD

            else -> EntranceForm.HORIZONTAL_GOODS_CARD
        }
    }

    fun DataItem.isEligibleToTrack(): Boolean {
        return source == ComponentSourceData.Recommendation
    }

    fun DataItem.isEligibleToTrackRecTrigger(componentNames: String): Boolean {
        if (componentNames == ComponentNames.ProductCardSingleItem.componentName ||
            componentNames == ComponentNames.ProductCardSingleItemReimagine.componentName) {
            return isEligibleToTrack() &&
                (getAppLog()?.pageName?.contains("best_seller") == true ||
                    getAppLog()?.pageName?.contains("trending") == true)
        }
        return isEligibleToTrack()
    }

    fun ComponentsItem.isEligibleToTrack(): Boolean {
        return getSource() == ComponentSourceData.Recommendation
    }

    private fun String?.cleanUpCurrencyValue(): Double {
        return CurrencyFormatHelper
            .convertRupiahToDouble(this.orEmpty())
    }

    private fun DataItem.getSalesPrice(): String {
        return discountedPrice.ifNullOrBlank { price.orEmpty() }
    }
}
