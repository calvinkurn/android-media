package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class CreatedBy(
    @SerializedName("role")
        var role: String = "",
    @SerializedName("name")
        var name: String = "",
    @SerializedName("id")
        var id: Long = 0,
    @SerializedName("picture")
        var picture: String = ""
){
    fun isNullCreatedBy() : Boolean{
        return  id==0L
    }
}
