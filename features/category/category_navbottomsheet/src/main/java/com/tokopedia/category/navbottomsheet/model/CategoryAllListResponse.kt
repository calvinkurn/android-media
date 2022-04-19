package com.tokopedia.category.navbottomsheet.model

import com.google.gson.annotations.SerializedName

data class CategoryAllListResponse(

        @field:SerializedName("categoryAllList")
        val categoryAllList: CategoryAllList
)