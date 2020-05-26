package com.tokopedia.kategori.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kategori.model.CategoryAllList

data class Data(

        @field:SerializedName("categoryAllList")
        val categoryAllList: CategoryAllList
)