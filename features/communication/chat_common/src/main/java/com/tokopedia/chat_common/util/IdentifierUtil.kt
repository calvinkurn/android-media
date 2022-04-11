package com.tokopedia.chat_common.util

import java.util.*

object IdentifierUtil {
    fun generateLocalId(): String {
        return UUID.randomUUID().toString()
    }
}