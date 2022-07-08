package com.tokopedia.usercomponents.userconsent

import com.tokopedia.usercomponents.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.usercomponents.common.utils.clickChildViewWithId
import com.tokopedia.usercomponents.common.utils.clickClickableSpanOnTypographyUnify
import com.tokopedia.usercomponents.common.utils.setTextOnTextFieldUnify2
import com.tokopedia.usercomponents.common.utils.waitOnView
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeViewHolder
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString

fun userConsentRobot(action: UserConsentRobot.() -> Unit): UserConsentRobot {
    return UserConsentRobot().apply(action)
}

infix fun UserConsentRobot.validateComponent(action: UserConsentResult.() -> Unit): UserConsentResult {
    Thread.sleep(2500)
    return UserConsentResult().apply(action)
}

class UserConsentRobot {
    fun clickCheckBoxSingleView() {
        onView(allOf(withId(R.id.checkboxPurposes), isDisplayed()))
            .perform(click())
    }

    fun clickCheckBoxMultipleView(position: Int) {
        onView(withId(R.id.recyclerPurposes))
            .perform(RecyclerViewActions.actionOnItemAtPosition<UserConsentPurposeViewHolder>(
                position,
                clickChildViewWithId<CheckboxUnify>(R.id.checkboxPurposes)
            ))
    }

    fun clickTncHyperlink() {
        waitOnView(allOf(withId(R.id.descriptionPurposes), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Syarat & Ketentuan"))
    }

    fun clickPolicyHyperlink() {
        onView(allOf(withId(R.id.descriptionPurposes), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Kebijakan Privacy"))
    }

    fun clickTncHyperlinkOnMultipleOptional() {
        onView(allOf(withId(R.id.textMainDescription), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Syarat & Ketentuan"))
    }

    fun clickPolicyHyperlinkOnMultipleOptional() {
        onView(allOf(withId(R.id.textMainDescription), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Kebijakan Privacy"))
    }

    fun clickActionButton() {
        onView(allOf(withId(R.id.buttonAction), isDisplayed()))
            .perform(click())
    }
}

class UserConsentResult {
    private val textWithTnc = "Saya menyetujui Syarat & Ketentuan"
    private val textWithTncPolicy = "Saya menyetujui Syarat & Ketentuan serta Kebijakan Privacy"

    private fun generatePurposeText(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>): String {
        var purposeText = ""
        if (purposes.size.orZero() == 1) {
            purposeText = purposes.first().description
        } else {
            purposes.forEachIndexed { index, purposeDataModel ->
                purposeText += when (index) {
                    (purposes.size.orZero() - 1) -> {
                        " & ${purposeDataModel.description}"
                    }

                    (purposes.size.orZero() - 2) -> {
                        purposeDataModel.description
                    }
                    else -> {
                        "${purposeDataModel.description}, "
                    }
                }
            }
        }

        return purposeText
    }

    fun shouldViewTnCMultipleOptional(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>) {
        onView(allOf(withId(R.id.textMainDescription), withText(textWithTnc), isDisplayed()))
        purposes.forEach {
            onView(allOf(withId(R.id.checkboxPurposes), withText(it.description), isDisplayed()))
        }
    }

    fun shouldViewTnCPolicyMultipleOptional(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>) {
        onView(allOf(withId(R.id.textMainDescription), withText(textWithTncPolicy), isDisplayed()))
        purposes.forEach {
            onView(allOf(withId(R.id.checkboxPurposes), withText(it.description), isDisplayed()))
        }
    }

    fun shouldViewTnCMandatory(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>) {
        val text = textWithTnc + generatePurposeText(purposes)
        onView(allOf(withId(R.id.descriptionPurposes), withText(text), isDisplayed()))
    }

    fun shouldViewTncPolicyMandatory(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>) {
        val text = textWithTncPolicy + generatePurposeText(purposes)
        onView(allOf(withId(R.id.descriptionPurposes), withText(text), isDisplayed()))
    }

    fun shouldViewTnCOptional(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>) {
        val text = textWithTnc + generatePurposeText(purposes)
        onView(allOf(withId(R.id.descriptionPurposes), withText(text), isDisplayed()))
    }

    fun shouldViewTncPolicyOptional(purposes: MutableList<UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel>) {
        val text = textWithTncPolicy + generatePurposeText(purposes)
        onView(allOf(withId(R.id.descriptionPurposes), withText(text), isDisplayed()))
    }

    fun shouldButtonActionEnable() {
        onView(allOf(withId(R.id.buttonAction), isDisplayed()))
    }
}