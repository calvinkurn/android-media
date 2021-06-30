package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.withItemCount
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.view.service.UploadImageChatServiceStub
import org.hamcrest.Matchers.greaterThan
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
        onView(withId(R.id.fl_image_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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
        onView(withId(R.id.recycler_view)).check(withItemCount(greaterThan(count)))
    }

    private fun openImagePicker() {
        clickPlusIconMenu()
        clickAttachImageMenu()
    }

    private fun getCurrentItemCount(): Int {
        val recyclerView = activityTestRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun openChatRoom(replyChatGqlDelay: Long = 0L) {
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        replyChatGQLUseCase.delayResponse = replyChatGqlDelay
        replyChatGQLUseCase.response = uploadImageReplyResponse
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData())
        )
    }

    @After
    fun deleteDummies() {
        UploadImageChatServiceStub.dummyMap.clear()
    }
}