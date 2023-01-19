package com.tokopedia.usercomponents.userconsent

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.common.utils.clickChildViewWithId
import com.tokopedia.usercomponents.common.utils.clickClickableSpanOnTypographyUnify
import com.tokopedia.usercomponents.common.utils.waitOnView
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeViewHolder
import org.hamcrest.CoreMatchers.allOf

fun userConsentRobot(action: UserConsentRobot.() -> Unit): UserConsentRobot {
    return UserConsentRobot().apply(action)
}

infix fun UserConsentRobot.validateComponent(action: UserConsentResult.() -> Unit): UserConsentResult {
    Thread.sleep(1000)
    return UserConsentResult().apply(action)
}

class UserConsentRobot {
    fun clickCheckBoxSingleView() {
        waitOnView(allOf(withId(R.id.checkboxPurposes), isDisplayed()))
            .perform(click())
    }

    fun clickCheckBoxMultipleView(position: Int) {
        waitOnView(withId(R.id.recyclerPurposes))
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
        waitOnView(allOf(withId(R.id.descriptionPurposes), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Kebijakan Privasi"))
    }

    fun clickTncHyperlinkOnMultipleOptional() {
        waitOnView(allOf(withId(R.id.textMainDescription), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Syarat & Ketentuan"))
    }

    fun clickPolicyHyperlinkOnMultipleOptional() {
        waitOnView(allOf(withId(R.id.textMainDescription), isDisplayed()))
            .perform(clickClickableSpanOnTypographyUnify("Kebijakan Privasi"))
    }

    fun clickActionButton() {
        waitOnView(allOf(withId(R.id.buttonAction), isDisplayed()))
            .perform(click())
    }
}

class UserConsentResult {
    private val textWithTnc = "Saya menyetujui Syarat & Ketentuan"
    private val textWithTncPolicy = "Saya menyetujui Syarat & Ketentuan serta Kebijakan Privasi"

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
