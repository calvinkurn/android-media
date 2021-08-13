package com.tokopedia.common_category.data.kategorymodel

import com.google.gson.annotations.SerializedName

data class Data(

        @field:SerializedName("categoryAllList")
        val categoryAllList: CategoryAllList
)