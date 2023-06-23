package com.tokopedia.play.broadcaster.viewmodel.banner

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.PlayBroadcastPreparationBannerModel
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on June 23, 2023
 */
class PlayBroadcasterBannerViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val uiModelBuilder = UiModelBuilder()
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockPlayShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig = mockk(relaxed = true)
    private val mockPlayBroadcastMapper: PlayBroadcastMapper = mockk(relaxed = true)

    private val mockAddedTag = GetAddedChannelTagsResponse()
    private val mockEmptyProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList(sectionSize = 0)

    private val mockConfigShortVideoAllowed = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123",
        shortVideoAllowed = true,
    )
    private val mockConfigShortVideoNotAllowed = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123",
        shortVideoAllowed = false,
    )
    private val mockConfigHasContent = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123",
        hasContent = true,
    )
    private val mockConfigHasNoContent = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123",
        hasContent = false,
    )
    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            channelId = "123"
        )
    )

    @Before
    fun setUp() {
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigShortVideoAllowed
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockEmptyProductTagSectionList
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
        coEvery { mockRepo.updateSchedule(any(), any()) } returns BroadcastScheduleUiModel.Scheduled(
            mockk(), "")

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockPlayBroadcastMapper.mapCover(any(), any()) } returns uiModelBuilder.buildPlayCoverUiModel()

        coEvery { mockPlayShortsEntryPointRemoteConfig.isShowEntryPoint() } returns true
    }

    @Test
    fun `shortBanner_remoteConfigTrue_shortVideoAllowedTrue`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playShortsEntryPointRemoteConfig = mockPlayShortsEntryPointRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.bannerPreparation.contains(
                PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_SHORTS)
            ).assertTrue()
        }
    }

    @Test
    fun `shortBanner_remoteConfigTrue_shortVideoAllowedFalse`() {


        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigShortVideoNotAllowed

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playShortsEntryPointRemoteConfig = mockPlayShortsEntryPointRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.bannerPreparation.contains(
                PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_SHORTS)
            ).assertFalse()
        }
    }

    @Test
    fun `shortBanner_remoteConfigFalse_shortVideoAllowedTrue`() {


        coEvery { mockPlayShortsEntryPointRemoteConfig.isShowEntryPoint() } returns false

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playShortsEntryPointRemoteConfig = mockPlayShortsEntryPointRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.bannerPreparation.contains(
                PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_SHORTS)
            ).assertFalse()
        }
    }

    @Test
    fun `performanceDashboardBanner_userIsShop_hasContent`() {

        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigHasContent

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playShortsEntryPointRemoteConfig = mockPlayShortsEntryPointRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.bannerPreparation.contains(
                PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_DASHBOARD)
            ).assertTrue()
        }
    }

    @Test
    fun `performanceDashboardBanner_userIsShop_hasNoContent`() {

        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigHasNoContent

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playShortsEntryPointRemoteConfig = mockPlayShortsEntryPointRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.bannerPreparation.contains(
                PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_DASHBOARD)
            ).assertFalse()
        }
    }

    @Test
    fun `performanceDashboardBanner_userIsUgc_hasContent`() {

        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfigHasContent
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel(onlyBuyer = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playShortsEntryPointRemoteConfig = mockPlayShortsEntryPointRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.bannerPreparation.contains(
                PlayBroadcastPreparationBannerModel(PlayBroadcastPreparationBannerModel.TYPE_DASHBOARD)
            ).assertFalse()
        }
    }
}
