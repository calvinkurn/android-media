package com.tokopedia.topchat.chatroom.data

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class UploadImageDummy (
    var messageId: String = "",
    var visitable: Visitable<*>? = null,
    var isFail: Boolean = false
)