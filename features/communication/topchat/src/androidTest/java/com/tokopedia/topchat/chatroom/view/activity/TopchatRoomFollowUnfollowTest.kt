package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.chat_common.R
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import org.junit.Test

@UiTest
class TopchatRoomFollowUnfollowTest: TopchatRoomTest() {

    @Test
    fun should_show_follow_toaster_when_click_header_menu_follow_toko(){

        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        launchChatRoomActivity()

        //When
        Espresso.onView(ViewMatchers.withId(R.id.header_menu)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Follow Toko")).perform(ViewActions.click())

        //Then
        assertSnackbarText(context.getString(com.tokopedia.topchat.R.string.title_success_follow_shop))
    }

    @Test
    fun should_show_unfollow_toaster_when_click_header_menu_following(){

        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()

        //When
        Espresso.onView(ViewMatchers.withId(R.id.header_menu)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Following")).perform(ViewActions.click())

        //Then
        assertSnackbarText(context.getString(com.tokopedia.topchat.R.string.title_success_unfollow_shop))
    }

    @Test
    fun should_show_error_toaster_when_click_header_menu_follow_and_failed(){

        //Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        toggleFavouriteShopUseCaseStub.isError = true
        launchChatRoomActivity()

        //When
        Espresso.onView(ViewMatchers.withId(R.id.header_menu)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Follow Toko")).perform(ViewActions.click())

        //Then
        Espresso.onView(ViewMatchers.withSubstring("Oops!"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun should_toaster_when_click_header_menu_follow_toko_from_broadcast_spam_handler() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        //When
        clickBroadcastHandlerFollowShop()

        // Then
        assertSnackbarText(context.getString(com.tokopedia.topchat.R.string.title_success_follow_shop))
    }

    @Test
    fun should_error_toaster_when_click_header_menu_follow_toko_from_broadcast_spam_handler_and_failed() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        toggleFavouriteShopUseCaseStub.isError = true
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        //When
        Espresso.onView(ViewMatchers.withId(com.tokopedia.topchat.R.id.btn_follow_shop))
            .perform(ViewActions.click())

        // Then
        Espresso.onView(ViewMatchers.withSubstring("Oops!"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}