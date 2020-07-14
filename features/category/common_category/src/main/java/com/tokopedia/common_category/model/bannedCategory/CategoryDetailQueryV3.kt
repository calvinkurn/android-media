package com.tokopedia.common_category.model.bannedCategory

import com.google.gson.annotations.SerializedName

class CategoryDetailQueryV3 {

    @SerializedName("data")
    var data: Data? = null

    @SerializedName("header")
    var header: Header? = null

    override fun toString(): String {
        return "CategoryDetailQueryV3{" +
                "data = '" + data + '\''.toString() +
                ",header = '" + header + '\''.toString() +
                "}"
    }
}