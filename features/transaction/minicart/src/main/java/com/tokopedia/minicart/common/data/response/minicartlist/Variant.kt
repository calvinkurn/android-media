package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Variant(
        @SerializedName("children_id")
        val childrenId: String = "",
        @SerializedName("is_parent")
        val isParent: Boolean = false,
        @SerializedName("is_variant")
        val isVariant: Boolean = false,
        @SerializedName("parent_id")
        val parentId: String = ""
)