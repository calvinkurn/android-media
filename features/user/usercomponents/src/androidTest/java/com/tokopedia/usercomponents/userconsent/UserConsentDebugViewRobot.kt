package com.tokopedia.usercomponents.userconsent

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.common.utils.setTextOnTextFieldUnify2
import org.hamcrest.CoreMatchers


fun userUserConsentDebugViewRobot(action: UserConsentDebugViewRobot.() -> Unit): UserConsentDebugViewRobot {
    return UserConsentDebugViewRobot().apply(action)
}

class UserConsentDebugViewRobot {
    fun loadConsentCollection(
        collectionId: String,
        version: String = "",
        actionButton: String = "Action",
    ) {
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.textCollextionId),
            ViewMatchers.isDisplayed()))
            .perform(setTextOnTextFieldUnify2(collectionId, R.id.textCollextionId),
                ViewActions.closeSoftKeyboard())

        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.textCollextionVersion),
            ViewMatchers.isDisplayed()))
            .perform(setTextOnTextFieldUnify2(version, R.id.textCollextionVersion),
                ViewActions.closeSoftKeyboard())

        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.textActionButton),
            ViewMatchers.isDisplayed()))
            .perform(setTextOnTextFieldUnify2(actionButton, R.id.textActionButton),
                ViewActions.closeSoftKeyboard())

        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(R.id.buttonLoadConsent),
            ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }
}
