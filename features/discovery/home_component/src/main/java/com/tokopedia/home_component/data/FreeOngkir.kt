package com.tokopedia.home_component.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 17/10/19
 */
data class FreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = ""
) 