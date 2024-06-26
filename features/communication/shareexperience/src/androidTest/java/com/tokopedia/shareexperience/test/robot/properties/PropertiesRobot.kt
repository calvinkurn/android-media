package com.tokopedia.shareexperience.test.robot.properties

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.stub.common.matcher.withRecyclerView

object PropertiesRobot {
    fun clickAffiliateCardOn(position: Int) {
        onView(
            withRecyclerView(R.id.shareex_rv_bottom_sheet)
                .atPositionOnView(position, R.id.shareex_tv_register_affiliate_title)
        ).perform(click())
    }

    fun clickImageOn(position: Int) {
        onView(
            withRecyclerView(R.id.shareex_rv_image_carousel)
                .atPositionOnView(position, R.id.shareex_iv_image_item)
        ).perform(click())
    }

    fun clickChipOn(position: Int) {
        onView(
            withRecyclerView(R.id.shareex_rv_chip)
                .atPositionOnView(position, R.id.shareex_chip)
        ).perform(click())
    }
}
