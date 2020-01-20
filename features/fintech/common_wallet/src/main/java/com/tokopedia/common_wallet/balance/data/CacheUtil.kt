package com.tokopedia.common_wallet.balance.data

import com.google.gson.Gson
import java.lang.reflect.Type

/**
 * Created by nabillasabbaha on 9/10/19.
 */

object CacheUtil {

    const val KEY_TOKOCASH_BALANCE_CACHE = "TOKOCASH_BALANCE_CACHE"
    const val KEY_POPUP_INTRO_OVO_CACHE = "INTRO_OVO_CACHE"
    const val FIRST_TIME_POPUP = "NEW_FIRST_TIME_POPUP"

    fun convertModelToString(obj: Any, type: Type): String {
        val gson = Gson()

        val element = gson.toJsonTree(obj, type)

        if (!element.isJsonObject) {
            throw RuntimeException()
        }

        return element.asJsonObject.toString()
    }

    fun <T> convertStringToModel(json: String, type: Type): T? {
        val gson = Gson()

        return gson.fromJson<T>(json, type)
    }

}
