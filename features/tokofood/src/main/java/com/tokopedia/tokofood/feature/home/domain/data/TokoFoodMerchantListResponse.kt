package com.tokopedia.tokofood.feature.home.domain.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.response.TokoFoodMerchantList

data class TokoFoodMerchantListResponse (
    @SerializedName("tokofoodGetMerchantList")
    val data: TokoFoodMerchantList = TokoFoodMerchantList()
)