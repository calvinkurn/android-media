package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatImageUploadViewHolder
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Test

@UiTest
class TopchatRoomUploadImageTest : TopchatRoomTest() {

    @Test
    fun upload_image_with_compress_and_with_service() {
        // Given
        enableCompressImage()
        enableUploadImageByService()
        openChatRoom()
        // When
        openMediaPicker()
        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_image_with_compress_and_without_service() {
        // Given
        enableCompressImage()
        disableUploadImageByService()
        openChatRoom()
        // When
        openMediaPicker()
        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_image_without_compress_and_with_service() {
        // Given
        disableCompressImage()
        enableUploadImageByService()
        openChatRoom()
        // When
        openMediaPicker()
        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_image_without_compress_and_without_service() {
        // Given
        disableCompressImage()
        disableUploadImageByService()
        openChatRoom()
        // When
        openMediaPicker()
        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
    }

    @Test
    fun upload_multiple_images_and_stay_in_chatroom() {
        // Given
        enableUploadImageByService()
        openChatRoom()
        // When
        val count = getCurrentItemCount()
        // send first image
        openMediaPicker()
        // send second image
        openMediaPicker()
        // Then
        assertImageContainerAtPosition(0, matches(isDisplayed()))
        assertImageContainerAtPosition(1, matches(isDisplayed()))
    }

    @Test
    fun upload_image_and_leave_chatroom_then_comeback() {
        // Given
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

    private fun assertImageReadStatusAtPosition(position: Int, assertions: ViewAssertion) {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(position, ImageUploadUiModel::class.java)
        )
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, R.id.chat_status)
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

    private fun getCurrentItemCount(): Int {
        val recyclerView = activityTestRule.activity.findViewById<RecyclerView>(R.id.recycler_view_chatroom)
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun openChatRoom(replyChatGqlDelay: Long = 0L) {
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        replyChatGQLUseCase.delayResponse = replyChatGqlDelay
        replyChatGQLUseCase.response = uploadImageReplyResponse
        launchChatRoomActivity()
        intending(hasData(INTERNAL_MEDIA_PICKER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))
    }

    @After
    fun deleteDummies() {
        UploadImageChatService.dummyMap.clear()
    }
}
