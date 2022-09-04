package com.tokopedia.profilecompletion.common.helper

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.TextFieldUnify2
import kotlinx.android.synthetic.main.item_empty_field_setting_profile.view.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.contains
import org.hamcrest.TypeSafeMatcher
import java.lang.reflect.Type
import java.util.concurrent.TimeoutException

fun clickViewHolder(title: String, action: ViewAction? = null) {
    val matcher = object : BoundedMatcher<RecyclerView.ViewHolder, ProfileInfoItemViewHolder>(ProfileInfoItemViewHolder::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("view holder with title: $title")
        }

        override fun matchesSafely(item: ProfileInfoItemViewHolder?): Boolean {
            return item?.binding?.fragmentProfileItemTitle?.text?.toString().equals(title, ignoreCase = true)
        }
    }
    if (action == null)
        Espresso.onView(ViewMatchers.withId(R.id.fragmentProfileInfoRv)).perform(RecyclerViewActions.scrollToHolder(matcher), RecyclerViewActions.actionOnHolderItem(matcher, ViewActions.click()))
    else
        Espresso.onView(ViewMatchers.withId(R.id.fragmentProfileInfoRv)).perform(RecyclerViewActions.scrollToHolder(matcher), RecyclerViewActions.actionOnHolderItem(matcher, action))
}

fun <T : View> clickChildWithViewId(resId: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? {
            return null
        }

        override fun getDescription(): String {
            return "Click on a child view with specified id."
        }

        override fun perform(uiController: UiController, view: View) {
            ViewActions.click().perform(uiController, view.findViewById(resId))
        }

    }
}

fun goToAnotherActivity(type: Type?, specifyClass: Boolean = true) {
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
    if (specifyClass) Intents.intending(IntentMatchers.hasComponent(type!!::class.java.name)).respondWith(result)
    else Intents.intending(IntentMatchers.anyIntent()).respondWith(result)
}

fun waitForView(viewId: Int, timeout: Long): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return isRoot()
        }

        override fun getDescription(): String {
            return "wait for a specific view with id $viewId; during $timeout millis."
        }

        override fun perform(uiController: UiController, rootView: View) {
            uiController.loopMainThreadUntilIdle()
            val startTime = System.currentTimeMillis()
            val endTime = startTime + timeout
            val viewMatcher = withId(viewId)

            do {
                // Iterate through all views on the screen and see if the view we are looking for is there already
                for (child in TreeIterables.breadthFirstViewTraversal(rootView)) {
                    // found view with required ID
                    if (viewMatcher.matches(child)) {
                        return
                    }
                }
                // Loops the main thread for a specified period of time.
                // Control may not return immediately, instead it'll return after the provided delay has passed and the queue is in an idle state again.
                uiController.loopMainThreadForAtLeast(100)
            } while (System.currentTimeMillis() < endTime) // in case of a timeout we throw an exception -> test fails
            throw PerformException.Builder()
                    .withCause(TimeoutException())
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(rootView))
                    .build()
        }
    }
}

fun checkToasterShowing(message: String) {
    Thread.sleep(1000)
    Espresso.onView(withSubstring(message)).perform().check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
}

fun isViewsExists(resIds: List<Int>) {
    resIds.forEach { id ->
        Espresso.onView(withId(id)).check(matches(isDisplayed()))
    }
}

fun checkTextOnEditText(id: Int, text: String) {
    Espresso.onView(allOf(supportsInputMethods(), isDescendantOfA(withId(id)))).check(matches(withText(text)))
}

fun typeTextOnEditText(id: Int, text: String) {
    Espresso.onView(allOf(supportsInputMethods(), isDescendantOfA(withId(id))))
            .perform(clearText(), typeText(text))
    Thread.sleep(3000)
}

fun clickSubmitButton(id: Int) {
    Espresso.onView(withId(id)).check(matches(isEnabled()))
    Espresso.onView(withId(id)).perform(click())
    Thread.sleep(3000)
}

fun checkMessageText(id:Int, expectedMessage: String) {
    val matcher = object: TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
        }

        override fun matchesSafely(item: View?): Boolean {
            if (item is TextAreaUnify2) {
                val message = item.textInputLayout.helperText ?: return false
                return expectedMessage.equals(message)
            } else if (item is TextFieldUnify2) {
                val message = item.textInputLayout.helperText ?: return false
                return expectedMessage.equals(message)
            } else  {
                return false
            }
        }

    }
    Espresso.onView(withId(id)).check(matches(matcher))
}

fun checkResultCode(activityResult: Instrumentation.ActivityResult, resultCode: Int) {
    assertThat(activityResult, ActivityResultMatchers.hasResultCode(resultCode));
}

