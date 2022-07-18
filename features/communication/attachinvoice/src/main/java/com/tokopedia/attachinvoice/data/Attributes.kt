package com.tokopedia.attachinvoice.data


import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("Code")
    val code: String = "",
    @SerializedName("CreateTime")
    val createTime: String = "",
    @SerializedName("Description")
    val description: String = "",
    @SerializedName("HrefURL")
    val hrefURL: String = "",
    @SerializedName("Id")
    val id: String = "",
    @SerializedName("ImageURL")
    val imageURL: String = "",
    @SerializedName("Status")
    val status: String = "",
    @SerializedName("StatusId")
    val statusId: String = "0",
    @SerializedName("Title")
    val title: String = "",
    @SerializedName("TotalAmount")
    val totalAmount: String = ""
)