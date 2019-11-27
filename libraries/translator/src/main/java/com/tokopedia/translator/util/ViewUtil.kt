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
import java.util.ArrayList

internal object ViewUtil {
    fun getChildren(viewGroup: View?): List<View> {
        val unTraversedViews = ArrayList<View>()
        val traversedViews = ArrayList<View>()
        unTraversedViews.add(viewGroup!!)

        while (unTraversedViews.isNotEmpty()) {
            val child = unTraversedViews.removeAt(0)
            traversedViews.add(child)
            if (child !is ViewGroup) {
                continue
            }

            for (counter in 0 until child.childCount) {
                unTraversedViews.add(child.getChildAt(counter))
            }
        }

        return traversedViews
    }

    fun getContentView(activity: Activity?): View? {
        return activity?.window?.decorView?.findViewById(android.R.id.content)
    }
}