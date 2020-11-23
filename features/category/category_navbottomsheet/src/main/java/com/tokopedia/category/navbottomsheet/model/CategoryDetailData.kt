package com.tokopedia.category.navbottomsheet.model

import com.google.gson.annotations.SerializedName

class CategoryDetailData{

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("parent")
    var parent: Int? = null

    @SerializedName("rootId")
    var rootId: Int? = null
}