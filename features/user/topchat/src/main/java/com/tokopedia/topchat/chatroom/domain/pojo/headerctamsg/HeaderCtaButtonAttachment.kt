package com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg

import com.google.gson.annotations.SerializedName

data class HeaderCtaButtonAttachment constructor(
    @SerializedName("cta_button")
    var ctaButton: HeaderCtaMessageAttachment = HeaderCtaMessageAttachment(),
)