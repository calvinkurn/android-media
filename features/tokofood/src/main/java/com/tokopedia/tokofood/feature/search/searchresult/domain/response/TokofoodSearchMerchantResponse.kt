package com.tokopedia.tokofood.feature.search.searchresult.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.response.TokoFoodMerchantList

data class TokofoodSearchMerchantResponse(
    @SerializedName("tokofoodSearchMerchant")
    val tokofoodSearchMerchant: TokoFoodMerchantList = TokoFoodMerchantList()
)