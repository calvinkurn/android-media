package com.tokopedia.topchat.chatroom.view.activity.cassava

import android.content.Intent
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.header.HeaderRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductRobot
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatRoomCassavaTest : TopchatRoomTest() {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(true, false)

    private val gtmLogDbSource = GtmLogDBSource(context)

    @Before
    override fun before() {
        super.before()
        gtmLogDbSource.deleteAll().subscribe()
    }

    @Test
    fun chat_follow_shop_tracker() {
        // Given
        val journeyId = "117"
        getChatUseCase.response = getChatUseCase.defaultChatWithBuyerResponse
        launchChatRoomActivity()

        // When
        HeaderRobot.clickThreeDotsMenu()
        HeaderRobot.clickFollowMenu()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun chat_report_user() {
        // Given
        val journeyId = "125"
        getChatUseCase.response = getChatUseCase.defaultChatWithBuyerResponse
        launchChatRoomActivity()
        preventOpenOtherActivity()

        // When
        HeaderRobot.clickThreeDotsMenu()
        HeaderRobot.clickReportUserMenu()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun chat_send_chat() {
        // Given
        val journeyId = "88"
        getChatUseCase.response = getChatUseCase.defaultChatWithBuyerResponse
        launchChatRoomActivity()

        // When
        clickComposeArea()
        typeMessage("Hello")
        clickSendBtn()

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun chat_order_via_product_card() {
        // Given
        val journeyId = "108"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        preventOpenOtherActivity()

        // When
        ProductRobot.clickSingleProductCardAt(1)
        ProductRobot.clickSingleProductCardAtcButtonAt(1)
        ProductRobot.clickSingleProductCardBuyButtonAt(1)

        // Then
        // TODO: validate later when success get query Id from thanos
//        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    @Test
    fun chat_srw() {
        // Given
        val journeyId = "114"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwUseCase.defaultResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        clickSrwPreviewItemAt(0)

        // Then
        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
    }

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(getDefaultProductPreview())
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }
}