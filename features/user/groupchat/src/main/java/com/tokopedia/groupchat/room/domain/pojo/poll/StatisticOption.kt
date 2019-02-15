package com.tokopedia.groupchat.room.domain.pojo.poll

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatisticOption (

    @SerializedName("option_id")
    @Expose
    var optionId: Int = 0,
    @SerializedName("option")
    @Expose
    var option: String = "",
    @SerializedName("voter")
    @Expose
    var voter: Int = 0,
    @SerializedName("percentage")
    @Expose
    val percentage: String = "",
    @SerializedName("is_selected")
    @Expose
    var isIsSelected: Boolean = false
){
}
