package com.tokopedia.product.manage.stock_reminder.data.source.cloud.response

import com.google.gson.annotations.SerializedName

data class StockReminderResponse(
        @SerializedName("IMSGetByProductIDs")
        var getByProductIds: DataWrapper
)