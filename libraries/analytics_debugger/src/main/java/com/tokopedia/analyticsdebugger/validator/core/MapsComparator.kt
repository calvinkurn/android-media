package com.tokopedia.analyticsdebugger.validator.core

import com.google.gson.internal.LinkedTreeMap
import java.util.*
import kotlin.collections.ArrayList

internal fun List<Map<String, Any>>.compare(arr: List<Map<String, Any>>): Boolean {
    for ((i, map) in this.withIndex()) {
        if (!map.subSetOf(arr[i])) return false
    }
    return true
}

internal fun Map<String, Any>.subSetOf(obj: Map<String, Any>): Boolean {
    for (entry in this) {
        if (!obj.containsKey(entry.key)) return false
        val objVal = obj[entry.key]
        if (!entry.value.eqSimilar(objVal!!)) return false
    }
    return true
}

@Suppress("UNCHECKED_CAST")
private fun Any.eqSimilar(v: Any): Boolean = when {
    this is LinkedTreeMap<*, *> && v is LinkedTreeMap<*, *> -> (this as Map<String, Any>).subSetOf(v as Map<String, Any>)
    this is ArrayList<*> && v is ArrayList<*> -> (this as List<Map<String, Any>>).compare(v as List<Map<String, Any>>)
    else -> this == v
}