package com.tokopedia.topchat.chatroom.view.activity.test.seller

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.previewAttachmentResult
import com.tokopedia.topchat.chatroom.view.activity.robot.voucherResult
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomSellerAttachVoucherTest : BaseSellerTopchatRoomTest() {

    @Test
    fun should_open_voucher_detail_when_click_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
            doActionOnListItemAt(0, click())
        }

        // Then
        val intent = Intent(context, MerchantVoucherDetailActivity::class.java)
        generalResult {
            openPageWithIntent(intent)
        }
    }

    @Test
    fun should_open_merchant_voucher_page_when_click_product_voucher_on_sellerapp() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
            doActionOnListItemAt(1, click())
        }

        // Then
        generalResult {
            openPageWithApplink("sellerapp://voucher-product-detail/7050189")
        }
    }

    @Test
    fun should_show_toaster_when_click_product_voucher_on_seller() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = false)
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
            doActionOnListItemAt(1, click())
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.topchat_mvc_not_available))
        }
    }

    @Test
    fun should_show_additional_text_in_lock_to_product_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)

        // When
        generalRobot {
            scrollChatToPosition(0)
        }

        // Then
        voucherResult {
            assertInvoiceAttachmentDesc(1, withSubstring("untuk produk tertentu"))
        }
    }

    @Test
    fun should_not_show_additional_text_in_regular_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)

        // When
        generalRobot {
            scrollChatToPosition(0)
        }

        // Then
        voucherResult {
            assertInvoiceAttachmentDesc(0, not(withSubstring("untuk produk tertentu")))
        }
    }

    @Test
    fun should_show_additional_text_in_preview_private_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        val voucherData = "{\"amount\":20000,\"amountType\":1,\"desktopUrl\":\"https://images.tokopedia.net/img/BTJGre/2022/1/25/a5dad590-e6d3-466b-9c8f-6ea81ad1632d.jpg\",\"identifier\":\"1\",\"isPublic\":0,\"minimumSpend\":100000,\"mobileUrl\":\"https://images.tokopedia.net/img/BTJGre/2022/1/25/a5dad590-e6d3-466b-9c8f-6ea81ad1632d.jpg\",\"source\":\"inbox\",\"tnc\":\"tnc panjang\",\"validThru\":1645576200,\"voucherCode\":\"1097122921\",\"voucherId\":7297979,\"voucherName\":\"gratis ongkir gratis ongkir gr\",\"voucherType\":1, \"isLockToProduct\":1}"
        val resultIntent = Intent().apply {
            this.putExtra(ApplinkConst.AttachVoucher.PARAM_VOUCHER_PREVIEW, voucherData)
        }
        launchChatRoomActivity(isSellerApp = true)
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent))

        // When
        generalRobot {
            scrollChatToPosition(0)
        }
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachVoucherMenu()
        }

        // Then
        previewAttachmentResult {
            assertVoucherPreview(0, withSubstring("untuk produk tertentu"))
        }
    }
}
