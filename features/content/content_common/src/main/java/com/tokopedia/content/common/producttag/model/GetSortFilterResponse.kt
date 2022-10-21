package com.tokopedia.content.common.producttag.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on May 17, 2022
 */
data class GetSortFilterResponse(
    @SerializedName("filter_sort_product")
    val wrapper: DynamicFilterModel
)