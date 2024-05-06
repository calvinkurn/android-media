package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.ButtonClickAnalyticData
import com.tokopedia.analytics.byteio.ButtonClickCompletedAnalyticData
import com.tokopedia.analytics.byteio.ButtonShowAnalyticData
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.ProductDetailCommonConstant

@Suppress("LateinitUsage")
class CartRedirectionButtonsByteIOTrackerDataProvider : ICartRedirectionButtonsByteIOTrackerDataProvider {

    private lateinit var _mediator: ICartRedirectionButtonsByteIOTrackerDataProvider.Mediator

    override fun registerCartRedirectionButtonsByteIOTrackerDataProvider(mediator: ICartRedirectionButtonsByteIOTrackerDataProvider.Mediator) {
        _mediator = mediator
    }

    override fun getButtonShowTrackData(cartType: String): ButtonShowAnalyticData? {
        return ButtonShowAnalyticData(
            buttonName = when (cartType) {
                ProductDetailCommonConstant.KEY_OCS_BUTTON -> ButtonShowAnalyticData.ButtonName.BUY_NOW
                else -> return null
            },
            productId = _mediator.getParentProductId() ?: return null,
            isSingleSku = _mediator.isSingleSku(),
            buyType = when (cartType) {
                ProductDetailCommonConstant.KEY_OCS_BUTTON -> ButtonShowAnalyticData.BuyType.OCS
                else -> return null
            },
            shopId = _mediator.getShopId()
        )
    }

    override fun getButtonClickTrackData(buttonAction: Int): ButtonClickAnalyticData? {
        return ButtonClickAnalyticData(
            buttonName = when (buttonAction) {
                ProductDetailCommonConstant.OCS_BUTTON -> ButtonClickAnalyticData.ButtonName.BUY_NOW
                else -> return null
            },
            productId = _mediator.getParentProductId() ?: return null,
            isSingleSku = _mediator.isSingleSku(),
            buyType = when (buttonAction) {
                ProductDetailCommonConstant.OCS_BUTTON -> ButtonClickAnalyticData.BuyType.OCS
                else -> return null
            },
            shopId = _mediator.getShopId()
        )
    }

    override fun getButtonClickCompletedTrackData(
        buttonActionType: Int,
        data: AddToCartDataModel
    ): ButtonClickCompletedAnalyticData? {
        return ButtonClickCompletedAnalyticData(
            productId = _mediator.getParentProductId() ?: return null,
            isSingleSku = _mediator.isSingleSku(),
            skuId = _mediator.getSkuId() ?: return null,
            quantity = _mediator.getProductMinOrder().toString(),
            productType = _mediator.getProductType() ?: return null,
            originalPrice = _mediator.getProductOriginalPrice().orZero(),
            salePrice = _mediator.getProductSalePrice().orZero(),
            followStatus = if (_mediator.isFollowShop()) {
                ButtonClickCompletedAnalyticData.FollowStatus.FOLLOWED
            } else {
                ButtonClickCompletedAnalyticData.FollowStatus.UNFOLLOWED
            },
            buyType = when (buttonActionType) {
                ProductDetailCommonConstant.OCS_BUTTON -> ButtonClickCompletedAnalyticData.BuyType.OCS
                else -> return null
            },
            cartId = data.data.cartId,
            shopId = data.data.shopId
        )
    }
}
