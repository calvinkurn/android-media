package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by M on 31/10/2019
 */
data class UmrahVariant(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("description")
        @Expose
        val desc: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("slashPrice")
        @Expose
        val slashPrice: Int = 0
)