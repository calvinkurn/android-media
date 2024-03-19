package com.tokopedia.translator.viewtree

import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import com.tokopedia.translator.util.ViewUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

internal object ViewTreeManagerFragment {

    private const val INDEX_SEPARATOR = "-"
    private const val MAIN_CONTENT_LAYOUT_NAME = "rootTop"
    private const val FRAGMENT_NAME_SEPARATOR = "@"
    private const val CHILD_SEPARATOR = "->"

    private fun getChildIndexInsideViewGroup(parent: ViewGroup, viewToFindIndex: View): Int {

        var initialIndex = -1

        for (i in 0 until parent.childCount) {
            val view = parent.getChildAt(i)
            ++initialIndex

            if (view === viewToFindIndex) {
                break
            }
        }

        return initialIndex

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

    fun createDOMIdentifier(view: View, fragment: Fragment): String {
        val stringBuilder = StringBuilder()
        var currentView = view

        do {
            if (currentView.id == Window.ID_ANDROID_CONTENT) {
                stringBuilder.insert(0, fragment.javaClass.name + FRAGMENT_NAME_SEPARATOR + MAIN_CONTENT_LAYOUT_NAME)
                break
            } else {
                stringBuilder.insert(
                    0,
                    CHILD_SEPARATOR + currentView.javaClass.simpleName + INDEX_SEPARATOR +
                        getChildIndexInsideViewGroup(currentView.parent as ViewGroup, currentView)
                )
            }

            currentView = currentView.parent as View
        } while (true)

        return stringBuilder.toString()
    }

    suspend fun findViewByDOMIdentifier(domIdentifier: String, fragment: Fragment): View? {
        return withContext(Dispatchers.Default) {

            val activitySplitting =
                domIdentifier.split(FRAGMENT_NAME_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (!activitySplitting[0].equals(fragment::class.java.name, ignoreCase = true)) {
                return@withContext null
            }

            val viewSplitting =
                activitySplitting[1].split(CHILD_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var viewLooker: View? = null

            if (viewSplitting[0].equals(MAIN_CONTENT_LAYOUT_NAME, ignoreCase = true)) {
                viewLooker = ViewUtil.getContentView(fragment)
            }

            return@withContext lookupForView(viewLooker, Arrays.copyOfRange(viewSplitting, 1, viewSplitting.size))
        }
    }


}
