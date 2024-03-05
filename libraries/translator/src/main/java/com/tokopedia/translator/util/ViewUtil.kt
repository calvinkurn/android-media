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
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


internal object ViewUtil {

    suspend fun getChildren(viewGroup: View?): List<View?> {
        return withContext(Dispatchers.Default) {
            sequence {

                val unTraversedViews = ArrayDeque<View>()

                viewGroup?.let {
                    unTraversedViews.add(it)
                    while (unTraversedViews.isNotEmpty()) {
                        val child = unTraversedViews.pop()

                        yield(child)

                        if (child is ViewGroup) {
                            for (counter in 0 until child.childCount) {
                                unTraversedViews.add(child.getChildAt(counter))
                            }
                        }
                    }
                }
            }.toList()
        }
    }

    fun getChildrenViews(viewGroup: View?): List<TextView> {
        val traversedViews = mutableSetOf<TextView>()

        viewGroup?.let {
            traverseChildrenViews(it, traversedViews)
        }

        return traversedViews.toList()
    }

    private fun traverseChildrenViews(
        viewGroup: View,
        traversedViews: MutableSet<TextView>
    ) {

        if (viewGroup is TextView) {
            traversedViews.add(viewGroup)
        }

        if (viewGroup is ViewGroup) {
            with(viewGroup) {
                for (i in 0 until childCount) {
                    try {
                        traverseChildrenViews(getChildAt(i), traversedViews)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
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
