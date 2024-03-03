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
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.ArrayDeque
import java.util.LinkedList


internal object ViewUtil {

    suspend fun getChildren(viewGroup: View?): List<TextView> {
        return withContext(Dispatchers.Main) {
            val unTraversedViews = ArrayDeque<View>()
            val traversedViews = ArrayList<TextView>()

            unTraversedViews.add(viewGroup!!)

            while (unTraversedViews.isNotEmpty()) {
                val child = unTraversedViews.poll()

                if (child != null) {
                    if (child is TextView) {
                        traversedViews.add(child)
                    }

                    if (child is ViewGroup) {
                        for (counter in 0 until child.childCount) {
                            unTraversedViews.add(child.getChildAt(counter))
                        }
                    }
                }
            }

            traversedViews.toList()
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
