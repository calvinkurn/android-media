package com.tokopedia.inbox.view.activity.notifcenter.buyer

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class NotifcenterWidgetTest : InboxNotifcenterTest() {

    @Test
    fun should_render_widget_message() {
        // Given
        val msg = inboxNotifcenterDep.notifcenterDetailUseCase
            .noTrackHistoryWidget.notifcenterDetail.newList[0].widget.message
        inboxNotifcenterDep.notifcenterDetailUseCase.apply {
            response = noTrackHistoryWidget
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertNotifWidgetVisibility(2, isDisplayed())
        NotifcenterAssertion.assertNotifWidgetMsg(2, msg)
    }

    @Test
    fun should_render_short_desc_on_notif_widget() {
        // Given
        inboxNotifcenterDep.notifcenterDetailUseCase.apply {
            response = noTrackHistoryWidgetMsg
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertNotifWidgetVisibility(2, not(isDisplayed()))
    }

}