package com.tokopedia.inbox.view.activity.notifcenter.buyer

import com.tokopedia.inbox.view.activity.base.notifcenter.InboxNotifcenterTest
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAction
import com.tokopedia.inbox.view.activity.base.notifcenter.NotifcenterAssertion
import org.junit.Test

class NotifcenterTDNTest : InboxNotifcenterTest() {

    @Test
    fun should_show_TDN_when_ad_exist() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.response
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(6)


        // Then
        NotifcenterAssertion.assertTdnExistAtPosition(6)
    }

    //TODO: should hide TDN when ad does not exist
    //TODO: should hide TDN when user has notification filter

}