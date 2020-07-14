package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 2020-03-06.
 */
data class PinnedProduct(
        @SerializedName("title_pinned")
        val title: String = "",
        @SerializedName("title_bottom_sheet")
        val titleBottomSheet: String = "",
        @SerializedName("is_show_discount")
        val isShowDiscount: Boolean = false
)