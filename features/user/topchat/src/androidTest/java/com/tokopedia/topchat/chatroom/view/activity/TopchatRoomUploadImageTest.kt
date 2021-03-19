package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.imagepicker.common.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.common.RESULT_IMAGES_FED_INTO_IMAGE_PICKER
import com.tokopedia.imagepicker.common.RESULT_PREVIOUS_IMAGE
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.RecyclerViewItemCountAssertion
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.view.service.UploadImageChatServiceStub
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Test

class TopchatRoomUploadImageTest : TopchatRoomTest() {

    private fun defaultGiven(delay: Long = 0L) {
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        replyChatGQLUseCase.delayResponse = delay
        replyChatGQLUseCase.response = uploadImageReplyResponse
        inflateTestFragment()
    }

    private fun defaultWhen() {
        Intents.intending(IntentMatchers.anyIntent())
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))
        openImagePicker()
    }

    @Test
    fun upload_image_and_stay_in_chatroom() {
        // Given
        defaultGiven(delay = 1000L)
        // When
        defaultWhen()
        Thread.sleep(1500L)
        // Then
        Espresso.onView(ViewMatchers.withId(R.id.fl_image_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun upload_big_image_in_chatroom() {
        // Given
        defaultGiven(delay = 4000L)
        // When
        defaultWhen()
        Thread.sleep(5000L)
        // Then
        Espresso.onView(ViewMatchers.withId(R.id.fl_image_container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun upload_multiple_images_and_stay_in_chatroom() {
        // Given
        defaultGiven()
        // When
        val count = getCurrentItemCount()
        defaultWhen()
        openImagePicker()
        // Then
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(RecyclerViewItemCountAssertion.withItemCount(greaterThan(count)))
    }

    private fun openImagePicker() {
        clickPlusIconMenu()
        clickAttachImageMenu()
    }

    private fun getCurrentItemCount(): Int {
        val recyclerView = activityTestRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        return recyclerView.adapter?.itemCount?: 0
    }

    private fun getImageData(): Intent {
        return Intent().apply {
            putStringArrayListExtra(PICKER_RESULT_PATHS, arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg"))
            putStringArrayListExtra(RESULT_PREVIOUS_IMAGE, arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg"))
            putStringArrayListExtra(RESULT_IMAGES_FED_INTO_IMAGE_PICKER, arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg"))
        }
    }

    @After
    fun deleteDummies() {
        UploadImageChatServiceStub.dummyMap.clear()
    }
}