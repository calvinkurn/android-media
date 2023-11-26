package com.tokopedia.inbox.universalinbox.test

import com.tokopedia.inbox.universalinbox.test.base.BaseUniversalInboxTest
import com.tokopedia.inbox.universalinbox.test.robot.generalRobot
import com.tokopedia.inbox.universalinbox.test.robot.menuResult
import com.tokopedia.inbox.universalinbox.test.robot.menuRobot
import com.tokopedia.inbox.universalinbox.test.robot.recommendationResult
import com.tokopedia.inbox.universalinbox.test.robot.recommendationRobot
import com.tokopedia.inbox.universalinbox.test.robot.widgetResult
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
        Thread.sleep(1000)
        menuResult {
            assertApplinkChatBuyer()
        }
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
        Thread.sleep(1000)
        menuResult {
            assertApplinkChatSeller()
        }
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
        Thread.sleep(1000)
        menuResult {
            assertApplinkReview()
        }
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
        Thread.sleep(1000)
        widgetResult {
            assertApplinkHelp()
        }
    }

    @Test
    fun should_open_pdp_from_product_recomm() {
        // When
        launchActivity()
        stubAllIntents()
        generalRobot {
            scrollToPosition(8) // trigger rv load
            scrollToPosition(11)
        }
        recommendationRobot {
            clickProductOnPosition(11)
        }

        // Then
        Thread.sleep(1000)
        recommendationResult {
            assertApplinkPDP()
        }
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
        Thread.sleep(1000)
        widgetResult {
            assertApplinkChatListDriver()
        }
    }
}
