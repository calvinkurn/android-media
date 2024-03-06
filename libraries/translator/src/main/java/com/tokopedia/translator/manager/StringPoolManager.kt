/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.translator.manager

import android.widget.TextView
import com.tokopedia.translator.repository.model.StringPoolItem

class StringPoolManager {

    private val mPools: HashMap<String, StringPoolItem> = HashMap()

    fun add(textView: TextView, current: String, newStr: String, destinationLang: String) {
        mPools.put(current.trim(), StringPoolItem(textView, current, newStr, destinationLang))
    }

    fun get(current: String?): StringPoolItem? {
        if (current == null) {
            return null
        }

        return mPools.get(current.trim())
    }

    fun clearPools() {
        mPools.clear()
    }

    fun updateCache(textViews: List<TextView>, old: List<String>, new: Array<String>, destinationLang: String) {
        if (old.size != new.size)
            return

        for (i in 0 until old.size) {
            add(textViews[i], old[i].trim(), new[i], destinationLang)
        }
    }


    fun getQueryStrList(): List<String> {
        val queryStrList = ArrayList<String>()

        for ((_, data) in mPools) {

            if (data.demandedText.isBlank() || data.textView.text != data.demandedText) {
                queryStrList.add(data.originalText)
            }
        }

        return queryStrList.toList()
    }

    override fun toString(): String {
        return "StringPoolManager(mPools=$mPools)"
    }

    companion object {

        private var LOCK = Any()

        @JvmStatic
        private var sInstance: StringPoolManager? = null
        fun getInstance(): StringPoolManager {
            return sInstance ?: synchronized(LOCK) {
                StringPoolManager().also {
                    sInstance = it
                }
            }
        }
    }
}
