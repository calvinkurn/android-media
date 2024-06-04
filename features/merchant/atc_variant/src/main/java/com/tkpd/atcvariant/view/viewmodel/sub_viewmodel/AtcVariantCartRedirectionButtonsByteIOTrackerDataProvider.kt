package com.tkpd.atcvariant.view.viewmodel.sub_viewmodel

import com.tkpd.atcvariant.view.viewmodel.GetVariantDataMediator
import com.tkpd.atcvariant.view.viewmodel.IAtcVariantCartRedirectionButtonsByteIOTrackerDataProvider
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.CartRedirectionButtonsByteIOTrackerDataProvider
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.ICartRedirectionButtonsByteIOTrackerDataProvider

class AtcVariantCartRedirectionButtonsByteIOTrackerDataProvider :
    IAtcVariantCartRedirectionButtonsByteIOTrackerDataProvider,
    ICartRedirectionButtonsByteIOTrackerDataProvider by CartRedirectionButtonsByteIOTrackerDataProvider() {
    override fun registerAtcVariantCartRedirectionButtonsByteIOTrackerDataProvider(mediator: GetVariantDataMediator) {
        registerCartRedirectionButtonsByteIOTrackerDataProvider(object : ICartRedirectionButtonsByteIOTrackerDataProvider.Mediator {
            override fun getParentProductId() = mediator.getVariantData()?.parentId
            override fun isSingleSku() = mediator.getVariantData()?.children?.size == 1
            override fun getSkuId() = getSelectedVariant()?.productId
            override fun getProductMinOrder() = getSelectedVariant()?.getFinalMinOrder()
            override fun getProductType() = getSelectedVariant()?.productType
            override fun getProductOriginalPrice() = getSelectedVariant()?.finalMainPrice
            override fun getProductSalePrice() = getSelectedVariant()?.finalPrice
            override fun isFollowShop() = mediator.getActivityResultData().isFollowShop
            private fun getSelectedVariant() = mediator.getVariantData()?.getChildByOptionId(
                mediator.getSelectedOptionIds()?.values.orEmpty().toList()
            )
            override fun getShopId() = mediator.getBasicInfo()?.shopID.orEmpty()
        })
    }
}
