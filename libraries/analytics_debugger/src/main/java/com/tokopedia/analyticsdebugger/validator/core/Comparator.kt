package com.tokopedia.analyticsdebugger.validator.core

import com.google.gson.internal.LinkedTreeMap

internal fun Map<String, Any>.compare(obj: Map<String, Any>, strict: Boolean = false): Boolean {
    for (entry in this) {
        if (!obj.containsKey(entry.key)) return false
        val objVal = obj[entry.key]
        if (!entry.value.eq(objVal!!)) return false
    }
    return when {
        strict -> this.size == obj.size
        else -> true
    }
}

@Suppress("UNCHECKED_CAST")
private fun Any.eq(v: Any): Boolean = when {
    this is LinkedTreeMap<*, *> && v is LinkedTreeMap<*, *> -> (this as Map<String, Any>).compare(v as Map<String, Any>)
    this is ArrayList<*> && v is ArrayList<*> -> (this as List<Map<String, Any>>).compare(v as List<Map<String, Any>>)
    this is String && v is String && this.contains("\\{\\{.*}}".toRegex()) -> regexCompare(this, v)
    else -> this == v
}

private fun List<Map<String, Any>>.compare(arr: List<Map<String, Any>>): Boolean {
    if (this.size > 1) throw Exception("Tracker Query array should only contains one element")
    for (map in arr) {
        if (!this[0].compare(map)) return false
    }
    return true
}

fun regexCompare(s: String, v: String): Boolean {
    val syntax = Regex("\\{\\{(.*)}}")
    val m = syntax.find(s)

    val regex: Regex = m?.groupValues?.get(1)?.toRegex()
            ?: throw Exception("Syntax not parsed")

    return regex.matchEntire(v) != null
}
