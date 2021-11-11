package com.tokopedia.topchat.chatroom.view.activity.cassava

import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.header.HeaderRobot
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
        // TODO: validate later when eventLabel value fixed from DA
//        assertThat(cassavaTestRule.validate(journeyId), hasAllSuccess())
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
}