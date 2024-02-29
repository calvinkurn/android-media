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
package com.tokopedia.translator.util

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.translator.manager.StringPoolManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal object ViewUtil {

    suspend fun getChildren(viewGroup: View?, stringPoolManager: StringPoolManager?): List<View> {
        return withContext(Dispatchers.Default) {
            val unTraversedViews = ArrayList<View>()
            val traversedViews = ArrayList<View>()
            unTraversedViews.add(viewGroup!!)

            while (unTraversedViews.isNotEmpty()) {
                val child = unTraversedViews.removeAt(0)

                if (child is TextView && child !is EditText) {
                    val stringPollItem = stringPoolManager?.get(child.text?.toString())
                    if (stringPollItem?.demandedText != child.text) {
                        traversedViews.add(child)
                    }
                }

                if (child !is ViewGroup) {
                    continue
                }

                for (counter in 0 until child.childCount) {
                    unTraversedViews.add(child.getChildAt(counter))
                }
            }
            traversedViews
        }
    }

    private fun getViewGroupId(viewGroup: View, activity: Activity): String {
        return try {
            val className = activity.javaClass.simpleName
            val layoutResourceId = viewGroup.id
            val fileName = activity.resources.getResourceEntryName(layoutResourceId)
            "$className@$fileName@$layoutResourceId"
        } catch (e: Exception) {
            ""
        }
    }

    fun getContentView(activity: Activity?): View? {
        return activity?.window?.decorView?.findViewById(android.R.id.content)
    }

    fun getContentView(fragment: Fragment?): View? {
        return fragment?.view
    }
}
