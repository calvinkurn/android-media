package com.tokopedia.common_category.util

import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.ArrayList


class ParamMapToUrl {

    companion object {
        @JvmStatic
        fun <T> generateUrlParamString(paramMap: Map<String, T>?): String {
            if (paramMap == null) {
                return ""
            }

            val paramList = ArrayList<String>()

            for (entry in paramMap.entries) {
                if (entry.value == null) continue

                addParamToList<T>(paramList, entry)
            }

            return TextUtils.join("&", paramList)
        }

        private fun <T> addParamToList(paramList: MutableList<String>, entry: Map.Entry<String, T>) {
            try {
                paramList.add(entry.key + "=" + URLEncoder.encode(entry.value.toString(), "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
    }
}

