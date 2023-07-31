package com.tokopedia.play.broadcaster.viewmodel.beautification

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
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationConfigUiModel
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail

/**
 * Created By : Jonathan Darwin on April 10, 2023
 */
class PlayBroadcastBeautificationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val uiModelBuilder = UiModelBuilder()
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val beautificationConfigModelBuilder = BeautificationConfigUiModelBuilder()

    private val mockAddedTag = GetAddedChannelTagsResponse()
    private val mockEmptyProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList(sectionSize = 0)

    private val mockPresetActivePosition = 1
    private val mockBeautificationConfigAvailable = beautificationConfigModelBuilder.buildBeautificationConfig(
        presetActivePosition = mockPresetActivePosition,
    )
    private val mockBeautificationConfigNotDownloaded = beautificationConfigModelBuilder.buildBeautificationConfig(
        presetActivePosition = mockPresetActivePosition,
        assetStatus = BeautificationAssetStatus.NotDownloaded
    )
    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123",
        beautificationConfig = mockBeautificationConfigAvailable
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

    @Before
    fun setUp() {
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockEmptyProductTagSectionList
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
        coEvery { mockRepo.updateSchedule(any(), any()) } returns BroadcastScheduleUiModel.Scheduled(
            mockk(), "")

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
    fun `playBroadcaster_beautification_getEmptyBeautificationConfig`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig.copy(beautificationConfig = BeautificationConfigUiModel.Empty)

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

            state.beautificationConfig.isUnknown.assertTrue()
            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } == null)
        }
    }

    @Test
    fun `playBroadcaster_beautification_getAllowedBeautificationConfig`() {
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

            state.beautificationConfig.isUnknown.assertFalse()

            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } != null)

            val allowRetryDownloadAsset = robot.getViewModelPrivateField<MutableStateFlow<Boolean>>("_allowRetryDownloadAsset")
            allowRetryDownloadAsset.value.assertTrue()
        }
    }

    @Test
    fun `playBroadcaster_beautification_getAllowedBeautificationConfigButGotDisabledFromRemoteConfig`() {
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

            state.beautificationConfig.isUnknown.assertTrue()
            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } == null)
        }
    }

    @Test
    fun `playBroadcaster_beautification_setBeautificationConfig_downloadLicenseError`() {
        coEvery { mockRepo.downloadLicense(any()) } throws Exception("Network Error")

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

            state.beautificationConfig.isUnknown.assertTrue()

            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } == null)

            val allowRetryDownloadAsset = robot.getViewModelPrivateField<MutableStateFlow<Boolean>>("_allowRetryDownloadAsset")
            allowRetryDownloadAsset.value.assertFalse()
        }
    }

    @Test
    fun `playBroadcaster_beautification_setBeautificationConfig_downloadModelError`() {
        coEvery { mockRepo.downloadModel(any()) } throws Exception("Network Error")

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

            state.beautificationConfig.isUnknown.assertTrue()

            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } == null)

            val allowRetryDownloadAsset = robot.getViewModelPrivateField<MutableStateFlow<Boolean>>("_allowRetryDownloadAsset")
            allowRetryDownloadAsset.value.assertFalse()
        }
    }

    @Test
    fun `playBroadcaster_beautification_setBeautificationConfig_downloadCustomFaceError`() {
        coEvery { mockRepo.downloadCustomFace(any()) } throws Exception("Network Error")

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

            state.beautificationConfig.isUnknown.assertTrue()

            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } == null)

            val allowRetryDownloadAsset = robot.getViewModelPrivateField<MutableStateFlow<Boolean>>("_allowRetryDownloadAsset")
            allowRetryDownloadAsset.value.assertFalse()
        }
    }

    @Test
    fun `playBroadcaster_beautification_setBeautificationConfig_setupOnDemandAsset_notDownloaded_success`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig.copy(beautificationConfig = mockBeautificationConfigNotDownloaded)

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

            state.beautificationConfig.isUnknown.assertFalse()

            val presetActive = state.beautificationConfig.selectedPreset ?: fail(Exception("Selected preset shouldn't be null"))
            presetActive.assetStatus.assertEqualTo(BeautificationAssetStatus.Available)
        }
    }

    @Test
    fun `playBroadcaster_beautification_setBeautificationConfig_setupOnDemandAsset_notDownloaded_error`() {
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig.copy(beautificationConfig = mockBeautificationConfigNotDownloaded)
        coEvery { mockRepo.downloadPresetAsset(any(), any()) } returns false

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
            val (state, events) = it.recordStateAndEvents {
                getAccountConfiguration()
            }

            state.beautificationConfig.isUnknown.assertFalse()

            assert(state.beautificationConfig.selectedPreset == null)

            events.last().assertEvent(PlayBroadcastEvent.BeautificationDownloadAssetFailed(Exception("ignore this"), mockBeautificationConfigNotDownloaded.presets[0]))
        }
    }

    @Test
    fun `playBroadcaster_beautification_removeBeautificationMenu`() {
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
            val (state, events) = it.recordStateAndEvents {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.RemoveBeautificationMenu)
            }

            assert(state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } == null)
            state.beautificationConfig.assertEqualTo(BeautificationConfigUiModel.Empty)
            events.last().assertEvent(PlayBroadcastEvent.InitializeBroadcaster(BroadcastingConfigUiModel()))
        }
    }

    @Test
    fun `playBroadcaster_beautification_resetBeautification`() {
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
            val preState = it.recordState {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.SelectFaceFilterOption(mockBeautificationConfigAvailable.faceFilters[2]))
                it.getViewModel().submitAction(PlayBroadcastAction.ChangeFaceFilterValue(100))

                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigAvailable.presets[2]))
                it.getViewModel().submitAction(PlayBroadcastAction.ChangePresetValue(100))
            }

            preState.beautificationConfig.faceFilters[2].isSelected.assertTrue()
            preState.beautificationConfig.faceFilters[2].value.assertEqualTo(1.0)

            preState.beautificationConfig.presets[2].isSelected.assertTrue()
            preState.beautificationConfig.presets[2].value.assertEqualTo(1.0)

            val postState = it.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.ResetBeautification)
            }

            postState.beautificationConfig.faceFilters[2].value.assertEqualTo(postState.beautificationConfig.faceFilters[2].defaultValue)
            postState.beautificationConfig.faceFilters[2].active.assertTrue()

            postState.beautificationConfig.presets[2].isSelected.assertFalse()
            postState.beautificationConfig.presets[2].value.assertEqualTo(postState.beautificationConfig.presets[2].defaultValue)

            postState.beautificationConfig.presets[mockPresetActivePosition].isSelected.assertTrue()
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectFaceFilter`() {

        val mockSelectedFaceFilterPosition = 2

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
            val prevState = it.recordState {
                getAccountConfiguration()
            }

            val state = it.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.SelectFaceFilterOption(mockBeautificationConfigAvailable.faceFilters[mockSelectedFaceFilterPosition]))
            }

            state.beautificationConfig.faceFilters.forEachIndexed { idx, e ->
                if (idx == mockSelectedFaceFilterPosition) {
                    e.active.assertTrue()
                    e.isSelected.assertTrue()
                }
                else {
                    e.active.assertEqualTo(prevState.beautificationConfig.faceFilters[idx].active)
                    e.isSelected.assertFalse()
                }
            }
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectFaceFilter_removeEffect`() {

        val mockSelectedFaceFilterPosition = 0

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
                it.getViewModel().submitAction(PlayBroadcastAction.SelectFaceFilterOption(mockBeautificationConfigAvailable.faceFilters[mockSelectedFaceFilterPosition]))
            }

            state.beautificationConfig.faceFilters.forEachIndexed { idx, e ->
                if (idx == mockSelectedFaceFilterPosition) {
                    e.active.assertTrue()
                    e.isSelected.assertTrue()
                }
                else {
                    e.active.assertFalse()
                    e.isSelected.assertFalse()
                    e.value.assertEqualTo(e.defaultValue)
                }
            }
        }
    }

    @Test
    fun `playBroadcaster_beautification_changeFaceFilterValue`() {

        val mockSelectedFaceFilterPosition = 2

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
                it.getViewModel().submitAction(PlayBroadcastAction.SelectFaceFilterOption(mockBeautificationConfigAvailable.faceFilters[mockSelectedFaceFilterPosition]))
                it.getViewModel().submitAction(PlayBroadcastAction.ChangeFaceFilterValue(90))
            }

            state.beautificationConfig.faceFilters[mockSelectedFaceFilterPosition].value.assertEqualTo(0.9)
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectPreset_removeEffect`() {

        val mockPresetNoneOptionPosition = 0

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
                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigAvailable.presets[mockPresetNoneOptionPosition]))
            }

            state.beautificationConfig.presets.forEachIndexed { idx, e ->
                if (idx == mockPresetNoneOptionPosition) {
                    e.isSelected.assertTrue()
                } else {
                    e.isSelected.assertFalse()
                }
            }
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectPreset_available`() {

        val mockPresetOptionPosition = 1

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
                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigAvailable.presets[mockPresetOptionPosition]))
            }

            state.beautificationConfig.presets.forEachIndexed { idx, e ->
                if (idx == mockPresetOptionPosition) {
                    e.isSelected.assertTrue()
                } else {
                    e.isSelected.assertFalse()
                }
            }
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectPreset_notDownloaded_success`() {

        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig.copy(beautificationConfig = mockBeautificationConfigNotDownloaded)

        val mockPresetOptionPosition = 2

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
                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigNotDownloaded.presets[mockPresetOptionPosition]))
            }

            state.beautificationConfig.presets.forEachIndexed { idx, e ->
                /**
                 * not auto select preset after download,
                 *  so the selected preset is still the old one
                 * */
                if (idx == mockPresetActivePosition) {
                    e.isSelected.assertTrue()
                } else {
                    e.isSelected.assertFalse()
                }

                if (
                    idx == mockPresetOptionPosition ||
                    idx == mockPresetActivePosition ||
                    idx == 0
                )
                    e.assetStatus.assertEqualTo(BeautificationAssetStatus.Available)
                else
                    e.assetStatus.assertEqualTo(BeautificationAssetStatus.NotDownloaded)
            }
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectPreset_notDownloaded_failed`() {

        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig.copy(beautificationConfig = mockBeautificationConfigNotDownloaded)
        coEvery { mockRepo.downloadPresetAsset(any(), any()) } returns false

        val mockPresetOptionPosition = 2

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
            val (state, events) = it.recordStateAndEvents {
                getAccountConfiguration()
                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigNotDownloaded.presets[mockPresetOptionPosition]))
            }

            state.beautificationConfig.presets.forEachIndexed { idx, e ->
                e.isSelected.assertFalse()

                if (idx == 0)
                    e.assetStatus.assertEqualTo(BeautificationAssetStatus.Available)
                else
                    e.assetStatus.assertEqualTo(BeautificationAssetStatus.NotDownloaded)
            }

            events.last().assertEvent(PlayBroadcastEvent.BeautificationDownloadAssetFailed(Exception("ignore this"), mockBeautificationConfigAvailable.presets[mockPresetOptionPosition]))
        }
    }

    @Test
    fun `playBroadcaster_beautification_changePresetValue`() {

        val mockSelectedPresetPosition = 2

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
                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigAvailable.presets[mockSelectedPresetPosition]))
                it.getViewModel().submitAction(PlayBroadcastAction.ChangePresetValue(90))
            }

            state.beautificationConfig.presets[mockSelectedPresetPosition].value.assertEqualTo(0.9)
        }
    }

    @Test
    fun `playBroadcaster_beautification_selectNoneForAllBeautification`() {

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

                it.getViewModel().submitAction(PlayBroadcastAction.SelectFaceFilterOption(mockBeautificationConfigAvailable.faceFilters[0]))
                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigAvailable.presets[0]))
            }

            val faceFilterMenu = state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } ?: fail(Exception("Face Filter menu should exists"))

            faceFilterMenu.isChecked.assertFalse()
        }
    }

    @Test
    fun `playBroadcaster_beautification_updateAllBeautificationValueToZero`() {

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

                mockBeautificationConfigAvailable.faceFilters.forEachIndexed { idx, e ->
                    if (idx != 0) {
                        it.getViewModel().submitAction(PlayBroadcastAction.SelectFaceFilterOption(mockBeautificationConfigAvailable.faceFilters[idx]))
                        it.getViewModel().submitAction(PlayBroadcastAction.ChangeFaceFilterValue(0))
                    }
                }

                it.getViewModel().submitAction(PlayBroadcastAction.SelectPresetOption(mockBeautificationConfigAvailable.presets[1]))
                it.getViewModel().submitAction(PlayBroadcastAction.ChangePresetValue(0))
            }

            val faceFilterMenu = state.menuList.firstOrNull { item -> item.menu == DynamicPreparationMenu.Menu.FaceFilter } ?: fail(Exception("Face Filter menu should exists"))
            faceFilterMenu.isChecked.assertFalse()
        }
    }
}
