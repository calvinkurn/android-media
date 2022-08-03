package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignPackageListResponse
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import javax.inject.Inject

class GetSellerCampaignPackageListMapper @Inject constructor() {

    fun map(data: GetSellerCampaignPackageListResponse): List<VpsPackage> {
        return data.getSellerCampaignPackageList.packageList.map { currentPackage ->
            VpsPackage(
                currentPackage.currentQuota,
                currentPackage.isDisabled,
                currentPackage.originalQuota,
                currentPackage.packageEndTime,
                currentPackage.packageId,
                currentPackage.packageName,
                currentPackage.packageStartTime
            )
        }
    }

}