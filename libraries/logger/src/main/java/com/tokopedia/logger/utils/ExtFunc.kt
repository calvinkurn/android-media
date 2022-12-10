package com.tokopedia.logger.utils

import com.tokopedia.logger.model.newrelic.NewRelicBodyApi
import com.tokopedia.logger.model.newrelic.NewRelicConfig

fun MutableMap<String, NewRelicBodyApi>.addValue(key: String, value: String, nrUid: String, nrApiKey: String) {
    if (containsKey(key)) {
        val newVal = this.getValue(key).messageList.toMutableList()
        newVal.add(value)
        this[key] = NewRelicBodyApi(NewRelicConfig(nrUid, nrApiKey), newVal.toList())
    } else {
        this[key] = NewRelicBodyApi(NewRelicConfig(nrUid, nrApiKey), listOf(value))
    }
}
