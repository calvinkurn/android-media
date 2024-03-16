package com.tokopedia.topchat.chatroom.view.activity.robot.invoice

import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult

object InvoiceResult {
    fun assertInvoiceAt(position: Int, status: String) {
        generalResult {
            assertViewInRecyclerViewAt(
                position,
                R.id.tv_status,
                withText(status)
            )
        }
    }
}
