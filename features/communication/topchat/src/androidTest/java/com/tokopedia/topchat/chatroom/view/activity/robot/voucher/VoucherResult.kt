package com.tokopedia.topchat.chatroom.view.activity.robot.voucher

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.tokopedia.topchat.R
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.Matcher
import com.tokopedia.merchantvoucher.R as merchantvoucherR

object VoucherResult {

    fun assertInvoiceAttachmentDesc(
        position: Int,
        matcher: Matcher<View>
    ) {
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(position, merchantvoucherR.id.tvVoucherDesc)
        )
            .check(matches(matcher))
    }
}
