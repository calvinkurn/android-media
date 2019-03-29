package com.tokopedia.trackingoptimizer.gson

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by hendry on 26/12/18.
 */
class HashMapJsonUtil {
    companion object {
        fun mapToJson(map: Map<String, Any>?): String? {
            if (map == null) {
                return null
            }
            return GsonSingleton.instance.toJson(map)
        }

        fun jsonToMap(json: String): HashMap<String, Any>? {
            try {
                val jsonObject = JSONObject(json)
                return jsonToMap(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }
        }

        fun jsonToMap(json: JSONObject?): HashMap<String, Any>? {
            var retMap: HashMap<String, Any>? = HashMap()

            if (json !== JSONObject.NULL) {
                retMap = toMap(json)
            }
            return retMap
        }

        fun findList(map: HashMap<*, *>?): ArrayList<Any>? {
            if (map == null) {
                return null
            }
            for (value in map.values) {
                if (value is ArrayList<*>) {
                    return value as ArrayList<Any>
                } else if (value is HashMap<*, *>) {
                    val list = findList(value)
                    if (list != null) {
                        return list
                    }
                }
            }
            return null
        }

        private fun toMap(obj: JSONObject?): HashMap<String, Any>? {
            if (obj == null) {
                return null
            }
            try {
                val map = HashMap<String, Any>()

                val keysItr = obj.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    var value = obj.get(key)

                    if (value is JSONArray) {
                        value = toList(value)
                    } else if (value is JSONObject) {
                        value = toMap(value)
                    }
                    map[key] = value
                }
                return map
            } catch (e: Exception) {
                return null
            }
        }

        private fun toList(array: JSONArray?): List<Any>? {
            if (array == null) {
                return null
            }
            val list = ArrayList<Any>()
            for (i in 0 until array.length()) {
                var value = array.get(i)
                if (value is JSONArray) {
                    value = toList(value)
                } else if (value is JSONObject) {
                    value = toMap(value)
                }
                list.add(value)
            }
            return list
        }
    }

}