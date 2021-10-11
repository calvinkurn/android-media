package com.tokopedia.graphql.util

import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author by nisie on 12/02/19.
 */

fun getParamString(paramName: String,
                   arguments: Bundle?,
                   savedInstanceState: Bundle?,
                   defaultValue : String = ""): String {
    return when {
        savedInstanceState != null -> savedInstanceState.getString(paramName, defaultValue)
        arguments != null -> arguments.getString(paramName, defaultValue)
        else -> defaultValue
    }
}

fun getParamInt(paramName: String,
                   arguments: Bundle?,
                   savedInstanceState: Bundle?,
                   defaultValue : Int = 0): Int {
    return when {
        savedInstanceState != null -> savedInstanceState.getInt(paramName, defaultValue)
        arguments != null -> arguments.getInt(paramName, defaultValue)
        else -> defaultValue
    }
}

fun getParamBoolean(paramName: String,
                arguments: Bundle?,
                savedInstanceState: Bundle?,
                defaultValue : Boolean): Boolean {
    return when {
        savedInstanceState != null -> savedInstanceState.getBoolean(paramName, defaultValue)
        arguments != null -> arguments.getBoolean(paramName, defaultValue)
        else -> defaultValue
    }
}

fun <T> T.toMapParam(): Map<String, Any> {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
}
