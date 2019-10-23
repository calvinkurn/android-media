package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("page")
    val page: Page = Page()
)