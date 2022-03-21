package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TxStats(
        @SerializedName("itemSold")
        @Expose
        val itemSold: Int = 0
)