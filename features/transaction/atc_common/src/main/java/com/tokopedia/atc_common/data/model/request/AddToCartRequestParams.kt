package com.tokopedia.atc_common.data.model.request

import com.tokopedia.atc_common.AtcFromExternalSource

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

data class AddToCartRequestParams(
        var productId: Long = 0,
        var shopId: Int = 0,
        var quantity: Int = 0,
        var notes: String = "",
        var lang: String = "",
        var attribution: String = "",
        var listTracker: String = "",
        var ucParams: String = "",
        var warehouseId: Int = 0,
        var atcFromExternalSource: String = AtcFromExternalSource.ATC_FROM_OTHERS,
        var isSCP: Boolean = false,
        // analytics data
        var productName: String = "",
        var category: String = "",
        var price: String = "",
        var userId: String = "",
        var categoryLevel1Id: String = "",
        var categoryLevel1Name: String = "",
        var categoryLevel2Id: String = "",
        var categoryLevel2Name: String = "",
        var categoryLevel3Id: String = "",
        var categoryLevel3Name: String = ""
)