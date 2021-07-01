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
            topAdsRepository.response = topAdsRepository.defaultResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(6)


        // Then
        NotifcenterAssertion.assertTdnExistAtPosition(6)
    }

    @Test
    fun should_hide_TDN_when_ad_does_not_exist() {
        // Given
        inboxNotifcenterDep.apply {
            topAdsRepository.response = topAdsRepository.noDataResponse
        }
        startInboxActivity()

        // When
        NotifcenterAction.scrollNotificationToPosition(6)


        // Then
        NotifcenterAssertion.assertTdnNotExistAtPosition(6)
    }

    //TODO: should hide TDN when user has notification filter

}