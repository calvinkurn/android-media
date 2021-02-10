package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.andThen
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 09/02/21
 */
class PlayViewModelUpdatePageTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val shareInfoBuilder = PlayShareInfoModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val totalViewBuilder = PlayTotalViewModelBuilder()
    private val likeBuilder = PlayLikeModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

    private val responseBuilder = PlayResponseBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given channel data is set, when page is focused, then like info should be updated accordingly`() {
        val isLiked = true

        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeBuilder.buildCompleteData(
                        status = likeBuilder.buildStatus(isLiked = false)
                )
        )
        val expectedModel = likeBuilder.buildStatus(
                isLiked = isLiked
        )

        givenPlayViewModelRobot {
            setMockResponseReportSummaries(responseBuilder.buildReportSummariesResponse())
            setMockResponseIsLike(isLiked)
        } andWhen {
            createPage(channelData)
        } andThen {
            focusPage(channelData)
        } thenVerify {
            likeStatusResult
                    .isEqualTo(expectedModel)
        }
    }
}