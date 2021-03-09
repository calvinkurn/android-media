package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import org.junit.Test

class TopchatRoomBuyerBroadcastTest : TopchatRoomTest() {

    @Test
    fun show_broadcast_spam_handler_when_block_and_following_is_false() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        assertBroadcastSpamHandlerIsVisible()
    }

    private fun assertBroadcastSpamHandlerIsVisible() {
        onView(withId(R.id.title_bc_handle)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_stop_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_follow_shop)).check(matches(isDisplayed()))
    }
}
