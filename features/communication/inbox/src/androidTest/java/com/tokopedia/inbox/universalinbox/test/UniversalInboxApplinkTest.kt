package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkChatBuyer
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkChatSeller
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkDiscussion
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuResult.assertApplinkReview
import com.tokopedia.inbox.universalinbox.test.robot.menuRobot
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationResult.assertApplinkPDP
import com.tokopedia.inbox.universalinbox.test.robot.recommendationRobot
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertApplinkChatListDriver
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetResult.assertApplinkHelp
import com.tokopedia.inbox.universalinbox.test.robot.widgetRobot
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class UniversalInboxApplinkTest : BaseUniversalInboxTest() {
    @Test
    fun should_open_chat_buyer() {
        // When
        launchActivity()
        stubAllIntents()
        menuRobot {
            clickMenuOnPosition(1)
        }
        // Then
        assertApplinkChatBuyer()
    }

    @Test
    fun should_open_chat_seller() {
        // When
        launchActivity()
        stubAllIntents()
        menuRobot {
            clickMenuOnPosition(2)
        }

        // Then
        assertApplinkChatSeller()
    }

    @Test
    fun should_open_discussion() {
        // When
        launchActivity()
        stubAllIntents()
        menuRobot {
            clickMenuOnPosition(3)
        }

        // Then
        assertApplinkDiscussion()
    }

    @Test
    fun should_open_review() {
        // When
        launchActivity()
        stubAllIntents()
        menuRobot {
            clickMenuOnPosition(4)
        }

        // Then
        assertApplinkReview()
    }

    @Test
    fun should_open_help() {
        // When
        launchActivity()
        stubAllIntents()
        widgetRobot {
            clickWidgetOnPosition(1)
        }

        // Then
        assertApplinkHelp()
    }

    @Test
    fun should_open_pdp_from_product_recomm() {
        // When
        launchActivity()
        stubAllIntents()
        generalRobot {
            scrollToPosition(11) // trigger rv load
            scrollToPosition(11)
        }
        recommendationRobot {
            clickProductOnPosition(11)
        }

        // Then
        assertApplinkPDP()
    }

    @Test
    fun should_open_chat_list_driver() {
        // When
        launchActivity()
        stubAllIntents()
        widgetRobot {
            clickWidgetOnPosition(0)
        }

        // Then
        assertApplinkChatListDriver()
    }
}
