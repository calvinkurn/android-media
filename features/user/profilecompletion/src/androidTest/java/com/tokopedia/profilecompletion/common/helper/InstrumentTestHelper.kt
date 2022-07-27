package com.tokopedia.profilecompletion.common.helper

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.lang.reflect.Type

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

fun checkToasterShowing(message: String) {
    Espresso.onView(ViewMatchers.withSubstring(message)).check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE
    )));
}

fun isViewsExists(resIds: List<Int>) {
    resIds.forEach { id ->
        Espresso.onView(ViewMatchers.withId(id)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}