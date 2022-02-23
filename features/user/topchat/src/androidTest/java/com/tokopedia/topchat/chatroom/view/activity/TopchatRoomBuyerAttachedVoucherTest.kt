package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot
import com.tokopedia.topchat.matchers.withRecyclerView
import org.junit.Test

class TopchatRoomBuyerAttachedVoucherTest: BaseBuyerTopchatRoomTest() {

    @Test
    fun should_open_product_list_from_voucher_page_when_click_product_voucher_on_mainapp() {
        // Given
        getChatUseCase.response = getChatUseCase.voucherAttachmentChatWithBuyerResponse
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        GeneralRobot.doScrollChatToPosition(0)
        Espresso.onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPosition(1)
        ).perform(ViewActions.click())

        // Then
        GeneralResult.openPageWithApplink("tokopedia://shop/10973651/voucher/7050189")
    }
}