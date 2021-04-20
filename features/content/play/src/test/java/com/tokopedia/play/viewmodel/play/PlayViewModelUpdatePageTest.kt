package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.andThen
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfoUiModel
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
    fun `given partner is admin, when page is focused, then partner follow info should not be updated`() {
        val partnerInfo = partnerInfoBuilder.buildIncompleteData(
                basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(type = PartnerType.Tokopedia)
        )

        val channelData = channelDataBuilder.buildChannelData(
                partnerInfo = partnerInfo
        )
        val mockPartnerInfoResponse = responseBuilder.buildPartnerInfoResponse()

        val expectedModel = partnerInfo

        givenPlayViewModelRobot {
            setMockPartnerInfoResponse(mockPartnerInfoResponse)
        } andWhen {
            createPage(channelData)
        } andThen {
            focusPage(channelData)
        } thenVerify {
            partnerInfoResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given partner is shop, when page is focused, then partner follow info should be updated`() {
        val partnerInfo = partnerInfoBuilder.buildIncompleteData(
                basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(type = PartnerType.Shop)
        )

        val channelData = channelDataBuilder.buildChannelData(
                partnerInfo = partnerInfo
        )

        val mockPartnerInfoResponse = responseBuilder.buildPartnerInfoResponse()

        val expectedModel = PlayPartnerInfoUiModel.Complete(
                basicInfo = partnerInfo.basicInfo,
                followInfo = uiModelMapper.mapPartnerInfo(mockPartnerInfoResponse)
        )

        givenPlayViewModelRobot {
            setMockPartnerInfoResponse(mockPartnerInfoResponse)
        } andWhen {
            createPage(channelData)
        } andThen {
            focusPage(channelData)
        } thenVerify {
            partnerInfoResult
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