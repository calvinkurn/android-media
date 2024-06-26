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
package com.tokopedia.translator.viewtree

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.tokopedia.translator.util.ViewUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

internal object ViewTreeManager {

    private const val INDEX_SEPARATOR = "-"
    private const val MAIN_CONTENT_LAYOUT_NAME = "rootTop"
    private const val ACTIVITY_NAME_SEPARATOR = "@"
    private const val CHILD_SEPARATOR = "->"

    private fun getChildIndexInsideViewGroup(parent: ViewGroup, viewToFindIndex: View): Int {

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)

            if (view === viewToFindIndex) {
                return i
            }
        }

        return -1
    }

    private fun lookupForView(view: View?, domeIdentifier: Array<String>): View {
        val viewPositioningSplitting = domeIdentifier[0].split(INDEX_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        val index = viewPositioningSplitting.getOrNull(1)?.toIntOrNull() ?: 0
        val childView = (view as ViewGroup).getChildAt(index)

        return if (domeIdentifier.size > 1) {
            lookupForView(
                childView,
                Arrays.copyOfRange(domeIdentifier, 1, domeIdentifier.size)
            )
        } else {
            childView
        }
    }

    fun createDOMIdentifier(view: View, activity: Activity): String {

        var domIdentifier = ""
        var currentView = view
        do {
            if (currentView.id == Window.ID_ANDROID_CONTENT) {
                domIdentifier = "${activity.localClassName}$ACTIVITY_NAME_SEPARATOR$MAIN_CONTENT_LAYOUT_NAME$domIdentifier"
                break
            } else {
                domIdentifier = "$CHILD_SEPARATOR${currentView.javaClass.simpleName}$INDEX_SEPARATOR${
                    getChildIndexInsideViewGroup(
                        currentView.parent as ViewGroup,
                        currentView
                    )
                }$domIdentifier"
            }

            currentView = currentView.parent as View
        } while (true)

        return domIdentifier
    }


    suspend fun findViewByDOMIdentifier(domIdentifier: String, activity: Activity): View? {
        return withContext(Dispatchers.Default) {

            val (activityClassName, childIdentifiers) = domIdentifier.split(ACTIVITY_NAME_SEPARATOR)

            if (activityClassName.equals(activity.localClassName, ignoreCase = true).not()) {
                return@withContext null
            }

            val viewSplitting =
                childIdentifiers.split(CHILD_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            var viewLooker: View? = null

            if (viewSplitting[0].equals(MAIN_CONTENT_LAYOUT_NAME, ignoreCase = true)) {
                viewLooker = ViewUtil.getContentView(activity)
            }

            return@withContext lookupForView(viewLooker, viewSplitting.copyOfRange(1, viewSplitting.size))
        }
    }
}
