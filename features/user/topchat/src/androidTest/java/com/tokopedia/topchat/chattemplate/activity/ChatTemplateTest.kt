package com.tokopedia.topchat.chattemplate.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.activity.base.BaseChatTemplateTest
import com.tokopedia.topchat.matchers.withRecyclerView
import org.junit.Test

@UiTest
class ChatTemplateTest: BaseChatTemplateTest() {

    @Test
    fun should_show_template_list_when_success_get_list() {
        //Given
        getTemplateUseCase.response = successGetTemplateResponseBuyer

        //When
        launchActivity()

        //Then
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(0, R.id.caption))
            .check(matches(withText(successGetTemplateResponseBuyer.templates.first())))
    }
}