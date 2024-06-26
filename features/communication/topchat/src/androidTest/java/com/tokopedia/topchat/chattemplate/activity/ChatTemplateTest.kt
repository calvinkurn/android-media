package com.tokopedia.topchat.chattemplate.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.DrawableMatcher
import com.tokopedia.topchat.chattemplate.activity.base.BaseChatTemplateTest
import com.tokopedia.topchat.chattemplate.view.activity.EditTemplateChatActivity
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class ChatTemplateTest: BaseChatTemplateTest() {

    @Test
    fun should_show_template_list_when_success_get_list() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer

        //When
        launchActivity()

        //Then
        onView(withId(R.id.recycler_view_template)).check(matches(isDisplayed()))
        onView(withRecyclerView(R.id.recycler_view_template).atPositionOnView(0, R.id.caption))
            .check(matches(
                withText(
                    getTemplateUseCaseStub.successGetTemplateResponseBuyer
                        .chatTemplatesAll
                        .buyerTemplate
                        .templates
                        .first()
                )
            ))
    }

    @Test
    fun should_not_show_template_list_and_switch_when_disabled() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer
        getTemplateUseCaseStub.response.chatTemplatesAll.buyerTemplate.isEnable = false

        //When
        launchActivity()

        //Then
        onView(withId(R.id.recycler_view_template)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_disabled_add_template_when_full_list() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer

        //When
        launchActivity()

        //Then
        onView(withRecyclerView(R.id.recycler_view_template).atPositionOnView(5, R.id.caption))
            .check(matches(withText(context.getString(com.tokopedia.chat_common.R.string.add_template_chat))))
        DrawableMatcher.compareDrawableOnRecyclerView(
            R.id.recycler_view_template,
            5,
            R.id.setting,
            R.drawable.ic_plus_grey
        )
    }

    @Test
    fun should_enable_add_template_when_not_full_list() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer
        getTemplateUseCaseStub.response.chatTemplatesAll.buyerTemplate.templates.clear()

        //When
        launchActivity()

        //Then
        onView(withRecyclerView(R.id.recycler_view_template).atPositionOnView(0, R.id.caption))
            .check(matches(withText(context.getString(com.tokopedia.chat_common.R.string.add_template_chat))))
        DrawableMatcher.compareDrawableOnRecyclerView(
            R.id.recycler_view_template,
            0,
            R.id.setting,
            R.drawable.ic_topchat_plus_green
        )
    }

    @Test
    fun should_show_toaster_when_click_disabled_add_button() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer

        //When
        launchActivity()
        onView(withId(R.id.content)).perform(swipeUp())
        onView(withRecyclerView(R.id.recycler_view_template).atPosition(5)).perform(click())

        //Then
        assertSnackBarWithSubText(context.getString(R.string.limited_template_chat_warning))
    }

    @Test
    fun should_go_to_edit_template_when_click_enabled_add_button() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer
        getTemplateUseCaseStub.response.chatTemplatesAll.buyerTemplate.templates.clear()

        //When
        launchActivity()
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withRecyclerView(R.id.recycler_view_template).atPosition(0)).perform(click())

        //Then
        intended(hasComponent(EditTemplateChatActivity::class.java.name))
    }

    @Test
    fun should_show_bottomsheet_when_click_info_button() {
        //Given
        getTemplateUseCaseStub.response = getTemplateUseCaseStub.successGetTemplateResponseBuyer

        //When
        launchActivity()
        onView(withId(R.id.template_list_info)).perform(click())

        //Then
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_title))
            .check(matches(withText(context.getString(R.string.title_info_list_template))))
        onView(withId(R.id.bs_chat_desc))
            .check(matches(withText(context.getString(R.string.body_info_list_template))))
    }
}
