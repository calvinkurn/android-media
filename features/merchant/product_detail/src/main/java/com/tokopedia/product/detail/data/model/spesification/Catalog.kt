package com.tokopedia.product.detail.data.model.spesification


import com.google.gson.annotations.SerializedName

data class Catalog(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("specification")
        val specification: ArrayList<Specification> = arrayListOf())