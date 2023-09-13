package com.tokopedia.universal_sharing

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
import com.tokopedia.common.stub.FakeGraphqlRepository
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ChipViewHolder
import org.hamcrest.CoreMatchers

class UniversalSharingRobot {

    fun atClickChips(position: Int) {
        onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(position)).perform(ViewActions.click())
    }

    fun atScrollChips(position: Int) {
        onView(withId(R.id.lst_chip)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    fun atScrollImagesOptions(position: Int) {
        onView(withId(R.id.image_list_container)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position))
    }

    fun atClickOneImageOptions(position: Int) {
        onView(RecyclerViewMatcher(R.id.image_list_container).atPosition(position)).perform(ViewActions.click())
    }

    fun atClickShareLainnya() {
        onView(withId(R.id.others_img)).perform(ViewActions.click())
    }

    fun atClickRegisterAffiliate() {
        onView(withId(R.id.card_register_affiliate)).perform(ViewActions.click())
        Thread.sleep(2000)
    }
}

class UniversalSharingResultRobot {

    fun shouldShowTitleBottomSheet() {
        onView(withText(R.string.label_to_social_media_text)).check(matches(isDisplayed()))
        Thread.sleep(1000)
    }

    fun shouldShowTitleHeadingImageOptions() {
        onView(withText(R.string.img_options_heading)).check(matches(isDisplayed()))
    }

    fun shouldShowTitleChipOptions() {
        onView(withText(R.string.chip_options_heading)).check(matches(isDisplayed()))
    }

    fun shouldShowImagesOptions(position: Int) {
        onView(withId(R.id.image_list_container)).check(matches(isDisplayed()))
        onView(RecyclerViewMatcher(R.id.image_list_container).atPosition(position))
            .check(matches(hasDescendant(withId(R.id.checked_circle))))
    }

    fun shouldShowThumbnailShare() {
        onView(withId(R.id.thumb_nail_image)).check(matches(isDisplayed()))
        onView(withId(R.id.thumb_nail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tokopedia_link)).check(matches(isDisplayed()))
    }

    fun shouldShowRegisterAffiliateTicker() {
        onView(withId(R.id.iv_affiliate)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_title_affiliate)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_new)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_description_affiliate)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_right_arrow)).check(matches(isDisplayed()))
    }

    fun shouldShowCommissionAffiliate() {
        onView(withId(R.id.affilate_commision)).check(matches(isDisplayed()))
    }

    fun shouldHideCommissionAffiliate() {
        onView(withId(R.id.affilate_commision)).check(matches(CoreMatchers.not(isDisplayed())))
    }

    fun shouldShowDefaultShareMediaList() {
        onView(withId(R.id.copy_link_img)).check(matches(isDisplayed()))
        onView(withId(R.id.copy_link_txtv)).check(matches(isDisplayed()))
        onView(withId(R.id.sms_img)).check(matches(isDisplayed()))
        onView(withId(R.id.sms_link_txtv)).check(matches(isDisplayed()))
        onView(withId(R.id.email_img)).check(matches(isDisplayed()))
        onView(withId(R.id.email_link_txtv)).check(matches(isDisplayed()))
        onView(withId(R.id.others_img)).check(matches(isDisplayed()))
        onView(withId(R.id.others_link_txtv)).check(matches(isDisplayed()))
    }

    /**
     * without sharing SMS if setImageOnlySharingOption(true)
     */
    fun shouldShowShareMediaListIfImageOnlySharingOptions() {
        onView(withId(R.id.copy_link_img)).check(matches(isDisplayed()))
        onView(withId(R.id.copy_link_txtv)).check(matches(isDisplayed()))
        onView(withId(R.id.email_img)).check(matches(isDisplayed()))
        onView(withId(R.id.email_link_txtv)).check(matches(isDisplayed()))
        onView(withId(R.id.others_img)).check(matches(isDisplayed()))
        onView(withId(R.id.others_link_txtv)).check(matches(isDisplayed()))
    }

    fun shouldShowTabChips() {
        onView(withId(R.id.lst_chip)).check(matches(isDisplayed()))
        onView(withId(R.id.lst_chip)).check(matches(hasViewHolderOf(ChipViewHolder::class.java)))
    }

    fun shouldShowSelectionTabChips(position: Int) {
        onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(position))
            .check(matches(hasDescendant(withId(R.id.view_chip))))
    }

    fun hasTitleChipSharing(title: String) {
        assert(title.isNotEmpty())
    }

    fun hasAffiliateIntent() {
        Intents.intended(IntentMatchers.hasData(ApplinkConst.AFFILIATE_ONBOARDING))
    }

    fun shouldHaveMediaNameLainnya(timber: MockTimber) {
        assertThat(timber, hasMediaName("Lainnya"))
    }
}

fun universalSharingRobot(func: UniversalSharingRobot.() -> Unit): UniversalSharingRobot {
    return UniversalSharingRobot().apply(func)
}

infix fun UniversalSharingRobot.validate(func: UniversalSharingResultRobot.() -> Unit): UniversalSharingResultRobot {
    // in KYC, there is no manual CTA Button to upload, so we just wait
    java.lang.Thread.sleep(1000)
    return UniversalSharingResultRobot().apply(func)
}

fun stubAppGraphqlRepo(): BaseAppComponent {
    val baseAppComponent = DaggerBaseAppComponent.builder()
        .appModule(object : AppModule(ApplicationProvider.getApplicationContext()) {
            override fun provideGraphqlRepository(): GraphqlRepository {
                return FakeGraphqlRepository()
            }
        }).build()
    ApplicationProvider.getApplicationContext<BaseMainApplication>().setComponent(baseAppComponent)
    return baseAppComponent
}
