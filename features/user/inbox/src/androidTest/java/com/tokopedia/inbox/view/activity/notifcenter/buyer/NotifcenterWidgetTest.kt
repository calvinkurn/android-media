package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
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
        NotifcenterAssertion.assertNotifWidgetMsg(2, msg)
    }

    @Test
    fun should_render_short_desc_on_notif_widget() {
        // Given
        val msg = inboxNotifcenterDep.notifcenterDetailUseCase
            .noTrackHistoryWidgetMsg.notifcenterDetail.newList[0].shortDescriptionHtml
        inboxNotifcenterDep.notifcenterDetailUseCase.apply {
            response = noTrackHistoryWidgetMsg
        }
        startInboxActivity()

        // Then
        NotifcenterAssertion.assertNotifWidgetMsg(2, msg)
    }

}