package com.tokopedia.home.beranda.data.datasource.local.entity

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home.beranda.data.model.AtfData
import com.tokopedia.home.constant.AtfKey

@Entity
data class AtfCacheEntity(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @PrimaryKey
    @SerializedName("position")
    @Expose
    val position: Int = 0,
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
    var status: Int = AtfKey.STATUS_SUCCESS,
    @SerializedName("isShimmer")
    @Expose
    val isShimmer: Boolean = true,
    @SerializedName("lastUpdate")
    @Expose
    val lastUpdate: Long = System.currentTimeMillis(),
    @SerializedName("style")
    @Expose
    val style: String = "",
)
