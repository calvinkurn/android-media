package com.tokopedia.universal_sharing.test.robot.universalsharing

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.stub.common.MockTimber
import com.tokopedia.universal_sharing.stub.common.hasMediaName
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ChipViewHolder
import org.hamcrest.CoreMatchers

class UniversalSharingResult {

    fun shouldShowTitleBottomSheet() {
        Espresso.onView(ViewMatchers.withText(R.string.label_to_social_media_text))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(1000)
    }

    fun shouldShowTitleHeadingImageOptions() {
        Espresso.onView(ViewMatchers.withText(R.string.img_options_heading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowTitleChipOptions() {
        Espresso.onView(ViewMatchers.withText(R.string.chip_options_heading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowImagesOptions(position: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.image_list_container))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(RecyclerViewMatcher(R.id.image_list_container).atPosition(position))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.checked_circle))))
    }

    fun shouldShowThumbnailShare() {
        Espresso.onView(ViewMatchers.withId(R.id.thumb_nail_image))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.thumb_nail_title))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tokopedia_link))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowRegisterAffiliateTicker() {
        Espresso.onView(ViewMatchers.withId(R.id.iv_affiliate))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_title_affiliate))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_new))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.tv_description_affiliate))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.iv_right_arrow))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowCommissionAffiliate() {
        Espresso.onView(ViewMatchers.withId(R.id.affilate_commision))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldHideCommissionAffiliate() {
        Espresso.onView(ViewMatchers.withId(R.id.affilate_commision))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun shouldShowDefaultShareMediaList() {
        Espresso.onView(ViewMatchers.withId(R.id.copy_link_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.copy_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.sms_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.sms_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.email_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.email_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.others_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.others_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * without sharing SMS if setImageOnlySharingOption(true)
     */
    fun shouldShowShareMediaListIfImageOnlySharingOptions() {
        Espresso.onView(ViewMatchers.withId(R.id.copy_link_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.copy_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.email_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.email_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.others_img))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.others_link_txtv))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowTabChips() {
        Espresso.onView(ViewMatchers.withId(R.id.lst_chip))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.lst_chip))
            .check(ViewAssertions.matches(hasViewHolderOf(ChipViewHolder::class.java)))
    }

    fun shouldShowSelectionTabChips(position: Int) {
        Espresso.onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(position))
            .check(ViewAssertions.matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.view_chip))))
    }

    fun hasTitleChipSharing(title: String) {
        assert(title.isNotEmpty())
    }

    fun hasAffiliateIntent() {
        Intents.intended(IntentMatchers.hasData(ApplinkConst.AFFILIATE_ONBOARDING))
    }

    fun shouldHaveMediaNameLainnya(timber: MockTimber) {
        ViewMatchers.assertThat(timber, hasMediaName("Lainnya"))
    }
}
