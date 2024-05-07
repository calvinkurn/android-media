package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.ButtonClickAnalyticData
import com.tokopedia.analytics.byteio.ButtonClickCompletedAnalyticData
import com.tokopedia.analytics.byteio.ButtonShowAnalyticData
import com.tokopedia.analytics.byteio.pdp.AtcBuyType
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.view.orZero

@Suppress("LateinitUsage")
class CartRedirectionButtonsByteIOTrackerDataProvider :
    ICartRedirectionButtonsByteIOTrackerDataProvider {

    private lateinit var _mediator: ICartRedirectionButtonsByteIOTrackerDataProvider.Mediator

    override fun registerCartRedirectionButtonsByteIOTrackerDataProvider(mediator: ICartRedirectionButtonsByteIOTrackerDataProvider.Mediator) {
        _mediator = mediator
    }

    override fun getButtonShowTrackData(cartType: String): ButtonShowAnalyticData? {
        if (cartType != AtcBuyType.OCS.funnel) return null
        val buttonType = AtcBuyType.getBuyType(cartType = cartType) ?: return null

        return ButtonShowAnalyticData(
            buttonName = buttonType.buttonName,
            productId = _mediator.getParentProductId() ?: return null,
            isSingleSku = _mediator.isSingleSku(),
            buyType = buttonType,
            shopId = _mediator.getShopId()
        )
    }

    override fun getButtonClickTrackData(buttonAction: Int): ButtonClickAnalyticData? {
        if (buttonAction != AtcBuyType.OCS.code) return null

        val buttonType = AtcBuyType.getBuyType(actionType = buttonAction) ?: return null
        return ButtonClickAnalyticData(
            buttonName = buttonType.buttonName,
            productId = _mediator.getParentProductId() ?: return null,
            isSingleSku = _mediator.isSingleSku(),
            buyType = buttonType,
            shopId = _mediator.getShopId()
        )
    }

    override fun getButtonClickCompletedTrackData(
        buttonActionType: Int,
        data: AddToCartDataModel
    ): ButtonClickCompletedAnalyticData? {
        if (buttonActionType != AtcBuyType.OCS.code) return null

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
            buyType = AtcBuyType.getBuyType(actionType = buttonActionType) ?: return null,
            cartId = data.data.cartId,
            shopId = data.data.shopId
        )
    }
}
