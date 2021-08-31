package com.tokopedia.topchat.chatlist.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.AttachmentItemViewHolder
import com.tokopedia.imagepicker.common.PICKER_RESULT_PATHS
import com.tokopedia.imagepicker.common.RESULT_IMAGES_FED_INTO_IMAGE_PICKER
import com.tokopedia.imagepicker.common.RESULT_PREVIOUS_IMAGE
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatroom.service.UploadImageChatService
import com.tokopedia.topchat.matchers.withIndex
import org.junit.After
import org.junit.Test

class ChatListUploadImageTest : ChatListTest() {

    @Test
    fun upload_image_and_leave_chatroom_then_comeback() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = SHOP_NAME_STUB
        userSession.nameStub = NAME_STUB
        chatListUseCase.response = buyerChatListPojo
        userSession.setIsShopOwner(true)
        setIntentImagePicker()

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)
        openBuyerTab()
        openChatRoom()
        openImagePicker()
        pressBackButton()
        openChatRoom()

        // Then
        onView(withId(R.id.fl_image_container))
            .check(matches(isDisplayed()))
    }

    @Test
    fun upload_image_then_failed_and_leave_chatroom_then_comeback() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = SHOP_NAME_STUB
        userSession.nameStub = NAME_STUB
        chatListUseCase.response = buyerChatListPojo
        userSession.setIsShopOwner(true)
        setIntentImagePicker()
        isFailedUpload = true

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)
        openBuyerTab()
        openChatRoom()
        openImagePicker()
        pressBackButton()
        openChatRoom()

        // Then
        onView(withId(R.id.fl_image_container))
            .check(matches(isDisplayed()))
        onView(withId(R.id.left_action))
            .check(matches(isDisplayed()))
    }

    @Test
    fun upload_image_then_failed_and_leave_chatroom_then_comeback_and_retry() {
        // Given
        userSession.hasShopStub = true
        userSession.shopNameStub = SHOP_NAME_STUB
        userSession.nameStub = NAME_STUB
        chatListUseCase.response = buyerChatListPojo
        userSession.setIsShopOwner(true)
        setIntentImagePicker()
        isFailedUpload = true

        // When
        activity.setupTestFragment(chatListUseCase, chatNotificationUseCase)
        openBuyerTab()
        openChatRoom()
        openImagePicker()
        pressBackButton()
        openChatRoom()
        onView(withId(R.id.left_action)).perform(click())
        onView(withText("Kirim ulang")).perform(click())

        // Then
        onView(withId(R.id.fl_image_container))
            .check(matches(isDisplayed()))
        onView(withId(R.id.left_action))
            .check(matches(isDisplayed()))
    }

    private fun openBuyerTab() {
        onView(withText(NAME_STUB))
            .perform(click())
    }

    private fun openChatRoom() {
        onView(withIndex(withId(R.id.recycler_view), 1))
            .perform(click())
    }

    private fun openImagePicker() {
        clickPlusIconMenu()
        clickAttachImageMenu()
    }

    private fun clickAttachImageMenu() {
        val viewAction = RecyclerViewActions
            .actionOnItemAtPosition<AttachmentItemViewHolder>(
                1, click()
            )
        onView(withId(R.id.rv_topchat_attachment_menu))
            .perform(viewAction)
    }

    private fun clickPlusIconMenu() {
        onView(withId(R.id.iv_chat_menu))
            .perform(click())
    }

    private fun setIntentImagePicker() {
        Intents.intending(IntentMatchers.hasData("tokopedia-android-internal://global/image-picker"))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))
    }

    private fun getImageData(): Intent {
        return Intent().apply {
            putStringArrayListExtra(
                PICKER_RESULT_PATHS,
                arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
            )
            putStringArrayListExtra(
                RESULT_PREVIOUS_IMAGE,
                arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
            )
            putStringArrayListExtra(
                RESULT_IMAGES_FED_INTO_IMAGE_PICKER,
                arrayListOf("https://images.tokopedia.net/img/LUZQDL/2021/3/18/fa23883b-4188-417b-ab8d-21255f62a324.jpg")
            )
        }
    }

    @After
    fun deleteDummies() {
        UploadImageChatService.dummyMap.clear()
        isFailedUpload = false
    }

    companion object {
        var isFailedUpload = false
    }
}