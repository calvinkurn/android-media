package com.tokopedia.chat_common.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FallbackAttachmentPojo {

    @SerializedName("message")
    @Expose
    var message:  String = ""
    @SerializedName("url")
    @Expose
    var url:  String = ""
    @SerializedName("span")
    @Expose
    var span:  String = ""
    @SerializedName("html")
    @Expose
    var html:  String = ""

}
