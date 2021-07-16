package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.junit.Test

class TopchatRoomInvoiceAttachmentTest : BaseBuyerTopchatRoomTest() {

    private val goodId = "770851031"
    private val badId = "INV_123"

    private val expectedIntent = Matchers.allOf(
        IntentMatchers.hasExtra(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, "Seller Testing"),
        IntentMatchers.hasExtra(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, "66961")
    )

    private val expectedIntentSeller = Matchers.allOf(
        IntentMatchers.hasExtra(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, "Buyer Testing"),
        IntentMatchers.hasExtra(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, "66961")
    )

    @Test
    fun user_successfully_attach_good_invoice() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        Intents.intending(expectedIntent).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, getInvoiceAttachmentIntent(true)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview)).check(matches(withTotalItem(1)))
    }

    @Test
    fun user_failed_to_attach_bad_invoice() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        Intents.intending(expectedIntent).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, getInvoiceAttachmentIntent(false)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(not(isDisplayed())))
    }

    @Test
    fun user_can_not_sent_preview_invoice_when_text_is_empty() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        Intents.intending(expectedIntent).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, getInvoiceAttachmentIntent(true)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickSendBtn()

        // Then
        assertSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }

    @Test
    fun user_successfully_attach_good_invoice_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity(isSellerApp = true)

        //When
        Intents.intending(expectedIntentSeller).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, getInvoiceAttachmentIntent(true)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview)).check(matches(withTotalItem(1)))
    }

    @Test
    fun user_failed_to_attach_bad_invoice_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity(isSellerApp = true)

        //When
        Intents.intending(expectedIntentSeller).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, getInvoiceAttachmentIntent(false)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(not(isDisplayed())))
    }

    @Test
    fun user_can_not_sent_preview_invoice_when_text_is_empty_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity(isSellerApp = true)

        // When
        Intents.intending(expectedIntentSeller).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, getInvoiceAttachmentIntent(true)
            )
        )
        clickPlusIconMenu()
        clickAttachInvoiceMenu()
        clickSendBtn()

        // Then
        assertSnackbarText(context.getString(R.string.topchat_desc_empty_text_box))
    }

    private fun getInvoiceAttachmentIntent(isValidInvoice: Boolean): Intent {
        val intent = Intent()
        val id: String = if(isValidInvoice) goodId else badId
        intent.putExtra(ApplinkConst.Chat.INVOICE_ID, id)
        intent.putExtra(ApplinkConst.Chat.INVOICE_CODE, "INV/20210422/MPL/1190494783")
        intent.putExtra(ApplinkConst.Chat.INVOICE_TITLE,
            "sendal jepit 999^^\u0026%#^%#$%$%*\u0026^%")
        intent.putExtra(ApplinkConst.Chat.INVOICE_DATE, "22 Apr 2021")
        intent.putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL,
            "https://ecs7.tokopedia.net/img/cache/100-square/VqbcmM/2021/2/16/61e5d2f7-f411-4b14-94fe-c5c401636952.jpg")
        intent.putExtra(ApplinkConst.Chat.INVOICE_URL,
            "https://www.tokopedia.com/invoice.pl?id\u003d770851031\u0026pdf\u003dInvoice-136513670-10825582-20210422132008-eHh4eHh4eHg3")
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, "700")
        intent.putExtra(ApplinkConst.Chat.INVOICE_STATUS, "Pesanan Selesai")
        intent.putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, "Rp 200")

        return intent
    }
}