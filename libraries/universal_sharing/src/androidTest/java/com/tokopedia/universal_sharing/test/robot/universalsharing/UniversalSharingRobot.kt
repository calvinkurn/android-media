package com.tokopedia.universal_sharing.test.robot.universalsharing

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.universal_sharing.R

object UniversalSharingRobot {

    fun clickOnSpecificChip(position: Int) {
        onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(position)).perform(ViewActions.click())
    }

    fun scrollHorizontalOnChips(position: Int) {
        onView(withId(R.id.lst_chip)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    fun scrollHorizontalIOnImagesOptions(position: Int) {
        onView(withId(R.id.image_list_container)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    fun clickOnSpecificImageOption(position: Int) {
        onView(RecyclerViewMatcher(R.id.image_list_container).atPosition(position)).perform(ViewActions.click())
    }

    fun clickShareLainnyaButton() {
        onView(withId(R.id.others_img)).perform(ViewActions.click())
    }

    fun clickOnRegisterAffiliateButton() {
        onView(withId(R.id.card_register_affiliate)).perform(ViewActions.click())
        Thread.sleep(2000)
    }
}
