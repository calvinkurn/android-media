package com.tokopedia.topads.edit.data.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class EditSingleAdResponse(

        @field:SerializedName("topAdsPatchPromo")
        val topAdsPatchPromo: TopAdsPatchPromo = TopAdsPatchPromo()
) {
    data class TopAdsPatchPromo(

            @field:SerializedName("data")
            val data: List<DataItem> = listOf(),

            @field:SerializedName("errors")
            val errors: List<Error> = listOf()
    ) {
        data class DataItem(

                @field:SerializedName("priceBid")
                val priceBid: Int = 0,

                @field:SerializedName("adID")
                val adID: String? = ""

        )
    }
}