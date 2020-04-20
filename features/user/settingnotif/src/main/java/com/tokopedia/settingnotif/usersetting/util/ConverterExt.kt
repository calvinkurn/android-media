package com.tokopedia.settingnotif.usersetting.util

import com.tokopedia.graphql.CommonUtils.fromJson
import com.tokopedia.graphql.CommonUtils.toJson

inline fun <reified T> dataClone(src: Any): T {
    val json = toJson(src)
    return fromJson(json, T::class.java)
}