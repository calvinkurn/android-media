package com.tokopedia.shop.common.data.source.cloud.model.productlist

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductTopAds.AdsStatus.INACTIVE
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductTopAds.AdsStatus.IN_REVIEW
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductTopAds.AdsStatus.PUBLISHED
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductTopAds.AdsStatus.REJECTED
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductTopAds.AdsStatus.UNPUBLISHED

data class ProductTopAds(
    @SerializedName("status")
    val status: Int,
    @SerializedName("management")
    val management: Int
) {
    private companion object {
        val ADS_STATUS = listOf(
            PUBLISHED,
            UNPUBLISHED,
            INACTIVE,
            REJECTED,
            IN_REVIEW
        )
    }

    private object AdsStatus {
        const val PUBLISHED = 1
        const val UNPUBLISHED = 2
        const val INACTIVE = 3
        const val REJECTED = 8
        const val IN_REVIEW = 11
    }

    private object ManagementType {
        const val AUTO = 2
    }

    fun isApplied() = ADS_STATUS.contains(status)

    fun isAutoAds() = management == ManagementType.AUTO
}