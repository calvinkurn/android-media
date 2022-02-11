package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomSellerAttachVoucherTest: BaseSellerTopchatRoomTest() {

    @Test
    fun should_open_voucher_detail_when_click_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        GeneralRobot.doScrollChatToPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPosition(1)).perform(click())

        // Then
        val intent = Intent(context, MerchantVoucherDetailActivity::class.java)
        GeneralResult.openPageWithIntent(intent)
    }

    @Test
    fun should_show_additional_text_in_lock_to_product_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)

        //When
        GeneralRobot.doScrollChatToPosition(0)

        // Then
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, com.tokopedia.merchantvoucher.R.id.tvVoucherDesc))
            .check(matches(withSubstring("untuk produk tertentu")))
    }

    @Test
    fun should_not_show_additional_text_in_regular_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)

        //When
        GeneralRobot.doScrollChatToPosition(0)

        // Then
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(0, com.tokopedia.merchantvoucher.R.id.tvVoucherDesc))
            .check(matches(not(withSubstring("untuk produk tertentu"))))
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

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent))
        GeneralRobot.doScrollChatToPosition(0)
        clickPlusIconMenu()
        clickAttachVoucherMenu()

        // Then
        onView(withRecyclerView(R.id.rv_attachment_preview)
            .atPositionOnView(0, com.tokopedia.merchantvoucher.R.id.tvVoucherDesc))
            .check(matches(withSubstring("untuk produk tertentu")))
    }
}