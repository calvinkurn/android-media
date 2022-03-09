package com.tokopedia.topchat.chattemplate.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.activity.base.BaseEditTemplateTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class EditTemplateTest: BaseEditTemplateTest() {

    @Test
    fun should_open_with_text_when_edit() {
        //When
        launchActivity()

        //Then
        onView(withId(R.id.edittext)).check(matches(withText(testTemplateList.first())))
    }

    @Test
    fun should_open_with_empty_text_when_create() {
        //When
        launchActivity(
            message = null,
            position = -1,
            sizeList = 1,
            arrayListTemplate = arrayListOf()
        )

        //Then
        onView(withId(R.id.edittext))
            .check(matches(withHint(context.getString(R.string.hint_template_chat))))
    }

    @Test
    fun should_not_able_to_save_when_less_than_5_chars() {
        //Given
        val expectedText = "Test"

        //When
        launchActivity()
        onView(withId(R.id.edittext)).perform(replaceText(expectedText))

        //Then
        onView(withId(R.id.edittext)).check(matches(withText(expectedText)))
        onView(withId(R.id.error)).check(matches(isDisplayed()))
        onView(withId(R.id.error)).check(matches(withText(R.string.minimal_char_template)))
    }

    @Test
    fun should_able_to_save_when_more_than_5_chars() {
        //Given
        val expectedText = "Test 123 Test 456"

        //When
        launchActivity()
        onView(withId(R.id.edittext)).perform(replaceText(expectedText))

        //Then
        onView(withId(R.id.edittext)).check(matches(withText(expectedText)))
        onView(withId(R.id.error)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_not_able_to_save_when_more_than_200_chars() {
        //Given
        val maxChar = 200
        val expectedText = getLongTestText()
        val errorText = context.getString(R.string.maximal_char_template, maxChar)

        //When
        launchActivity()
        onView(withId(R.id.edittext)).perform(replaceText(expectedText))

        //Then
        onView(withId(R.id.edittext)).check(matches(withText(expectedText)))
        onView(withId(R.id.error)).check(matches(withText(errorText)))
    }

    @Test
    fun should_not_show_delete_button_when_create_template() {
        //When
        launchActivity(
            message = null,
            position = -1,
            sizeList = 1,
            arrayListTemplate = arrayListOf()
        )

        //Then
        onView(withId(R.id.action_organize)).check(doesNotExist())
    }

    @Test
    fun should_show_pop_up_when_delete_template() {
        //When
        launchActivity()
        onView(withId(R.id.action_organize)).perform(click())

        //Then
        assertWithSubText(context.getString(R.string.delete_chat_template))
        assertWithSubText(context.getString(R.string.forever_deleted_template))
    }

    @Test
    fun should_show_snackBar_when_delete_last_template() {
        //Given
        val list = arrayListOf("Test")

        //When
        launchActivity(sizeList = 2, arrayListTemplate = list)
        onView(withId(R.id.action_organize)).perform(click())

        //Then
        assertWithSubText(context.getString(R.string.minimum_template_chat_warning))
    }

    private fun getLongTestText(): String {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam auctor ex mi, " +
                "eu egestas lacus consectetur et. In eleifend luctus ligula, eu efficitur tellus " +
                "elementum quis. Mauris dapibus auctor nisl, sit amet suscipit erat sagittis vitae. " +
                "Ut id blandit lacus, in pharetra quam. Ut sed massa tristique, " +
                "ornare lacus et, mollis quam. Aenean eu iaculis urna. Suspendisse laoreet semper " +
                "erat sed tristique. Nam sagittis nibh ligula, vel varius neque pretium a. " +
                "Sed eget luctus tortor. Quisque enim tellus, condimentum eget porta in, consectetur ac neque. " +
                "Ut lacus diam, lobortis et tortor non, porttitor vestibulum ipsum. Mauris vel aliquam dui.\n" +
                "Nunc eu arcu eu mauris ornare lacinia. Vivamus ultricies, dui eu pretium varius, " +
                "urna purus ornare nulla, non dapibus eros elit a urna. Morbi consectetur risus libero, " +
                "in pellentesque enim viverra vel. Curabitur mollis consequat justo vitae elementum. " +
                "Morbi non quam eget tellus blandit aliquet a in turpis. Proin nec neque accumsan massa " +
                "hendrerit venenatis consequat eget neque. Morbi venenatis venenatis erat eget fermentum. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris mattis placerat enim, " +
                "in gravida mauris. Duis ac eros finibus, semper mauris eget, sagittis metus. " +
                "Pellentesque rhoncus rhoncus nunc, sit amet fringilla ipsum dictum eget. Morbi quis sapien." +
                "Test Test Test Test Test"
    }
}