package com.tokopedia.universal_sharing.test.robot.universalsharing

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.test.application.matcher.hasViewHolderOf
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.stub.common.MockTimber
import com.tokopedia.universal_sharing.stub.common.hasMediaName
import com.tokopedia.universal_sharing.view.bottomsheet.viewholder.ChipViewHolder
import org.hamcrest.CoreMatchers

object UniversalSharingResult {

    fun shouldShowTitleBottomSheet() {
        onView(withText(R.string.label_to_social_media_text))
            .check(matches(ViewMatchers.isDisplayed()))
        Thread.sleep(1000)
    }

    fun shouldShowTitleHeadingImageOptions() {
        onView(withText(R.string.img_options_heading))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowTitleChipOptions() {
        onView(withText(R.string.chip_options_heading))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowImagesOptions(position: Int) {
        onView(ViewMatchers.withId(R.id.image_list_container))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(RecyclerViewMatcher(R.id.image_list_container).atPosition(position))
            .check(matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.checked_circle))))
    }

    fun shouldShowThumbnailShare() {
        onView(ViewMatchers.withId(R.id.thumb_nail_image))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.thumb_nail_title))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.tokopedia_link))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowRegisterAffiliateTicker() {
        onView(ViewMatchers.withId(R.id.iv_affiliate))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.tv_title_affiliate))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.tv_new))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.tv_description_affiliate))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.iv_right_arrow))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowCommissionAffiliate() {
        onView(ViewMatchers.withId(R.id.affilate_commision))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun shouldHideCommissionAffiliate() {
        onView(ViewMatchers.withId(R.id.affilate_commision))
            .check(matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

    fun shouldShowDefaultShareMediaList() {
        onView(ViewMatchers.withId(R.id.copy_link_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.copy_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.sms_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.sms_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.email_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.email_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.others_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.others_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    /**
     * without sharing SMS if setImageOnlySharingOption(true)
     */
    fun shouldShowShareMediaListIfImageOnlySharingOptions() {
        onView(ViewMatchers.withId(R.id.copy_link_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.copy_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.email_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.email_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.others_img))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.others_link_txtv))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun shouldShowTabChips() {
        onView(ViewMatchers.withId(R.id.lst_chip))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.lst_chip))
            .check(matches(hasViewHolderOf(ChipViewHolder::class.java)))
    }

    fun shouldShowSelectionTabChips(position: Int) {
        onView(RecyclerViewMatcher(R.id.lst_chip).atPosition(position))
            .check(matches(ViewMatchers.hasDescendant(ViewMatchers.withId(R.id.view_chip))))
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
