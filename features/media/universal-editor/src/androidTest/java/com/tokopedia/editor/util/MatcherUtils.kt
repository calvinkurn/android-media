package com.tokopedia.editor.util

import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun isAlignmentMatch(alignment: FontAlignment): TypeSafeMatcher<View> {
    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View?): Boolean {
            view?.let {
                return try {
                    (it as TextView).textAlignment == alignment.value
                } catch (e: Exception) {
                    false
                }
            }
            return false
        }

        override fun describeTo(description: Description?) {}
    }
}
