package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.headerRobot
import org.junit.Test
import com.tokopedia.topchat.R as topchatR

@UiTest
class TopchatRoomFollowUnfollowTest : TopchatRoomTest() {

    @Test
    fun should_show_follow_toaster_when_click_header_menu_follow_toko() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        launchChatRoomActivity()

        // When
        headerRobot {
            clickThreeDotsMenu()
            clickFollowMenu()
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.title_success_follow_shop))
        }
    }

    @Test
    fun should_show_unfollow_toaster_when_click_header_menu_following() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()

        // When
        headerRobot {
            clickThreeDotsMenu()
            clickFollowingMenu()
        }

        // Then
        generalResult {
            assertToasterText(context.getString(topchatR.string.title_success_unfollow_shop))
        }
    }

    @Test
    fun should_show_error_toaster_when_click_header_menu_follow_and_failed() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        toggleFavouriteShopUseCaseStub.isError = true
        launchChatRoomActivity()

        // When
        headerRobot {
            clickThreeDotsMenu()
            clickFollowMenu()
        }

        // Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }

    @Test
    fun should_toaster_when_click_header_menu_follow_toko_from_broadcast_spam_handler() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        launchChatRoomActivity()
        stubIntents()

        // When
        broadcastRobot {
            clickFollowShop(0)
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.title_success_follow_shop))
        }
    }

    @Test
    fun should_error_toaster_when_click_header_menu_follow_toko_from_broadcast_spam_handler_and_failed() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        toggleFavouriteShopUseCaseStub.isError = true
        launchChatRoomActivity()
        stubIntents()

        // When
        broadcastRobot {
            clickFollowShop(0)
        }

        // Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }
}
