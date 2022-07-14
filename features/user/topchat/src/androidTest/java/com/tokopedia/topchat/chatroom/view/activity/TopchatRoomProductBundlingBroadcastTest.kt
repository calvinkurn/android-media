package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.hideBanner
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import org.junit.Test

class TopchatRoomProductBundlingBroadcastTest: TopchatRoomTest()  {

    @Test
    fun show_broadcast_multiple_product_bundling() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithProductBundlingResponse
            .blockPromo(false)
            .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        Espresso.onView(ViewMatchers.withId(R.id.iv_banner))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun show_broadcast_single_product_bundling_with_multiple_item() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingMultipleItemResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        Espresso.onView(ViewMatchers.withId(R.id.iv_banner))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun show_broadcast_single_product_bundling_with_single_item() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingSingleItemResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        Espresso.onView(ViewMatchers.withId(R.id.iv_banner))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun show_broadcast_single_product_bundling_with_oos() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingOutOfStockResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        Espresso.onView(ViewMatchers.withId(R.id.iv_banner))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}