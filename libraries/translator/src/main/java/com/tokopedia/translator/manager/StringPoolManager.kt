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

import com.tokopedia.translator.repository.model.StringPoolItem

class StringPoolManager {

    private val mPools: HashMap<String, StringPoolItem> = HashMap()

    fun add(current: String, newStr: String) {
        mPools.put(current.trim(), StringPoolItem(current, newStr))
    }

    fun get(current: String?): StringPoolItem? {
        if (current == null) {
            return null
        }

        return mPools.get(current.trim())
    }

    fun updateCache(old: Array<String>, new: Array<String>) {
        if (old.size != new.size)
            return

        for (i in 0 until old.size) {
            add(old[i].trim(), new[i])
        }
    }


    fun getQueryString(): String {
        val builder = StringBuilder()

        for ((key, data) in mPools) {

            if (data.demandedText.isEmpty()) {
                builder.append(data.originalText)
                    .append(TranslatorManager.DELIM)
            }
        }

        val length = builder.length

        if (length > 0) {
            builder.deleteCharAt(length - 1)
        }

        return builder.toString()
    }

    override fun toString(): String {
        return "StringPoolManager(mPools=$mPools)"
    }
}
