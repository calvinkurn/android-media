@file:Suppress("NAME_SHADOWING")

package com.tokopedia.play.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Matcher


/**
 * Custom ViewAction to click on a child view with specified id
 */
fun clickOnViewChild(viewId: Int) = object : ViewAction {
    override fun getConstraints() = null

    override fun getDescription() = "Click on a child view with specified id."

    override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
}

fun orientationChangeAction(orientation: Int) = object : ViewAction {

    override fun getDescription(): String = "change orientation to $orientation"

    override fun getConstraints(): Matcher<View> = isRoot()

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        var activity = getActivity(view.context)
        if (activity == null && view is ViewGroup) {
            val c = view.childCount
            var i = 0
            while (i < c && activity == null) {
                activity = getActivity(view.getChildAt(i).context)
                ++i
            }
        }
        activity?.requestedOrientation = orientation
    }

    private fun getActivity(context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
}

fun orientationLandscape(): ViewAction {
    return orientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
}

fun orientationPortrait(): ViewAction {
    return orientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
}

fun isKeyboardShown(targetContext: Context): Boolean {
    val inputMethodManager = targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.isAcceptingText
}

/**
 * Register / unregister IdlingResource
 */
fun register(resource: IdlingResource) = IdlingRegistry.getInstance().register(resource)
fun unregister(resource: IdlingResource) = IdlingRegistry.getInstance().unregister(resource)