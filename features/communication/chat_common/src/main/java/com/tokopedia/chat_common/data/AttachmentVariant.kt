package com.tokopedia.chat_common.data

import com.google.gson.annotations.SerializedName

class AttachmentVariant(
    @SerializedName("option")
    val options: AttachmentVariantOption = AttachmentVariantOption()
)
