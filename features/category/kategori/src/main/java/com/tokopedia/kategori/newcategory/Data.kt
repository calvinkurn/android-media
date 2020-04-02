package com.tokopedia.kategori.newcategory

import com.google.gson.annotations.SerializedName

data class Data(

        @field:SerializedName("categoryAllList")
        val categoryAllList: CategoryAllList
)