package com.tokopedia.universal_sharing.test.robot.universalsharing

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.abstraction.common.di.module.AppModule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.universal_sharing.stub.data.repository.FakeGraphqlRepository
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ChipViewHolder
import org.hamcrest.CoreMatchers

class UniversalSharingRobot {

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
