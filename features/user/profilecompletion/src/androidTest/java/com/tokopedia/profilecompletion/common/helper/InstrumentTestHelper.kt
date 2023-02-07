package com.tokopedia.profilecompletion.common.helper

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.repeatedlyUntil
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.profileinfo.view.viewholder.ProfileInfoItemViewHolder
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.TextFieldUnify2
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
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
    if (action == null) {
        Espresso.onView(withId(R.id.nested_scroll_view)).perform(repeatedlyUntil(swipeUp(), hasDescendant(withText(title)), 10), click())
    }
    else {
        Espresso.onView(withId(R.id.nested_scroll_view)).perform(repeatedlyUntil(swipeUp(), hasDescendant(withText(title)), 10))
        Espresso.onView(withId(R.id.fragmentProfileInfoRv)).perform(RecyclerViewActions.scrollToHolder(matcher), RecyclerViewActions.actionOnHolderItem(matcher, action))
    }

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
            click().perform(uiController, view.findViewById(resId))
        }

    }
}

fun goToAnotherActivity(type: Type?, specifyClass: Boolean = true) {
    val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
    if (specifyClass) Intents.intending(IntentMatchers.hasComponent(type!!::class.java.name)).respondWith(result)
    else Intents.intending(IntentMatchers.anyIntent()).respondWith(result)
}

fun checkToasterShowing(message: String) {
    Thread.sleep(1000)
    Espresso.onView(withSubstring(message)).perform().check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}

fun isViewsExists(resIds: List<Int>) {
    resIds.forEach { id ->
        Espresso.onView(withId(id)).check(matches(isDisplayed()))
    }
}

fun isViewsNotExists(vararg resIds: Int) {
    resIds.forEach { id ->
        Espresso.onView(withId(id)).check(matches(not(isDisplayed())))
    }
}

fun checkTextOnEditText(id: Int, text: String) {
    Espresso.onView(allOf(supportsInputMethods(), isDescendantOfA(withId(id)))).check(matches(withText(text)))
}

fun typingTextOn(id: Int, text: String) {
    Espresso.onView(allOf(supportsInputMethods(), isDescendantOfA(withId(id))))
            .perform(clearText(), typeText(text))
    Thread.sleep(3000)
}

fun clickSubmitButton(id: Int) {
    Espresso.onView(withId(id)).check(matches(isEnabled()))
    Espresso.onView(withId(id)).perform(click())
    Thread.sleep(3000)
}

fun clickOnView(id: Int) {
    Espresso.onView(withId(id)).check(matches(isEnabled())).perform(click())
}

fun swipeUp(id: Int) {
    Espresso.onView(withId(id)).perform(swipeUp())
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

fun isDisplayed(vararg resIds: Int) {
    resIds.forEach {
        Espresso.onView(withId(it))
            .check(matches(isDisplayed()))
    }
}

fun isEnable(isEnabled: Boolean, resId: Int) {
    Espresso.onView(withId(resId))
        .check(
            matches(
                if (isEnabled) isEnabled() else not(isEnabled())
            )
        )
}

fun clearText(inputType: Int) {
    Espresso.onView(ViewMatchers.withInputType(inputType))
        .perform(clearText(), ViewActions.closeSoftKeyboard())
}

fun isTextDisplayed(vararg texts: String) {
    texts.forEach {
        Espresso.onView(withText(it))
            .check(matches(isDisplayed()))
    }
}

fun clickOnDisplayedView(resId: Int) {
    Espresso.onView(withId(resId))
        .check(matches(isDisplayed()))
        .perform(click())
}

fun inputTextThenCloseKeyboard(inputType: Int, text: String) {
    Espresso.onView(ViewMatchers.withInputType(inputType))
        .perform(typeText(text), ViewActions.closeSoftKeyboard())
}
