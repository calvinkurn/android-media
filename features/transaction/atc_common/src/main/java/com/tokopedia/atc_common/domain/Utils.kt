package com.tokopedia.atc_common.domain

/**
 * Created by Irfan Khoirul on 2019-07-15.
 */

fun StringBuilder.replaceParam(source: StringBuilder, key: String, value: String): StringBuilder {
    return source.replace(source.indexOf(key), source.indexOf(key) + key.length, value)
}
