package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleSellerStatusResponse
import com.tokopedia.tkpd.flashsale.domain.entity.SellerEligibility
import javax.inject.Inject

class GetFlashSaleSellerStatusMapper @Inject constructor() {

    fun map(response: GetFlashSaleSellerStatusResponse): SellerEligibility {
        val access = response.getFlashSaleSellerStatus.adminAccess
        return SellerEligibility(access.manageFlashSale.isDeviceAllowed, access.manageFlashSale.isUserAllowed)
    }
}