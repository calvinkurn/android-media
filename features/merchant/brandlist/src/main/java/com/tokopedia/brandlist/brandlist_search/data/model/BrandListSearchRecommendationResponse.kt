package com.tokopedia.brandlist.brandlist_search.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.brandlist.brandlist_page.data.model.OfficialStoreBrandsRecommendation

data class BrandListSearchRecommendationResponse(
        @SerializedName("OfficialStoreBrandsRecommendation")
        val officialStoreBrandsRecommendation: OfficialStoreBrandsRecommendation

)