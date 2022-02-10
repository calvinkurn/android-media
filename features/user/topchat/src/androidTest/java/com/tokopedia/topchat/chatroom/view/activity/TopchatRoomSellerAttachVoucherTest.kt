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
    fun should_show_additional_text_in_private_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        launchChatRoomActivity(isSellerApp = true)

        //When
        GeneralRobot.doScrollChatToPosition(0)

        // Then
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(0, com.tokopedia.merchantvoucher.R.id.tvVoucherDesc))
            .check(matches(withSubstring("untuk produk tertentu")))
    }

    @Test
    fun should_show_additional_text_in_preview_private_voucher() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithSellerResponse
        val voucherData = "{\"amount\":20000,\"amountType\":1,\"desktopUrl\":\"https://images.tokopedia.net/img/BTJGre/2022/1/25/a5dad590-e6d3-466b-9c8f-6ea81ad1632d.jpg\",\"identifier\":\"1\",\"isPublic\":0,\"minimumSpend\":100000,\"mobileUrl\":\"https://images.tokopedia.net/img/BTJGre/2022/1/25/a5dad590-e6d3-466b-9c8f-6ea81ad1632d.jpg\",\"source\":\"inbox\",\"tnc\":\"\\u003col\\u003e\\u003cli\\u003eVoucher Gratis Ongkir dapat digunakan dengan membuka halaman promo di Keranjang.\\u003c/li\\u003e\\u003cli\\u003eVoucher Gratis Ongkir hanya berlaku apabila pembelian Pengguna sudah memenuhi syarat dan ketentuan yang tertera pada voucher.\\u003c/li\\u003e\\u003cli\\u003eNominal Gratis Ongkir yang bisa didapatkan sebesar Rp 20.000.\\u003c/li\\u003e\\u003cli\\u003eVoucher Gratis Ongkir berlaku untuk transaksi minimal Rp 100.000 (tidak termasuk ongkos kirim dan biaya tambahan lainnya).\\u003c/li\\u003e\\u003cli\\u003eVoucher Gratis Ongkir hanya berlaku untuk pembelanjaan di 10973651br04dc4st-s4tu.\\u003c/li\\u003e\\u003cli\\u003e1 (satu) Pengguna Tokopedia hanya boleh menggunakan 1 (satu) akun Tokopedia untuk menggunakan Voucher Gratis Ongkir ini.\\u003c/li\\u003e\\u003cli\\u003ePromo tidak berlaku untuk produk Logam Mulia, Emas Batangan, Voucher, dan Paket Data.\\u003c/li\\u003e\\u003cli\\u003eTokopedia berhak melakukan tindakan yang diperlukan apabila diduga terjadi tindakan kecurangan yang dilakukan oleh Pengguna dan/atau melanggar Syarat dan Ketentuan Situs dan/atau merugikan pihak Tokopedia.\\u003c/li\\u003e\\u003cli\\u003eDengan menggunakan Voucher Gratis Ongkir, Pengguna dianggap telah memahami dan menyetujui semua Syarat dan Ketentuan yang berlaku.\\u003c/li\\u003e\\u003c/ol\\u003e\",\"validThru\":1645576200,\"voucherCode\":\"1097122921\",\"voucherId\":7297979,\"voucherName\":\"gratis ongkir gratis ongkir gr\",\"voucherType\":1}"
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