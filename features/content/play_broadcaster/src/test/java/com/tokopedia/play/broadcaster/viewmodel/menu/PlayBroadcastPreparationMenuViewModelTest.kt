package com.tokopedia.play.broadcaster.viewmodel.menu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.beautification.BeautificationConfigUiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail

/**
 * Created By : Jonathan Darwin on February 21, 2023
 */
class PlayBroadcastPreparationMenuViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val uiModelBuilder = UiModelBuilder()
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val beautificationConfigUiModelBuilder = BeautificationConfigUiModelBuilder()

    private val mockAddedTag = GetAddedChannelTagsResponse()
    private val mockEmptyProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList(sectionSize = 0)
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()
    private val mockBeautificationConfig = beautificationConfigUiModelBuilder.buildBeautificationConfig()
    private val mockTitle = "Title"
    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123",
        beautificationConfig = mockBeautificationConfig,
    )
    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            channelId = "123"
        )
    )

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockPlayBroadcastMapper: PlayBroadcastMapper = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockRemoteConfig: RemoteConfig = mockk(relaxed = true)

    private val menuList = listOf(
        DynamicPreparationMenu.createTitle(true),
        DynamicPreparationMenu.createCover(false),
        DynamicPreparationMenu.createProduct(false),
        DynamicPreparationMenu.createSchedule(false),
        DynamicPreparationMenu.createFaceFilter(false).copy(isChecked = true),
    )


    @Before
    fun setUp() {
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockEmptyProductTagSectionList
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
        coEvery { mockRepo.updateSchedule(any(), any()) } returns BroadcastScheduleUiModel.Scheduled(mockk(), "")

        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockPlayBroadcastMapper.mapCover(any(), any()) } returns uiModelBuilder.buildPlayCoverUiModel()

        coEvery { mockRepo.downloadLicense(any()) } returns true
        coEvery { mockRepo.downloadModel(any()) } returns true
        coEvery { mockRepo.downloadCustomFace(any()) } returns true
        coEvery { mockRepo.downloadPresetAsset(any(), any()) } returns true

        coEvery { mockRemoteConfig.getBoolean(any(), any()) } returns true
    }

    @Test
    fun `playBroadcaster_menu_initialState`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
            }

            state.menuList.assertEqualTo(menuList)
        }
    }

    @Test
    fun `playBroadcaster_menu_mandatoryFieldFilled`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                uploadTitle(mockTitle)
            }

            state.menuList.forEach { menu ->
                if(menu.menu == DynamicPreparationMenu.Menu.Title && !menu.isChecked) {
                    fail(Exception("Title should be checked!"))
                }
                if(menu.menu != DynamicPreparationMenu.Menu.Title && !menu.isEnabled) {
                    fail(Exception("${menu.menu} should be enabled!"))
                }
            }
        }
    }

    @Test
    fun `playBroadcast_menu_coverFilled`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                uploadTitle(mockTitle)
                uploadCover()
            }

            state.menuList.forEach { menu ->
                if(menu.menu == DynamicPreparationMenu.Menu.Title && !menu.isChecked) {
                    fail(Exception("Title should be checked!"))
                }
                if(menu.menu == DynamicPreparationMenu.Menu.Cover && !menu.isChecked) {
                    fail(Exception("Cover should be checked!"))
                }
                if(menu.menu != DynamicPreparationMenu.Menu.Title && !menu.isEnabled) {
                    fail(Exception("${menu.menu} should be enabled!"))
                }
            }
        }
    }

    @Test
    fun `playBroadcast_menu_productFilled`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                uploadTitle(mockTitle)
                setProduct(mockProductTagSectionList)
            }

            state.menuList.forEach { menu ->
                if(menu.menu == DynamicPreparationMenu.Menu.Title && !menu.isChecked) {
                    fail(Exception("Title should be checked!"))
                }
                if(menu.menu == DynamicPreparationMenu.Menu.Product && !menu.isChecked) {
                    fail(Exception("Product should be checked!"))
                }
                if(menu.menu != DynamicPreparationMenu.Menu.Title && !menu.isEnabled) {
                    fail(Exception("${menu.menu} should be enabled!"))
                }
            }
        }
    }

    @Test
    fun `playBroadcast_menu_scheduleFilled`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
                uploadTitle(mockTitle)
                setSchedule()
            }

            state.menuList.forEach { menu ->
                if(menu.menu == DynamicPreparationMenu.Menu.Title && !menu.isChecked) {
                    fail(Exception("Title should be checked!"))
                }
                if(menu.menu == DynamicPreparationMenu.Menu.Schedule && !menu.isChecked) {
                    fail(Exception("Schedule should be checked!"))
                }
                if(menu.menu != DynamicPreparationMenu.Menu.Title && !menu.isEnabled) {
                    fail(Exception("${menu.menu} should be enabled!"))
                }
            }
        }
    }

    @Test
    fun `playBroadcast_menu_faceFilterDisabledFromRemoteConfig`() {

        coEvery { mockRemoteConfig.getBoolean(any(), any()) } returns false

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
            }

            val faceFilter = state.menuList.firstOrNull { menu -> menu.menu == DynamicPreparationMenu.Menu.FaceFilter }

            assert(faceFilter == null)
        }
    }

    @Test
    fun `playBroadcast_menu_faceFilterFilled`() {

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            playBroadcastMapper = mockPlayBroadcastMapper,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val state = it.recordState {
                getAccountConfiguration()
            }

            val faceFilter = state.menuList.firstOrNull { menu -> menu.menu == DynamicPreparationMenu.Menu.FaceFilter } ?: fail("Face Filter shouldn't be null")

            faceFilter.isChecked.assertTrue()
        }
    }
}
