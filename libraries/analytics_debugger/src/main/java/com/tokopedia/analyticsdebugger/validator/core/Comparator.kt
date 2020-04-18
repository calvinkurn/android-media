package com.tokopedia.analyticsdebugger.validator.core

import com.google.gson.internal.LinkedTreeMap

internal fun List<Map<String, Any>>.compare(arr: List<Map<String, Any>>): Boolean {
    if (this.size > 1) throw Exception("Tracker Query array should only contains one element")
    for (map in arr) {
        if (!this[0].subSetOf(map)) return false
    }
    return true
}

internal fun Map<String, Any>.subSetOf(obj: Map<String, Any>): Boolean {
    for (entry in this) {
        if (!obj.containsKey(entry.key)) return false
        val objVal = obj[entry.key]
        if (!entry.value.eqSubSet(objVal!!)) return false
    }
    return true
}

@Suppress("UNCHECKED_CAST")
private fun Any.eqSubSet(v: Any): Boolean = when {
    this is LinkedTreeMap<*, *> && v is LinkedTreeMap<*, *> -> (this as Map<String, Any>).subSetOf(v as Map<String, Any>)
    this is ArrayList<*> && v is ArrayList<*> -> (this as List<Map<String, Any>>).compare(v as List<Map<String, Any>>)
    this is String && v is String && this.contains("\\{\\{.*}}".toRegex()) -> regexCompare(this, v)
    else -> this == v
}

fun regexCompare(s: String, v: String): Boolean {
    val syntax = Regex("\\{\\{(.*)}}")
    val m = syntax.find(s)

    val regex: Regex = m?.groupValues?.get(1)?.toRegex()
            ?: throw Exception("Syntax not parsed")

    return regex.matchEntire(v) != null
}
