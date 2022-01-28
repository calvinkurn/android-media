package com.tokopedia.shop_showcase.shop_showcase_tab.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rafli Syam on 16/03/2021
 */
data class GetFeaturedShowcaseRequestParams(
        @SerializedName("shopID")
        @Expose
        var shopId: String = "0"
)