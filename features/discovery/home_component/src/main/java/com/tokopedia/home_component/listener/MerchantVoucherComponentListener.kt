package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel

/**
 * Created by dhaba
 */
interface MerchantVoucherComponentListener  {
    fun onViewAllClicked(headerName: String, seeMoreAppLink: String, userId: String)
    fun onShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked)
    fun onMerchantImpressed(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int)
    fun onProductClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int)
    fun getUserId() : String
}