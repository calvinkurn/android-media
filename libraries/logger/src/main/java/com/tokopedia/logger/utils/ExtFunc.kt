package com.tokopedia.logger.utils

import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicConfig

fun MutableMap<String, NewRelicBodyApi>.addValue(
    nrUid: String,
    msg: String,
    encryptNrApiKey: String,
    decryptNrKey: ((String) -> (String)),
    deleteLog: () -> Unit
) {
    if (containsKey(nrUid)) {
        val existVal = this.getValue(nrUid)
        val msgList = existVal.messageList.toMutableList()
        msgList.add(msg)
        this[nrUid] = existVal.copy(messageList = msgList.toList())
    } else {
        try {
            val nrApiKey = decryptNrKey.invoke(encryptNrApiKey)
            this[nrUid] = NewRelicBodyApi(NewRelicConfig(nrUid, nrApiKey), listOf(msg))
        } catch (t: Throwable) {
            deleteLog()
        }
    }
}
