package com.tokopedia.tokopedianow.test.util

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.ViewFinder
import org.hamcrest.Matcher
import java.lang.reflect.Field

class ViewIdlingResource(
    private val viewMatcher: Matcher<View>,
    private val idleMatcher: Matcher<View?>
) : IdlingResource {

    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun isIdleNow(): Boolean {
        val view = getView(viewMatcher)
        val idle = idleMatcher.matches(view)
        if (idle && resourceCallback != null) {
            resourceCallback!!.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    override fun getName(): String {
        return "$this$viewMatcher"
    }

    companion object {
        private fun getView(viewMatcher: Matcher<View>): View? {
            return try {
                val viewInteraction = onView(viewMatcher)
                val finderField: Field = viewInteraction.javaClass.getDeclaredField("viewFinder")
                finderField.isAccessible = true
                val finder = finderField.get(viewInteraction) as ViewFinder
                finder.view
            } catch (e: Exception) {
                null
            }
        }
    }
}
