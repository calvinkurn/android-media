package com.tokopedia.shareexperience.test.robot.properties

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.shareexperience.R
import com.tokopedia.shareexperience.stub.common.matcher.atPositionCheckInstanceOf
import com.tokopedia.shareexperience.stub.common.matcher.atPositionCheckNotInstanceOf
import com.tokopedia.shareexperience.stub.common.matcher.withRecyclerView
import com.tokopedia.shareexperience.stub.common.matcher.withTotalItem
import com.tokopedia.shareexperience.ui.model.ShareExAffiliateRegistrationUiModel
import com.tokopedia.shareexperience.ui.model.ShareExErrorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSeparatorUiModel
import com.tokopedia.shareexperience.ui.model.ShareExSubtitleUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExCommonChannelUiModel
import com.tokopedia.shareexperience.ui.model.channel.ShareExSocialChannelUiModel
import com.tokopedia.shareexperience.ui.model.chip.ShareExChipsUiModel
import com.tokopedia.shareexperience.ui.model.image.ShareExImageCarouselUiModel
import com.tokopedia.unifycomponents.R as unifycomponentsR

object PropertiesResult {

    fun assertTitle(title: String) {
        onView(withSubstring(title)).check(
            matches(isDisplayed())
        )
    }

    fun assertSubtitle(subtitleText: String) {
        onView(withId(R.id.shareex_tv_subtitle))
            .check(matches(withText(subtitleText)))
    }

    fun assertSubtitleAt(position: Int, not: Boolean = false) {
        val matcher = if (!not) {
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExSubtitleUiModel::class.java
            )
        } else {
            atPositionCheckNotInstanceOf(
                position = position,
                expectedClass = ShareExSubtitleUiModel::class.java
            )
        }
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(matcher)
    }

    fun assertChipsAt(position: Int, not: Boolean = false) {
        val matcher = if (!not) {
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExChipsUiModel::class.java
            )
        } else {
            atPositionCheckNotInstanceOf(
                position = position,
                expectedClass = ShareExChipsUiModel::class.java
            )
        }
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(matcher)
    }

    fun assertChipTotal(total: Int) {
        onView(withId(R.id.shareex_rv_chip)).check(
            matches(withTotalItem(total))
        )
    }

    fun assertChipItemAt(position: Int, text: String) {
        onView(
            withRecyclerView(R.id.shareex_rv_chip)
                .atPositionOnView(position, unifycomponentsR.id.chip_text)
        ).check(matches(withText(text)))
    }

    fun assertImageCarouselAt(position: Int, not: Boolean = false) {
        val matcher = if (!not) {
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExImageCarouselUiModel::class.java
            )
        } else {
            atPositionCheckNotInstanceOf(
                position = position,
                expectedClass = ShareExImageCarouselUiModel::class.java
            )
        }
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(matcher)
    }

    fun assertImageCarouselTotal(total: Int) {
        onView(withId(R.id.shareex_rv_image_carousel)).check(
            matches(withTotalItem(total))
        )
    }

    fun assertImageAt(position: Int, isSelected: Boolean) {
        val matcher = if (isSelected) {
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        } else {
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        }
        onView(
            withRecyclerView(R.id.shareex_rv_image_carousel)
                .atPositionOnView(position, R.id.shareex_icon_checkmark)
        ).check(matches(matcher))
        onView(
            withRecyclerView(R.id.shareex_rv_image_carousel)
                .atPositionOnView(position, R.id.shareex_border_selected_image)
        ).check(matches(matcher))
    }

    fun assertShareLinkBody(
        title: String,
        commissionText: String,
        label: String,
        date: String
    ) {
        onView(withId(R.id.shareex_tv_title_link))
            .check(matches(withText(title)))

        if (commissionText.isNotBlank()) {
            onView(withId(R.id.shareex_tv_commision_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
            onView(withId(R.id.shareex_tv_commision_link))
                .check(matches(withText(commissionText)))
        } else {
            onView(withId(R.id.shareex_tv_commision_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }

        if (label.isNotBlank()) {
            onView(withId(R.id.shareex_label_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
            onView(withId(R.id.shareex_label_link))
                .check(matches(withText(label)))
        } else {
            onView(withId(R.id.shareex_label_link))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }

        if (date.isNotBlank()) {
            onView(withId(R.id.shareex_tv_date))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
            onView(withId(R.id.shareex_tv_date))
                .check(matches(withText(date)))
        } else {
            onView(withId(R.id.shareex_tv_date))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        }
    }

    fun assertSeparatorLine(position: Int) {
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExSeparatorUiModel::class.java
            )
        )
    }

    fun assertRegisterAffiliateAt(position: Int, not: Boolean = false) {
        val matcher = if (!not) {
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExAffiliateRegistrationUiModel::class.java
            )
        } else {
            atPositionCheckNotInstanceOf(
                position = position,
                expectedClass = ShareExAffiliateRegistrationUiModel::class.java
            )
        }
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(matcher)
    }

    fun assertRegisterAffiliate(
        title: String,
        label: String,
        desc: String
    ) {
        onView(withId(R.id.shareex_tv_register_affiliate_title))
            .check(matches(withText(title)))
        onView(withId(R.id.shareex_label_register_affiliate))
            .check(matches(withText(label)))
        onView(withId(R.id.shareex_tv_register_affiliate_desc))
            .check(matches(withText(desc)))
    }

    fun assertSocialChannelAt(position: Int) {
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExSocialChannelUiModel::class.java
            )
        )
    }

    fun assertSocialChannelTotal(total: Int) {
        onView(withId(R.id.shareex_rv_channel_social)).check(
            matches(withTotalItem(total))
        )
    }

    fun assertCommonChannelAt(position: Int) {
        onView(withId(R.id.shareex_rv_bottom_sheet)).check(
            atPositionCheckInstanceOf(
                position = position,
                expectedClass = ShareExCommonChannelUiModel::class.java
            )
        )
    }

    fun assertCommonChannelTotal(total: Int) {
        onView(withId(R.id.shareex_rv_channel_common)).check(
            matches(withTotalItem(total))
        )
    }

    fun assertErrorView(not: Boolean = false) {
        if (!not) {
            onView(withId(R.id.shareex_global_error)).check(matches(isDisplayed()))
        } else {
            onView(withId(R.id.shareex_rv_bottom_sheet)).check(
                atPositionCheckNotInstanceOf(
                    position = 0,
                    expectedClass = ShareExErrorUiModel::class.java
                )
            )
        }
    }

    fun assertAffiliateAppLink() {
        Intents.intended(
            IntentMatchers.hasData("tokopedia://affiliate")
        )
    }
}
