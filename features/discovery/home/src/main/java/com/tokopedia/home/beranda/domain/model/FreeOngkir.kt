package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 17/10/19
 */
class FreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = ""
) 