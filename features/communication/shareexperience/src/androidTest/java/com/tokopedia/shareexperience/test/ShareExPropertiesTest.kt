package com.tokopedia.shareexperience.test

import com.tokopedia.shareexperience.domain.model.channel.ShareExChannelModel
import com.tokopedia.shareexperience.stub.data.GqlResponseStub
import com.tokopedia.shareexperience.test.base.ShareExBaseTest
import com.tokopedia.shareexperience.test.robot.generalResult
import com.tokopedia.shareexperience.test.robot.propertiesResult
import com.tokopedia.shareexperience.test.robot.propertiesRobot
import org.junit.Test

class ShareExPropertiesTest : ShareExBaseTest() {

    @Test
    fun assert_share_default_view() {
        // Given
        GqlResponseStub.sharePropertiesResponse.isError = true

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertErrorView(not = true)

            assertSocialChannelAt(0)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(1)
            assertCommonChannelTotal(4)
        }
        generalResult {
            assertRvTotalItem(2)
        }
    }

    @Test
    fun assert_share_error_view() {
        // Given
        GqlResponseStub.sharePropertiesResponse.isError = true

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertErrorView()
        }
        generalResult {
            assertRvTotalItem(1)
        }
    }

    @Test
    fun assert_share_card_only() {
        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertTitle("Belanja rame-rame pasti lebih seru!")

            assertSubtitle("Ini subtitle")
            assertSubtitleAt(0)

            assertChipsAt(1, not = true)
            assertImageCarouselAt(1, not = true)

            assertShareLinkBody( // pos 1
                title = "Jual Full Update Product KVI, lagi murah loh!",
                commissionText = "",
                label = "",
                date = ""
            )
            assertSeparatorLine(2)
            assertRegisterAffiliateAt(3, not = true)

            assertSocialChannelAt(3)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(4)
            assertCommonChannelTotal(4)
        }
    }

    @Test
    fun assert_share_with_image_thumbnails() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_with_image_thumbnails.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertTitle("Belanja rame-rame pasti lebih seru!")

            assertSubtitle("Ini subtitle")
            assertSubtitleAt(0)

            assertChipsAt(1, not = true)

            assertImageCarouselAt(1)
            assertImageCarouselTotal(3)
            assertImageAt(0, true)
            assertImageAt(1, false)
            assertImageAt(2, false)

            assertShareLinkBody( // pos 2
                title = "Jual Full Update Product KVI, lagi murah loh!",
                commissionText = "",
                label = "",
                date = ""
            )
            assertSeparatorLine(3)
            assertRegisterAffiliateAt(4, not = true)

            assertSocialChannelAt(4)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(5)
            assertCommonChannelTotal(4)
        }
    }

    @Test
    fun assert_share_with_chips() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_with_chips.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertTitle("Belanja rame-rame pasti lebih seru!")

            assertSubtitleAt(0, not = true)

            assertChipsAt(0)
            assertChipTotal(4)
            assertChipItemAt(0, "One")
            assertChipItemAt(1, "Two")
            assertChipItemAt(2, "Three")
            assertChipItemAt(3, "Four")

            assertImageCarouselAt(1, not = true)

            assertShareLinkBody( // pos 1
                title = "Jual Full Update Product KVI, lagi murah loh!",
                commissionText = "",
                label = "",
                date = ""
            )
            assertSeparatorLine(2)
            assertRegisterAffiliateAt(3, not = true)

            assertSocialChannelAt(3)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(4)
            assertCommonChannelTotal(4)
        }
    }

    @Test
    fun assert_share_with_affiliate_registration() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_affiliate_register.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertTitle("Belanja rame-rame pasti lebih seru!")

            assertSubtitleAt(0, not = true)
            assertChipsAt(0, not = true)
            assertImageCarouselAt(0, not = true)

            assertShareLinkBody(
                title = "Jual Full Update Product KVI, lagi murah loh!",
                commissionText = "",
                label = "",
                date = ""
            )
            assertSeparatorLine(1)
            assertRegisterAffiliateAt(2)
            assertRegisterAffiliate(
                title = "Tokopedia Affiliate",
                label = "BARU",
                desc = "User Is Not Registered"
            )

            assertSocialChannelAt(3)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(4)
            assertCommonChannelTotal(4)
        }
    }

    @Test
    fun assert_share_with_affiliate_eligible() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_affiliate_eligible.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertTitle("Belanja rame-rame pasti lebih seru!")

            assertSubtitleAt(0, not = true)
            assertChipsAt(0, not = true)
            assertImageCarouselAt(0, not = true)

            assertShareLinkBody(
                title = "Jual Full Update Product KVI, lagi murah loh!",
                commissionText = "Komisi hingga Rp0/barang terjual",
                label = "Komisi Extra",
                date = "Hingga 31 Des 2051"
            )
            assertSeparatorLine(1)
            assertRegisterAffiliateAt(2, not = true)

            assertSocialChannelAt(2)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(3)
            assertCommonChannelTotal(4)
        }
    }

    @Test
    fun assert_share_with_affiliate_eligible_without_label() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_affiliate_eligible_without_label.json"

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertTitle("Belanja rame-rame pasti lebih seru!")

            assertSubtitleAt(0, not = true)
            assertChipsAt(0, not = true)
            assertImageCarouselAt(0, not = true)

            assertShareLinkBody(
                title = "Jual Full Update Product KVI, lagi murah loh!",
                commissionText = "Komisi hingga Rp0/barang terjual",
                label = "",
                date = ""
            )
            assertSeparatorLine(1)
            assertRegisterAffiliateAt(2, not = true)

            assertSocialChannelAt(2)
            assertSocialChannelTotal(9)

            assertCommonChannelAt(3)
            assertCommonChannelTotal(4)
        }
    }

    @Test
    fun assert_share_without_social_channels() {
        // Given
        GqlResponseStub.sharePropertiesResponse.isError = true
        channelMapperStub.socialChannel = ShareExChannelModel()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        Thread.sleep(10000)
        propertiesResult {
            assertCommonChannelAt(0)
            assertCommonChannelTotal(4)
        }
        generalResult {
            assertRvTotalItem(1)
        }
    }

    @Test
    fun assert_share_without_default_channels() {
        // Given
        GqlResponseStub.sharePropertiesResponse.isError = true
        channelMapperStub.stubCommonChannel = true

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertSocialChannelAt(0)
            assertSocialChannelTotal(9)
        }
        generalResult {
            assertRvTotalItem(1)
        }
    }

    @Test
    fun assert_chip_click() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_with_chips.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertChipsAt(0)
            assertChipTotal(4)
            assertChipItemAt(0, "One")
            assertChipItemAt(1, "Two")
            assertChipItemAt(2, "Three")
            assertChipItemAt(3, "Four")

            assertShareLinkTitle("Jual Full Update Product KVI, lagi murah loh!")
        }

        // When
        propertiesRobot {
            clickChipOn(1)
        }

        // Then
        propertiesResult {
            assertShareLinkTitle("Properties tab ke 2")
        }

        // When
        propertiesRobot {
            clickChipOn(2)
        }

        // Then
        propertiesResult {
            assertShareLinkTitle("Properties tab ke 3")
        }

        // When
        propertiesRobot {
            clickChipOn(3)
        }

        // Then
        propertiesResult {
            assertShareLinkTitle("Properties tab ke 4")
        }
    }

    @Test
    fun assert_image_click() {
        // Given
        GqlResponseStub.sharePropertiesResponse.filePath = "properties/share_properties_with_image_thumbnails.json"
        GqlResponseStub.sharePropertiesResponse.updateResponseObject()

        // When
        launchActivity()
        stubAllIntents()

        // Then
        propertiesResult {
            assertImageCarouselTotal(3)
            assertImageAt(0, true)
            assertImageAt(1, false)
            assertImageAt(2, false)
        }

        // When
        propertiesRobot {
            clickImageOn(1)
        }

        // Then
        propertiesResult {
            assertImageAt(0, false)
            assertImageAt(1, true)
            assertImageAt(2, false)
        }

        // When
        propertiesRobot {
            clickImageOn(2)
        }

        // Then
        propertiesResult {
            assertImageAt(0, false)
            assertImageAt(1, false)
            assertImageAt(2, true)
        }
    }
}
