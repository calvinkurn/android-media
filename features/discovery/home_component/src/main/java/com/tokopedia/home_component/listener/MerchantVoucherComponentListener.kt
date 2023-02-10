package com.tokopedia.home_component.listener

import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel

/**
 * Created by dhaba
 */
interface MerchantVoucherComponentListener  {
    fun onViewAllCardClicked(channelId: String, headerName: String, seeMoreAppLink: String, userId: String, campaignCode: String)
    fun onViewAllClicked(channelId: String, headerName: String, seeMoreAppLink: String, userId: String, campaignCode: String)
    fun onShopClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int)
    fun onMerchantImpressed(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int)
    fun onProductClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int)
    fun getUserId() : String
}
