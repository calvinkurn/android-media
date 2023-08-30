package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.internal.ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.assertViewObjectValue
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatImageUploadViewHolder
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Test

@UiTest
class TopchatRoomUploadImageTest : TopchatRoomTest() {

    @Test
    fun upload_image_with_service() {
        // Given
        enableUploadImageByService()
        disableUploadSecure()
        openChatRoom()

        // When
        openMediaPicker()
        simulateWebSocketImageUploadResponse()

        // Then
        assertAttachmentType(isSecure = false)
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_image_without_service() {
        // Given
        disableUploadImageByService()
        disableUploadSecure()
        openChatRoom()

        // When
        openMediaPicker()
        simulateWebSocketImageUploadResponse()

        // Then
        assertAttachmentType(isSecure = false)
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_image_with_service_and_secure() {
        // Given
        enableUploadImageByService()
        enableUploadSecure()
        openChatRoom(replyResponse = replyChatGQLUseCase.uploadImageReplySecureResponse)

        // When
        openMediaPicker()
        simulateWebSocketImageUploadResponse()

        // Then
        assertAttachmentType()
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_image_without_service_and_secure() {
        // Given
        disableUploadImageByService()
        openChatRoom(replyResponse = replyChatGQLUseCase.uploadImageReplySecureResponse)

        // When
        openMediaPicker()
        simulateWebSocketImageUploadResponse()

        // Then
        assertAttachmentType()
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_multiple_images_and_stay_in_chatroom() {
        // Given
        enableUploadImageByService()
        openChatRoom()
        // When
        // send first image
        openMediaPicker()
        simulateWebSocketImageUploadResponse()
        // send second image
        openMediaPicker()
        simulateWebSocketImageUploadResponse()
        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
        assertImageContainerAtPosition(1, matches(isDisplayed()))
    }

    @Test
    fun upload_image_and_leave_chatroom_then_comeback() {
        // Given
        uploadImageUseCase.isError = null
        enableUploadImageByService()
        openChatRoom()

        // When
        openMediaPicker()
        finishActivity()
        openChatRoom()

        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun should_have_1_failed_image_attachment_when_user_come_back_to_chatroom() {
        // Given
        uploadImageUseCase.isError = true
        enableUploadImageByService()
        openChatRoom()

        // When
        openMediaPicker()
        finishActivity()
        openChatRoom()

        // Then
        assertChatRoomList(
            hasTotalItemOf(1, TopchatImageUploadViewHolder::class.java)
        )
    }

    @Test
    fun should_have_1_failed_image_attachment_when_user_come_back_to_chatroom_after_retry_upload_image() {
        // Given
        uploadImageUseCase.isError = true
        enableUploadImageByService()
        openChatRoom()

        // When
        openMediaPicker()
        finishActivity()
        openChatRoom()
        clickImageUploadErrorHandler()
        clickRetrySendImage()
        finishActivity()
        openChatRoom()

        // Then
        assertChatRoomList(
            hasTotalItemOf(1, TopchatImageUploadViewHolder::class.java)
        )
    }

    @Test
    fun should_not_showing_chat_status_when_failed_to_upload_image() {
        // Given
        uploadImageUseCase.isError = true
        openChatRoom()

        // When
        openMediaPicker()

        // Then
        assertImageReadStatusAtPosition(0, matches(not(isDisplayed())))
    }

    private fun assertImageContainerAtPosition(position: Int, assertions: ViewAssertion) {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(position, ImageUploadUiModel::class.java)
        )
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.fl_image_container)
        )
            .check(assertions)
    }

    private fun assertAttachmentType(isSecure: Boolean = true) {
        val expectedValue = if (isSecure) {
            AttachmentType.Companion.TYPE_IMAGE_UPLOAD_SECURE.toIntOrZero()
        } else {
            AttachmentType.Companion.TYPE_IMAGE_UPLOAD.toIntOrZero()
        }
        assertViewObjectValue(replyChatGQLUseCase.response.data.attachment.type, expectedValue)
    }

    private fun assertImageReadStatusAtPosition(position: Int, assertions: ViewAssertion) {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(position, ImageUploadUiModel::class.java)
        )
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, com.tokopedia.chat_common.R.id.chat_status)
        )
            .check(assertions)
    }

    private fun clickImageUploadErrorHandler() {
        onView(withId(R.id.left_action)).perform(click())
    }

    private fun clickRetrySendImage() {
        onView(withText("Kirim ulang")).perform(click())
    }

    private fun openMediaPicker() {
        clickPlusIconMenu()
        clickAttachImageMenu()
    }

    private fun openChatRoom(
        replyChatGqlDelay: Long = 0L,
        replyResponse: ChatReplyPojo = replyChatGQLUseCase.uploadImageReplyResponse
    ) {
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        replyChatGQLUseCase.delayResponse = replyChatGqlDelay
        replyChatGQLUseCase.response = replyResponse
        launchChatRoomActivity()
        intending(hasData(INTERNAL_MEDIA_PICKER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))
    }

    private fun simulateWebSocketImageUploadResponse() {
        val roomMetaData = getCurrentRoomMetaData(getChatUseCase.response)
        val incomingEventWs = websocket.generateUploadImageResponse(roomMetaData, false)
        websocket.simulateResponse(incomingEventWs)
    }

    private fun getCurrentRoomMetaData(chat: GetExistingChatPojo): RoomMetaData {
        return existingChatMapper.generateRoomMetaData(MSG_ID, chat)
    }

    @After
    fun deleteDummies() {
        UploadImageChatService.dummyMap.clear()
    }
}
