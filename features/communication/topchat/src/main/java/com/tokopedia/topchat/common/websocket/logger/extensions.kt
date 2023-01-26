package com.tokopedia.topchat.common.websocket.logger

import com.tokopedia.cachemanager.gson.GsonSingleton

fun parseInitPayload(payload: String): ParseTopchatWebSocketPayload? {
    return try {
        GsonSingleton
            .instance
            .fromJson(payload, ParseTopchatWebSocketPayload::class.java)
    } catch (ignored: Throwable) {
        null
    }
}
