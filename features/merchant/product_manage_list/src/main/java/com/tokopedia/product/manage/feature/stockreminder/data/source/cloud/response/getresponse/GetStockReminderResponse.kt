package com.tokopedia.product.manage.feature.stockreminder.data.source.cloud.response.getresponse

import com.google.gson.annotations.SerializedName

data class GetStockReminderResponse(
        @SerializedName("IMSGetByProductIDs")
        var getByProductIds: GetDataWrapper
)