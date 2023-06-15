package com.tokopedia.tokochat.test

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.applink.internal.ApplinkConstInternalMedia
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.R
import com.tokopedia.tokochat.stub.domain.response.ApiResponseStub
import com.tokopedia.tokochat.stub.domain.usecase.TokoChatSendMessageUseCaseStub.Companion.TEST_ID
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.general.GeneralResult
import com.tokopedia.tokochat.test.robot.header_date.HeaderDateResult
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleResult
import com.tokopedia.tokochat.test.robot.message_bubble.MessageBubbleRobot
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaRobot
import com.tokopedia.tokochat.test.robot.ticker.TickerResult
import com.tokopedia.tokochat.util.TokoChatViewUtil
import com.tokopedia.tokochat_common.util.TokoChatCacheManagerImpl
import com.tokopedia.utils.file.FileUtil
import org.junit.Test
import timber.log.Timber
import java.io.FileOutputStream

@UiTest
class TokoChatHistoryChatTest : BaseTokoChatTest() {

    @Test
    fun should_show_date_header_in_chat_history() {
        // When
        launchChatRoomActivity()
        Thread.sleep(1000)
        val adapter = getTokoChatAdapter()
        val position = adapter.lastIndex - 1

        // Then
        HeaderDateResult.assertHeaderDateVisibility(position = position, isVisible = true)
    }

    @Test
    fun should_show_first_ticker() {
        // When
        launchChatRoomActivity()
        Thread.sleep(1000)
        val adapter = getTokoChatAdapter()
        val lastItem = adapter.lastIndex

        // Then
        TickerResult.assertTickerVisibility(position = lastItem, isVisible = true)
    }

