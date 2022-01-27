package com.tokopedia.chat_common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AttachmentVariant(
        @SerializedName("option")
        @Expose
        val options: AttachmentVariantOption = AttachmentVariantOption()
)