package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

data class FinalAdResponse(

        @field:SerializedName("topadsManageGroupAds")
        val topadsManageGroupAds: TopadsManageGroupAds = TopadsManageGroupAds()

) {
    data class TopadsManageGroupAds(

            @field:SerializedName("keywordResponse")
            val keywordResponse: KeywordResponse = KeywordResponse(),

            @field:SerializedName("groupResponse")
            val groupResponse: GroupResponse = GroupResponse()
    ) {
        data class KeywordResponse(

                @field:SerializedName("errors")
                val errors: List<ErrorsItem>? = listOf()
        )
        data class GroupResponse(

                @field:SerializedName("errors")
                val errors: List<ErrorsItem>? = listOf()
        )
        data class ErrorsItem(

                @field:SerializedName("code")
                val code: String? = null,

                @field:SerializedName("detail")
                val detail: String? = null,

                @field:SerializedName("title")
                val title: String? = null
        )

    }

}