    @Test
    fun should_show_image_attachment() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_image_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertImageAttachmentVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertImageAttachmentRetryDownloadVisibility(
            position = 0,
            isVisible = false
        )
        MessageBubbleResult.assertImageAttachmentRetryUploadVisibility(
            position = 0,
            isVisible = false
        )
    }

    @Test
    fun should_show_image_attachment_when_user_upload_image() {
        // Given
        saveDummyImage()

        // When
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.hasData(ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))

        ReplyAreaRobot.clickAttachmentPlusButton()
        ReplyAreaRobot.clickAttachmentMenuButton(0)

        // Then
        MessageBubbleResult.assertImageAttachmentVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertImageAttachmentRetryDownloadVisibility(
            position = 0,
            isVisible = false
        )
        MessageBubbleResult.assertImageAttachmentRetryUploadVisibility(
            position = 0,
            isVisible = false
        )
    }

    @Test
    fun should_show_dummy_image_attachment_when_user_fail_upload_image() {
        // Given
        ApiResponseStub.imageAttachmentUploadResponse = Pair(
            200,
            "image_attachment/fail_upload_image_attachment.json"
        )
        cacheManager.saveCache(TokoChatCacheManagerImpl.TOKOCHAT_IMAGE_ATTACHMENT_MAP, mapOf(Pair(TEST_ID, TEST_ID)))
        saveDummyImage()

        // When
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.hasData(ApplinkConstInternalMedia.INTERNAL_MEDIA_PICKER))
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, getImageData()))

        ReplyAreaRobot.clickAttachmentPlusButton()
        ReplyAreaRobot.clickAttachmentMenuButton(0)

        // Then
        GeneralResult.assertSnackBarWithSubText("Custom message error from BE")

        Thread.sleep(3000) // Wait for image to appear
        MessageBubbleResult.assertImageAttachmentVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertImageAttachmentRetryDownloadVisibility(
            position = 0,
            isVisible = false
        )
        MessageBubbleResult.assertImageAttachmentRetryUploadVisibility(
            position = 0,
            isVisible = true
        )
    }

    @Test
    fun should_show_broken_image_attachment_when_user_fail_download_image() {
        // Given
        FileUtil.deleteFolder(TokoChatViewUtil.getTokopediaTokoChatCacheDirectory().absolutePath)
        ApiResponseStub.imageAttachmentDownloadResponse = Pair(404, "")
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_image_ext.json"
        )

        // When
        launchChatRoomActivity()
        Thread.sleep(3000)

        // Then
        MessageBubbleResult.assertImageAttachmentVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertImageAttachmentRetryDownloadVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertImageAttachmentRetryUploadVisibility(
            position = 0,
            isVisible = false
        )
    }

    @Test
    fun should_show_not_supported_attachment_voice_notes() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_voice_notes_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertMessageBubbleText(
            position = 0,
            text = activity.getString(
                R.string.tokochat_unsupported_attachment_voice_note
            )
        )
    }

    @Test
    fun should_show_not_supported_attachment_general() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_sticker_ext.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertMessageBubbleText(
            position = 0,
            text = activity.getString(
                R.string.tokochat_unsupported_attachment_general
            )
        )
    }

    @Test
    fun should_show_new_sent_message_and_pending_mark() {
        // When
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea("Test 123")
        ReplyAreaRobot.clickReplyButton()
        Thread.sleep(2000) // Wait for message to be saved in db

        // Then
        MessageBubbleResult.assertMessageBubbleCheckMark(
            position = 0
        )
    }

    @Test
    fun should_show_long_message_bubble_message_when_text_too_long() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_long_message.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleReadMoreText(
            position = 0
        )
    }

    @Test
    fun should_show_long_message_bottom_sheet() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_long_message.json"
        )

        // When
        launchChatRoomActivity()
        MessageBubbleRobot.clickReadMore()
        Thread.sleep(2000) // Wait for bottomsheet to show

        // Then
        MessageBubbleResult.assertMessageBubbleBottomSheet()
    }

    @Test
    fun should_show_admin_message_as_ticker() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_system.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        TickerResult.assertTickerVisibility(
            position = 0,
            isVisible = true
        )
        TickerResult.assertTickerText(
            "Pesanan selesai! Kamu bisa liat riwayat chat dan juga nge-chat driver sampai 2 jam setelah pesanan diselesaikan."
        )
    }

    @Test
    fun should_show_censored_message_when_users_send_blocked_words() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_censored.json"
        )

        // When
        launchChatRoomActivity()

        // Then
        MessageBubbleResult.assertMessageBubbleCensoredVisibility(
            position = 0,
            isVisible = true
        )
        MessageBubbleResult.assertMessageBubbleCensoredText(
            position = 0,
            text = activity.getString(
                com.tokopedia.tokochat_common.R.string.tokochat_message_censored
            )
        )
    }

    @Test
    fun should_show_bottomsheet_guide_chat_when_users_click_show_censored() {
        // Given
        ApiResponseStub.chatHistoryResponse = Pair(
            200,
            "chat_history/success_get_chat_history_censored.json"
        )

        // When
        launchChatRoomActivity()
        MessageBubbleRobot.clickCheckGuide()

        // Then
        MessageBubbleResult.assertGuideChatBottomSheet()
    }

    private fun saveDummyImage() {
        try {
            val dummyBitmap: Bitmap = BitmapFactory.decodeResource(
                context.resources,
                com.tokopedia.tokochat.test.R.drawable.dummy_image
            )
            val file = TokoChatViewUtil.getTokoChatPhotoPath("dummy_image")
            if (!file.exists()) {
                val outStream = FileOutputStream(file)
                dummyBitmap.compress(Bitmap.CompressFormat.JPEG, 1, outStream)
                outStream.flush()
                outStream.close()
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    private fun getImageData(): Intent {
        return Intent().apply {
            putExtra(
                EXTRA_RESULT_PICKER,
                PickerResult(
                    originalPaths = listOf(TokoChatViewUtil.getTokoChatPhotoPath("dummy_image").path)
                )
            )
        }
    }
}
