package com.tokopedia.recentview.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Badge {
    @SerializedName("title")
    @Expose
    var title: String = ""
    //sometimes it's different at ws (sometimes image_url and sometimes imgurl)
    @SerializedName("image_url")
    @Expose
    var imageUrl: String = ""

}