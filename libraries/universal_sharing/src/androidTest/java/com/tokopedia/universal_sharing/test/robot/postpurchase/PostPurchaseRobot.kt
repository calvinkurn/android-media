package com.tokopedia.universal_sharing.test.robot.postpurchase

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.stub.common.matcher.withRecyclerView

object PostPurchaseRobot {
    fun clickOnShareButton(position: Int) {
        onView(
            withRecyclerView(R.id.universal_sharing_post_purchase_rv)
                .atPositionOnView(position, R.id.universal_sharing_layout_btn_share)
        ).perform(click())
    }
}
