package com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.CartRedirectionButtonsByteIOTrackerViewModel
import com.tokopedia.product.detail.common.buttons_byte_io_tracker.ICartRedirectionButtonsByteIOTrackerViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.ICartRedirectionButtonsByteIOTrackerSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.mediator.GetProductDetailDataMediator

class CartRedirectionButtonsByteIOTrackerSubViewModel :
    ICartRedirectionButtonsByteIOTrackerSubViewModel,
    ICartRedirectionButtonsByteIOTrackerViewModel by CartRedirectionButtonsByteIOTrackerViewModel() {

    override fun registerCartRedirectionButtonsByteIOTrackerSubViewModel(mediator: GetProductDetailDataMediator) {
        registerCartRedirectionButtonsByteIOTrackerViewModel(object : ICartRedirectionButtonsByteIOTrackerViewModel.Mediator {
            override fun getParentProductId() = mediator.getP1()?.parentProductId
            override fun isSingleSku() = if (mediator.getP1()?.isProductVariant() == false) true
            else mediator.getVariant()?.children?.size == 1
            override fun getSkuId() = mediator.getP1()?.basic?.productID
            override fun getProductMinOrder() = mediator.getP1()?.basic?.minOrder
            override fun getProductType() = mediator.getP1()?.productType
            override fun getProductOriginalPrice() = mediator.getP1()?.originalPrice
            override fun getProductSalePrice() = mediator.getP1()?.finalPrice
            override fun isFollowShop() = mediator.getP2Login()?.isFollow.orZero() != 0
        })
    }
}
