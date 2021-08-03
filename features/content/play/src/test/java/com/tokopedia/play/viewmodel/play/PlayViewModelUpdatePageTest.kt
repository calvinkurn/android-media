package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.andThen
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.ui.toolbar.model.PartnerType
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 09/02/21
 */
class PlayViewModelUpdatePageTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val likeBuilder = PlayLikeModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()

    private val responseBuilder = PlayResponseBuilder()
    private val classBuilder = ClassBuilder()
    private val uiModelMapper = classBuilder.getPlayUiModelMapper()

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

    @Test
    fun `given cart should be shown, when update badge cart count, then cart info should be updated`() {
        val cartInfo = cartInfoBuilder.buildCompleteData(
                shouldShow = true,
                count = 5
        )

        val channelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfo
        )

        val mockCartCount = 7

        givenPlayViewModelRobot {
            setMockCartCountResponse(mockCartCount)
        } andWhen {
            createPage(channelData)
        } thenVerify {
            cartInfoResult
                    .isEqualTo(cartInfo)
        } andThen {
            updateCartCountFromNetwork()
        } thenVerify {
                cartInfoResult
                        .isNotEqualTo(cartInfo)
                        .isEqualTo(cartInfo.copy(count = mockCartCount))
        }
    }
}