package com.tokopedia.home.beranda.data.model

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.constant.AtfKey

data class AtfData(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("component")
        @Expose
        val component: String = "",
        @SerializedName("param")
        @Expose
        val param: String = "",
        @SerializedName("isOptional")
        @Expose
        val isOptional: Boolean = false,
        @SerializedName("content")
        @Expose
        var content: String? = "",
        @SerializedName("status")
        @Expose
        var status: Int = AtfKey.STATUS_LOADING
) {
        inline fun <reified T> getAtfContent(): T? {
                val gson = Gson()
                return gson.fromJson(content, T::class.java)
        }
}