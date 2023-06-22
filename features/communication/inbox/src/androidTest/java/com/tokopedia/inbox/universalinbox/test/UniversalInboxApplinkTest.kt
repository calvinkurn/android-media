package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralRobot.scrollToPosition
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkChatBuyer
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkChatSeller
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkDiscussion
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkReview
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuRobot.clickMenuOnPosition
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertApplinkPDP
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationRobot.clickProductOnPosition
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertApplinkHelp
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetRobot.clickWidgetOnPosition
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxApplinkTest : BaseUniversalInboxTest() {
    @Test
    fun should_open_chat_buyer() {
        // When
        launchActivity()
        stubAllIntents()
        clickMenuOnPosition(2)

        // Then
        assertApplinkChatBuyer()
    }

    @Test
    fun should_open_chat_seller() {
        // When
        launchActivity()
        stubAllIntents()
        clickMenuOnPosition(3)

        // Then
        assertApplinkChatSeller()
    }

    @Test
    fun should_open_discussion() {
        // When
        launchActivity()
        stubAllIntents()
        clickMenuOnPosition(5)

        // Then
        assertApplinkDiscussion()
    }

    @Test
    fun should_open_review() {
        // When
        launchActivity()
        stubAllIntents()
        clickMenuOnPosition(6)

        // Then
        assertApplinkReview()
    }

    @Test
    fun should_open_help() {
        // When
        launchActivity()
        stubAllIntents()
        clickWidgetOnPosition(1)

        // Then
        assertApplinkHelp()
    }

    @Test
    fun should_open_pdp_from_product_recomm() {
        // When
        launchActivity()
        stubAllIntents()
        scrollToPosition(12)
        clickProductOnPosition(12)

        // Then
        assertApplinkPDP()
    }
}
