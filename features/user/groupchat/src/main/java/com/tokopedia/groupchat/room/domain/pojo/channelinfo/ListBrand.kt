package com.tokopedia.groupchat.room.domain.pojo.channelinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListBrand(

        @SerializedName("brand_id")
        @Expose
        var brandId: String = "",
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("image_url")
        @Expose
        var imageUrl: String = "",
        @SerializedName("brand_url")
        @Expose
        var brandUrl: String = ""
) {


}
