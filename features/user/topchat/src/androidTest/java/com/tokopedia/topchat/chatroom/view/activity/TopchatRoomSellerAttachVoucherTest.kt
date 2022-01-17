package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
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
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        GeneralRobot.doScrollChatToPosition(1)
        Espresso.onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPosition(1)).perform(click())

        // Then
        val intent = Intent(context, MerchantVoucherDetailActivity::class.java)
        GeneralResult.openPageWithIntent(intent)
    }
}