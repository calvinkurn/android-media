package com.tokopedia.analyticsdebugger.cassava.validator.core

import com.google.gson.internal.LinkedTreeMap
import com.tokopedia.analyticsdebugger.cassava.data.CassavaValidateResult
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
        if (map.canValidate(mapGtm, isExact).isValid) {
            return true
        }
    }
    return false
}

/**
 * Method to validate Regex from local json/thanos with GTM Log
 *
 * currently we use Event Name and Event Label as unique identifier to determine is that log is the expected log or not
 * if it is the expected one, we will get the error message if some of the query not valid
 * if it is not, we won't get the error message
 *
 * we do this to make sure the error message is related with the regex that developer testing,
 */
internal fun Map<String, Any>.canValidate(obj: Map<String, Any>, strict: Boolean = false): CassavaValidateResult {
    val isNeedCause = this.haveSameEventAndLabel(obj)

    for (entry in this) {
        if (!obj.containsKey(entry.key))
            return CassavaValidateResult(false,
                    if (isNeedCause) "Key \"${entry.key}\" not found in GTM Log" else "")
        val objVal = obj[entry.key]
        val eqResult = entry.value.eq(objVal!!)
        if (!eqResult.isValid) {
            if (isNeedCause && eqResult.errorCause.isNotEmpty()) {
                eqResult.errorCause += " in field ${entry.key}"
            } else {
                eqResult.errorCause = ""
            }

            return eqResult
        }
    }
    return when {
        strict -> CassavaValidateResult(this.size == obj.size, if (isNeedCause) "Map size is different" else "") // won't use the error message if true
        else -> CassavaValidateResult(true, "")
    }
}

internal fun Map<String, Any>.haveSameEventAndLabel(obj: Map<String, Any>): Boolean =
        (this.containsKey(ValidatorEngine.EVENT_KEY) &&
                obj.containsKey(ValidatorEngine.EVENT_KEY) &&
                this[ValidatorEngine.EVENT_KEY]!!.eq(obj[ValidatorEngine.EVENT_KEY]!!).isValid)
                && (this.containsKey(ValidatorEngine.EVENT_LABEL_KEY) &&
                obj.containsKey(ValidatorEngine.EVENT_LABEL_KEY) &&
                this[ValidatorEngine.EVENT_LABEL_KEY]!!.eq(obj[ValidatorEngine.EVENT_LABEL_KEY]!!).isValid)

private fun List<Map<String, Any>>.validateArray(arr: List<Map<String, Any>>): CassavaValidateResult {
    if (this.size > 1) throw ArrayStoreException("Tracker Query array should only contains one element")
    for (map in arr) {
        val result = this[0].canValidate(map)
        if (!result.isValid) return result
    }
    return CassavaValidateResult(true, "")
}

private fun Any.eq(v: Any): CassavaValidateResult = when {
    this is LinkedTreeMap<*, *> && v is LinkedTreeMap<*, *> -> (this as Map<String, Any>).canValidate(v as Map<String, Any>)
    this is ArrayList<*> && v is ArrayList<*> -> (this as List<Map<String, Any>>).validateArray(v as List<Map<String, Any>>)
    this is String -> CassavaValidateResult(regexEquals(this, v), "Regex \"$this\" didn't match with \"$v\"")
    else -> CassavaValidateResult(this == v, "\"$this\" is not equals to \"$v\"")
}

fun regexEquals(s: String, v: Any): Boolean {
    val regex = Regex(s)
    val vString = v.toString()
    return regex.matchEntire(vString) != null
}
