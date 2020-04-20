package com.tokopedia.topads.edit.data.response

import com.google.gson.annotations.SerializedName

data class FinalAdResponse(

        @field:SerializedName("topadsManageGroupAds")
        val topadsManageGroupAds: TopadsManageGroupAds = TopadsManageGroupAds()

) {
    data class TopadsManageGroupAds(

            @field:SerializedName("keywordResponse")
            val keywordResponse: KeywordResponse = KeywordResponse(),

            @field:SerializedName("header")
            val header: Header? = Header(),

            @field:SerializedName("groupResponse")
            val groupResponse: GroupResponse = GroupResponse()
    ) {
        data class KeywordResponse(

                @field:SerializedName("data")
                val data: List<DataItem> = listOf(),

                @field:SerializedName("errors")
                val errors: List<ErrorsItem>? = listOf(),

                @field:SerializedName("status")
                val status: Status? = Status()
        )

        data class Header(

                @field:SerializedName("total_data")
                val totalData: Int = 0,

                @field:SerializedName("process_time")
                val processTime: Double? = null
        )

        data class GroupResponse(

                @field:SerializedName("data")
                val data: Data? = Data(),

                @field:SerializedName("errors")
                val errors: List<ErrorsItem>? = listOf(),

                @field:SerializedName("status")
                val status: Status = Status()
        )

        data class Status(

                @field:SerializedName("error_code")
                val errorCode: Int? = null,

                @field:SerializedName("message")
                val message: String? = null
        )

        data class ErrorsItem(

                @field:SerializedName("code")
                val code: String? = null,

                @field:SerializedName("detail")
                val detail: String? = null,

                @field:SerializedName("title")
                val title: String? = null,

                @field:SerializedName("object")
                val value: Object1 = Object1()
        )

        data class Object1(

                @field:SerializedName("text")
                val text: List<Any?>? = null,

                @field:SerializedName("type")
                val type: Int? = null
        )

        data class DataItem(

                @field:SerializedName("resourceUrl")
                val resourceUrl: String? = null,

                @field:SerializedName("id")
                val id: Int? = null
        )


    }

}