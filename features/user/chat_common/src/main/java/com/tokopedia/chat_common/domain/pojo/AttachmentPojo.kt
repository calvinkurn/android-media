package com.tokopedia.chat_common.domain.pojo

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttachmentPojo {

    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("type")
    @Expose
    var type: String = ""
    @SerializedName("attributes")
    @Expose
    var attributes: JsonObject? = null
    @SerializedName("fallback_attachment")
    @Expose
    var fallbackAttachment: FallbackAttachmentPojo? = null
}
