package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.assertion.withItemCount
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.TopchatImageUploadViewHolder
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Test

class TopchatRoomUploadImageTest : TopchatRoomTest() {

    @Test
    fun upload_image_and_stay_in_chatroom() {
        // Given
        openChatRoom()
        // When
        openImagePicker()
        // Then
        assertImageContainerAtPosition(0)
    }

    @Test
    fun upload_multiple_images_and_stay_in_chatroom() {
        // Given
        openChatRoom()
        // When
        val count = getCurrentItemCount()
        //send first image
        openImagePicker()
        //send second image
        openImagePicker()
        // Then
        assertImageContainerAtPosition(0)
        assertImageContainerAtPosition(1)
        onView(withId(R.id.recycler_view_chatroom)).check(withItemCount(greaterThan(count)))
    }

    @Test
    fun upload_image_and_leave_chatroom_then_comeback() {
        // Given
        openChatRoom()

        // When
        openImagePicker()
        finishActivity()
        openChatRoom()

        // Then
        assertImageContainerAtPosition(0)
    }

    @Test
    fun should_have_1_failed_image_attachment_when_user_come_back_to_chatroom() {
        // Given
        uploadImageUseCase.isError = true
        openChatRoom()

        // When
        openImagePicker()
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
        openChatRoom()

        // When
        openImagePicker()
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
        openImagePicker()

        // Then
        assertImageUploadViewModelAtPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(0, R.id.chat_status))
            .check(matches(not(isDisplayed())))
    }

    private fun assertImageContainerAtPosition(position: Int) {
        assertImageUploadViewModelAtPosition(position)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(position, R.id.fl_image_container))
            .check(matches(isDisplayed()))
    }

    private fun assertImageUploadViewModelAtPosition(position: Int) {
        onView(withId(R.id.recycler_view_chatroom)).check(
            atPositionIsInstanceOf(position, ImageUploadViewModel::class.java)
        )
    }

    private fun clickImageUploadErrorHandler() {
        onView(withId(R.id.left_action)).perform(click())
    }

    private fun clickRetrySendImage() {
        onView(withText("Kirim ulang")).perform(click())
    }

    private fun openImagePicker() {
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
        intending(hasData(ApplinkConstInternalGlobal.IMAGE_PICKER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))
    }

    @After
    fun deleteDummies() {
        UploadImageChatService.dummyMap.clear()
    }
}