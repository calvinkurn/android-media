package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.google.gson.internal.LinkedTreeMap
import com.tokopedia.analyticsdebugger.database.GtmLogDB

fun Map<String, Any>.containsPairOf(pair: Pair<String, String>): Boolean {
    forEach {
        when {
            it.key == pair.first && regexEquals(pair.second, it.value) -> return true
            it.value is LinkedTreeMap<*, *> -> return (it.value as Map<String, Any>).containsPairOf(pair)
        }
    }
    return false
}

fun List<GtmLogDB>.containsMapOf(map: Map<String, Any>, isExact: Boolean): Boolean {
    for (gtm in this) {
        val mapGtm = gtm.data.toJsonMap()
        if (map.canValidateWithErrorMessage(mapGtm, isExact).first) {
            return true
        }
    }
    return false
}

internal fun Map<String, Any>.canValidate(obj: Map<String, Any>, strict: Boolean = false): Boolean {
    for (entry in this) {
        if (!obj.containsKey(entry.key)) return false
        val objVal = obj[entry.key]
        if (!entry.value.eq(objVal!!).first) return false
    }
    return when {
        strict -> this.size == obj.size
        else -> true
    }
}

internal fun Map<String, Any>.canValidateWithErrorMessage(obj: Map<String, Any>, strict: Boolean = false): Pair<Boolean, String> {
    for (entry in this) {
        if (!obj.containsKey(entry.key)) return Pair(false, "Key \"${entry.key}\" not found in GTM Log")
        val objVal = obj[entry.key]
        val eqResult = entry.value.eq(objVal!!)
        if (!eqResult.first) {
            var errorString = eqResult.second
            errorString += " in field ${entry.key}"
            return Pair(eqResult.first, errorString)
        }
    }
    return when {
        strict -> Pair(this.size == obj.size, "Map size is different") // won't use the error message if true
        else -> Pair(true, "")
    }
}

internal fun Map<String, Any>.haveSameEventAndLabel(obj: Map<String, Any>): Boolean =
        (this.containsKey(ValidatorEngine.EVENT_KEY) &&
                obj.containsKey(ValidatorEngine.EVENT_KEY) &&
                this[ValidatorEngine.EVENT_KEY]!!.eq(obj[ValidatorEngine.EVENT_KEY]!!).first)
                && (this.containsKey(ValidatorEngine.EVENT_LABEL_KEY) &&
                obj.containsKey(ValidatorEngine.EVENT_LABEL_KEY) &&
                this[ValidatorEngine.EVENT_LABEL_KEY]!!.eq(obj[ValidatorEngine.EVENT_LABEL_KEY]!!).first)

private fun List<Map<String, Any>>.validateArray(arr: List<Map<String, Any>>): Pair<Boolean, String> {
    if (this.size > 1) throw Exception("Tracker Query array should only contains one element")
    for (map in arr) {
        val result = this[0].canValidateWithErrorMessage(map)
        if (!result.first) return result
    }
    return Pair(true, "")
}

private fun Any.eq(v: Any): Pair<Boolean, String> = when {
    this is LinkedTreeMap<*, *> && v is LinkedTreeMap<*, *> -> (this as Map<String, Any>).canValidateWithErrorMessage(v as Map<String, Any>)
    this is ArrayList<*> && v is ArrayList<*> -> (this as List<Map<String, Any>>).validateArray(v as List<Map<String, Any>>)
    this is String -> Pair(regexEquals(this, v), "Regex \"$this\" didn't match with \"$v\"")
    else -> Pair(this == v, "\"$this\" is not equals to \"$v\"")
}

fun regexEquals(s: String, v: Any): Boolean {
    val regex = Regex(s)
    val vString = v.toString()
    return regex.matchEntire(vString) != null
}
