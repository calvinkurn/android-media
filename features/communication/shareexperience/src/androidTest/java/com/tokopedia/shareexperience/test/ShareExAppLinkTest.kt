package com.tokopedia.shareexperience.test

import com.tokopedia.shareexperience.stub.data.GqlResponseStub
import com.tokopedia.shareexperience.test.base.ShareExBaseTest
import com.tokopedia.shareexperience.test.robot.channelResult
import com.tokopedia.shareexperience.test.robot.channelRobot
import com.tokopedia.shareexperience.test.robot.propertiesResult
import com.tokopedia.shareexperience.test.robot.propertiesRobot
import org.junit.Test

class ShareExAppLinkTest : ShareExBaseTest() {
    @Test
    fun open_affiliate_registration() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_affiliate_register.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        propertiesRobot {
            clickAffiliateCardOn(2)
        }

        // Then
        propertiesResult {
            assertAffiliateAppLink()
        }
    }

    @Test
    fun open_whatsapp() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(0)
            clickSocialChannelOn(0)
        }

        // Then
        channelResult {
            assertWhatsapp()
        }
    }

    @Test
    fun open_fb_feed() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(1)
            clickSocialChannelOn(1)
        }

        // Then
        channelResult {
            assertFbFeed()
        }
    }

    @Test
    fun open_fb_story() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(2)
            clickSocialChannelOn(2)
        }

        // Then
        channelResult {
            assertFbStory()
        }
    }

    @Test
    fun open_ig_feed() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(3)
            clickSocialChannelOn(3)
        }

        // Then
        channelResult {
            assertIgFeed()
        }
    }

    @Test
    fun open_ig_story() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(4)
            clickSocialChannelOn(4)
        }

        // Then
        channelResult {
            assertIgStory()
        }
    }

    @Test
    fun open_ig_dm() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(5)
            clickSocialChannelOn(5)
        }

        // Then
        channelResult {
            assertIgDm()
        }
    }

    @Test
    fun open_line() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(6)
            clickSocialChannelOn(6)
        }

        // Then
        channelResult {
            assertLine()
        }
    }

    @Test
    fun open_twitter() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(7)
            clickSocialChannelOn(7)
        }

        // Then
        channelResult {
            assertTwitter()
        }
    }

    @Test
    fun open_telegram() {
        // When
        launchActivity()
        stubAllIntents()

        channelRobot {
            scrollSocialToPosition(8)
            clickSocialChannelOn(8)
        }

        // Then
        channelResult {
            assertTelegram()
        }
    }
}
