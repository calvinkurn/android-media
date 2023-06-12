package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName

data class PicObjUpload(
    @SerializedName("file_name")
    var fileName: String?,
    @SerializedName("file_path")
    var filePath: String?,
)
