package com.tokopedia.content.common.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 18, 2022
 */
data class GetSortFilterProductCountResponse(
    @SerializedName("searchProduct")
    val wrapper: Wrapper = Wrapper()
) {

    data class Wrapper(
        @SerializedName("count_text")
        val countText: String = ""
    )
}