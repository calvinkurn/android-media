package com.tokopedia.product.detail.common.buttons_byte_io_tracker

import com.tokopedia.analytics.byteio.ButtonClickAnalyticData
import com.tokopedia.analytics.byteio.ButtonClickCompletedAnalyticData
import com.tokopedia.analytics.byteio.ButtonShowAnalyticData
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.product.detail.common.ProductDetailCommonConstant

class CartRedirectionButtonsByteIOTrackerViewModel: ICartRedirectionButtonsByteIOTrackerViewModel {

    private var _mediator: ICartRedirectionButtonsByteIOTrackerViewModel.Mediator? = null

    override fun registerCartRedirectionButtonsByteIOTrackerViewModel(mediator: ICartRedirectionButtonsByteIOTrackerViewModel.Mediator) {
        _mediator = mediator
    }

    override fun getButtonShowTrackData(cartType: String): ButtonShowAnalyticData? {
        return _mediator?.run {
            ButtonShowAnalyticData(
                buttonName = when (cartType) {
                    ProductDetailCommonConstant.KEY_OCS_BUTTON -> ButtonShowAnalyticData.ButtonName.BUY_NOW
                    else -> return null
                },
                productId = getParentProductId() ?: return null,
                isSingleSku = isSingleSku(),
                buyType = when (cartType) {
                    ProductDetailCommonConstant.KEY_OCS_BUTTON -> ButtonShowAnalyticData.BuyType.OCS
                    else -> return null
                }
            )
        }
    }

    override fun getButtonClickTrackData(buttonAction: Int): ButtonClickAnalyticData? {
        return _mediator?.run {
            ButtonClickAnalyticData(
                buttonName = when (buttonAction) {
                    ProductDetailCommonConstant.OCS_BUTTON -> ButtonClickAnalyticData.ButtonName.BUY_NOW
                    else -> return null
                },
                productId = getParentProductId() ?: return null,
                isSingleSku = isSingleSku(),
                buyType = when (buttonAction) {
                    ProductDetailCommonConstant.OCS_BUTTON -> ButtonClickAnalyticData.BuyType.OCS
                    else -> return null
                }
            )
        }
    }

    override fun getButtonClickCompletedTrackData(
        buttonActionType: Int,
        data: AddToCartDataModel
    ): ButtonClickCompletedAnalyticData? {
        return _mediator?.run {
            ButtonClickCompletedAnalyticData(
                productId = getParentProductId() ?: return null,
                isSingleSku = isSingleSku(),
                skuId = getSkuId() ?: return null,
                quantity = getProductMinOrder().toString(),
                productType = getProductType() ?: return null,
                originalPrice = getProductOriginalPrice().toString(),
                salePrice = getProductSalePrice().toString(),
                followStatus = if (isFollowShop()) {
                    ButtonClickCompletedAnalyticData.FollowStatus.FOLLOWED
                } else {
                    ButtonClickCompletedAnalyticData.FollowStatus.UNFOLLOWED
                },
                buyType = when (buttonActionType) {
                    ProductDetailCommonConstant.OCS_BUTTON -> ButtonClickCompletedAnalyticData.BuyType.OCS
                    else -> return null
                },
                cartId = data.data.cartId
            )
        }
    }
}
