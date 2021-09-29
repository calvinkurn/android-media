package com.tokopedia.common.topupbills.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by resakemal on 28/08/19.
 */
class TopupBillsCatalog(
        @SuppressLint("Invalid Data Type")
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = ""
)