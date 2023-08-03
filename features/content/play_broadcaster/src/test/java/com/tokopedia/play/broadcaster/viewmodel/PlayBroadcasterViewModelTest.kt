package com.tokopedia.play.broadcaster.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.bottomsheet.WarningInfoBottomSheet
import com.tokopedia.content.common.ui.model.AccountStateInfoType
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.domain.model.GetAddedChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetPage
import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetType
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.assertEmpty
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertEvent
import com.tokopedia.play.broadcaster.util.assertFailed
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertNotEqualTo
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

/**
 * Created By : Jonathan Darwin on October 18, 2021
 */
class PlayBroadcasterViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetAddedTagUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true)
    private val mockUserSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val mockHydraSharedPreferences: HydraSharedPreferences = mockk(relaxed = true)
    private val mockDataStore: PlayBroadcastDataStore = mockk(relaxed = true)
    private val mockHydraConfigStore: HydraConfigStore = mockk(relaxed = true)
    private val mockRemoteConfig: RemoteConfig = mockk(relaxed = true)

    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()
    private val uiModelBuilder = UiModelBuilder()

    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            channelId = "123",
            coverUrl = "https://tokopedia.com"
        )
    )
    private val mockAddedTag = GetAddedChannelTagsResponse()
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    @Before
    fun setUp() {
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetAddedTagUseCase.executeOnBackground() } returns mockAddedTag
        coEvery { mockRepo.getProductTagSummarySection(any()) } returns mockProductTagSectionList
        coEvery { mockRepo.getBroadcastingConfig(any(), any()) } returns uiModelBuilder.buildBroadcastingConfigUiModel()
    }

    @Test
    fun `given config info, when get live countdown duration, it should return duration stated on config info`() {
        val countDown = 5
        val configMock = uiModelBuilder.buildConfigurationUiModel(countDown = countDown.toLong())

        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            it.getAccountConfiguration()
            it.getViewModel().getBeforeLiveCountDownDuration().assertEqualTo(countDown)
        }
    }

    @Test
    fun `given no config info, when get live countdown duration, it should return default duration`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher
        )

        robot.use {
            val defaultCountDown = it.getViewModelPrivateField<Int>("DEFAULT_BEFORE_LIVE_COUNT_DOWN")
            it.getViewModel().getBeforeLiveCountDownDuration().assertEqualTo(defaultCountDown)
        }
    }

    @Test
    fun `given seller allowed to stream, and channelStatus Unknown, then it should trigger createChannel()`() {
        val mockConfig = uiModelBuilder.buildConfigurationUiModel(
            streamAllowed = true,
            channelStatus = ChannelStatus.Unknown
        )
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        val mock = spyk(robot.getViewModel(), recordPrivateCalls = true)

        robot.use {
            mock.submitAction(PlayBroadcastAction.GetConfiguration(TYPE_SHOP))

            verify { mock invokeNoArgs "createChannel" }
        }
    }

    @Test
    fun `when user submit set product action, it should emit new product section list state`() {
        val mockProductTagSection = productSetupUiModelBuilder.buildProductTagSectionList()
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSection))
            }

            state.selectedProduct.assertEqualTo(mockProductTagSection)
        }
    }

    @Test
    fun `when user submit set product action, and product changed because deleted should show toaster deleted`() {
        val mockProductTagSection = productSetupUiModelBuilder.buildProductTagSectionList()
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val sharedPreferences: HydraSharedPreferences = mockk(relaxed = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { sharedPreferences.savedSelectedAutoGeneratedCover(2, "123", "prep") }
        coEvery { sharedPreferences.getSavedSelectedAutoGeneratedCover("123", "prep") } returns 2

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            sharedPref = sharedPreferences,
        )

        robot.use {
            val event = robot.recordEvent {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSection))
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(emptyList()))
            }

            event.last().assertEvent(PlayBroadcastEvent.AutoGeneratedCoverToasterDelete)
        }
    }

    @Test
    fun `when user submit set product action, and product changed should show toaster updated`() {
        val mockProductTagSection1 = productSetupUiModelBuilder.buildProductTagSectionList()
        val mockProductTagSection2 = productSetupUiModelBuilder.buildProductTagSectionList(sectionSize = 3, productSizePerSection = 2)
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val sharedPreferences: HydraSharedPreferences = mockk(relaxed = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { sharedPreferences.savedSelectedAutoGeneratedCover(2, "123", "prep") }
        coEvery { sharedPreferences.getSavedSelectedAutoGeneratedCover("123", "prep") } returns 2

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            sharedPref = sharedPreferences,
        )

        robot.use {
            val event = robot.recordEvent {
                getAccountConfiguration()
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSection1))
                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSection2))
            }

            event.last().assertEvent(PlayBroadcastEvent.AutoGeneratedCoverToasterUpdate)
        }
    }

    @Test
    fun `when user success get save button config and emit false and remote config false, then showSaveButton should be false`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(showSaveButton = false)
        val remoteConfigButtonMock = false

        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery {
            mockRemoteConfig.getBoolean(
                "android_show_live_to_vod_button_play_broadcaster",
                true
            )
        } returns remoteConfigButtonMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            remoteConfig = mockRemoteConfig,
        )

        robot.use{
            val state = it.recordState {
                getAccountConfiguration()
            }
            state.channel.showPostVideoButton.assertFalse()
        }
    }

    @Test
    fun `when user success get save button config and emit false and remote config true, then showSaveButton should be false`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(showSaveButton = false)
        val remoteConfigButtonMock = true

        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery {
            mockRemoteConfig.getBoolean(
                "android_show_live_to_vod_button_play_broadcaster",
                true
            )
        } returns remoteConfigButtonMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            remoteConfig = mockRemoteConfig,
        )

        robot.use{
            val state = it.recordState {
                getAccountConfiguration()
            }
            state.channel.showPostVideoButton.assertFalse()
        }
    }

    @Test
    fun `when user success get save button config and emit true and remote config false, then showSaveButton should be true`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(showSaveButton = true)
        val remoteConfigButtonMock = false

        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery {
            mockRemoteConfig.getBoolean(
                "android_show_live_to_vod_button_play_broadcaster",
                true
            )
        } returns remoteConfigButtonMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            remoteConfig = mockRemoteConfig,
        )

        robot.use{
            val state = it.recordState {
                getAccountConfiguration()
            }
            state.channel.showPostVideoButton.assertFalse()
        }
    }

    @Test
    fun `when user success get save button config and emit true and remote config true, then showSaveButton should be true`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(showSaveButton = true)
        val remoteConfigButtonMock = true

        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery {
            mockRemoteConfig.getBoolean(
                "android_show_live_to_vod_button_play_broadcaster",
                true
            )
        } returns remoteConfigButtonMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            remoteConfig = mockRemoteConfig,
        )

        robot.use{
            val state = it.recordState {
                getAccountConfiguration()
            }
            state.channel.showPostVideoButton.assertTrue()
        }
    }

    //region usersession
    /**
     * User Session
     */
    @Test
    fun `given user session logged in true, then it should return true`() {
        every { mockUserSessionInterface.isLoggedIn } returns true

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSessionInterface,
        )

        robot.use {
            it.getViewModel().isUserLoggedIn.assertTrue()
        }
    }

    @Test
    fun `given user session shop avatar is empty, then it should return empty`() {
        every { mockUserSessionInterface.shopAvatar } returns ""

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSessionInterface,
        )

        robot.use {
            it.getViewModel().getAuthorImage().assertEmpty()
        }
    }

    @Test
    fun `given user session shop name is empty, then it should return empty`() {
        every { mockUserSessionInterface.shopName } returns ""

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSessionInterface,
        )

        robot.use {
            it.getViewModel().authorName.assertEmpty()
        }
    }
    //endregion

    //region timer
    /**
     * Timer
     */
    @Test
    fun `when trigger startTimer(), then it will trigger broadcasterTimer start`() {
        val mockTimer: PlayBroadcastTimer = mockk(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            broadcastTimer = mockTimer,
        )

        robot.use {
            it.getViewModel().startTimer()

            verify { mockTimer.start() }
        }
    }

    @Test
    fun `when trigger stopTimer(), then it will trigger broadcasterTimer stop`() {
        val mockTimer: PlayBroadcastTimer = mockk(relaxed = true)

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            broadcastTimer = mockTimer,
        )

        robot.use {
            it.getViewModel().stopTimer()

            verify { mockTimer.stop() }
        }
    }
    //endregion

    @Test
    fun `when user is empty`() {
        coEvery { mockRepo.getAccountList() } returns emptyList()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            it.recordState { it.getAccountConfiguration() }
            val configInfo = it.getViewModel().observableConfigInfo.getOrAwaitValue()
            configInfo.assertFailed()
        }
    }

    @Test
    fun `when user only have shop and eligible then selected account is shop`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(onlyShop = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when user only have shop and not eligible then selected account is shop with info`() {
        val mockTnc = listOf(TermsAndConditionUiModel("apa aja"))
        val configMock = uiModelBuilder.buildConfigurationUiModel(tnc = mockTnc)
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false, onlyShop = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NotWhitelisted)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().isAllowChangeAccount.assertFalse()
            it.getViewModel().tncList.assertEqualTo(mockTnc)
        }
    }

    @Test
    fun `when user switch account`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)

            val state1 = robot.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.SwitchAccount())
            }

            state1.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when user UGC success onboarding and success`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.SuccessOnBoardingUGC)
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when user UGC success onboarding and fails`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val mockException = Exception("any exception")

        coEvery { mockRepo.getAccountList() } throws mockException
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            it.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.SuccessOnBoardingUGC)
                val configInfo = it.getViewModel().observableConfigInfo.getOrAwaitValue()
                configInfo.assertFailed()
            }
        }
    }

    @Test
    fun `when user set cover uploaded source`() {
        coEvery { mockHydraSharedPreferences.setUploadedCoverSource(1, "123", "prep") }
        coEvery { mockHydraSharedPreferences.getSavedSelectedAutoGeneratedCover("123", "prep") } returns 1

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            it.recordState {
                it.getViewModel().submitAction(PlayBroadcastAction.SetCoverUploadedSource(1))
                mockHydraSharedPreferences.getSavedSelectedAutoGeneratedCover("123", "prep").assertEqualTo(1)
            }
        }
    }

    @Test
    fun `when user only have buyer and eligible then selected account is buyer`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(onlyBuyer = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            it.getViewModel().isAllowChangeAccount.assertFalse()
            it.getViewModel().tncList.assertEmpty()
            it.getViewModel().warningInfoType.assertEqualTo(WarningInfoBottomSheet.WarningType.UNKNOWN)
        }
    }

    @Test
    fun `when user only have buyer and not eligible then selected account is buyer with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncBuyer = false, onlyBuyer = true)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NotAcceptTNC)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_USER)
            it.getViewModel().isAllowChangeAccount.assertFalse()
            it.getViewModel().warningInfoType.assertEqualTo(WarningInfoBottomSheet.WarningType.UNKNOWN)
        }
    }

    @Test
    fun `when user have already switch account before then selected account is from preferences`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockHydraSharedPreferences.getLastSelectedAccountType() } returns TYPE_USER
        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockHydraSharedPreferences,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            it.getViewModel().isAllowChangeAccount.assertTrue()
        }
    }

    @Test
    fun `when selected account is from preferences but selected account is not eligible then switch to other`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false)

        coEvery { mockHydraSharedPreferences.getLastSelectedAccountType() } returns TYPE_SHOP
        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            sharedPref = mockHydraSharedPreferences,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when shop account eligible then selected account is shop`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when shop account not eligible but buyer account is eligible then selected account is buyer`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when shop account not eligible and buyer account not eligible then selected account is shop with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false, usernameBuyer = false, tncBuyer = false)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NoUsername)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when shop and buyer account eligible but live stream then selected account is shop with info`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(channelStatus = ChannelStatus.Live)
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration()
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.Live)
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().warningInfoType.assertEqualTo(WarningInfoBottomSheet.WarningType.LIVE)
        }
    }

    @Test
    fun `when entry point from user profile then selected account should be non-seller`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration(TYPE_USER)
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
        }
    }

    @Test
    fun `when entry point from user profile but non-seller not eligible then selected account should be seller`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncBuyer = false)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration(TYPE_USER)
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when entry point from whatever that require open as seller then selected account should be seller`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                it.getAccountConfiguration(TYPE_SHOP)
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
        }
    }

    @Test
    fun `when entry point from whatever that require open as seller but seller not eligible then selected account should be non-seller`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false)
        val mockCover = PlayCoverUiModel(croppedCover = CoverSetupState.Blank, state = SetupDataState.Draft)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
        }
    }

    @Test
    fun `when user account not eligible, then no disable live to vod config should show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel(usernameBuyer = false, tncShop = false)
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = TickerBottomSheetPage.LIVE_PREPARATION,
            type = TickerBottomSheetType.BOTTOM_SHEET,
        )
        val isFirstTime = true

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodBottomSheetPref(
            page = TickerBottomSheetPage.LIVE_PREPARATION.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_PREPARATION
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.UNKNOWN)
            state.tickerBottomSheetConfig.type.assertEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and first time show disable live to vod in preparation live, then it should show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = TickerBottomSheetPage.LIVE_PREPARATION,
            type = TickerBottomSheetType.BOTTOM_SHEET,
        )
        val isFirstTime = true

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodBottomSheetPref(
            page = TickerBottomSheetPage.LIVE_PREPARATION.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_PREPARATION
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.LIVE_PREPARATION)
            state.tickerBottomSheetConfig.type.assertNotEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and first not time show disable live to vod in preparation live, then it should not show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = TickerBottomSheetPage.LIVE_PREPARATION,
            type = TickerBottomSheetType.BOTTOM_SHEET,
        )
        val isFirstTime = false

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodBottomSheetPref(
            page = TickerBottomSheetPage.LIVE_PREPARATION.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_PREPARATION
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.UNKNOWN)
            state.tickerBottomSheetConfig.type.assertEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and fail get disable live to vod in preparation live, then it should not show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val isFirstTime = false

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } throws Throwable("Fail?")
        coEvery { mockHydraSharedPreferences.getLiveToVodBottomSheetPref(
            page = TickerBottomSheetPage.LIVE_PREPARATION.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_PREPARATION
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.UNKNOWN)
            state.tickerBottomSheetConfig.type.assertEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and first time show disable live to vod in report page, then it should show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = TickerBottomSheetPage.LIVE_REPORT,
            type = TickerBottomSheetType.TICKER,
        )
        val isFirstTime = true

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodTickerPref(
            page = TickerBottomSheetPage.LIVE_REPORT.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_REPORT
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.LIVE_REPORT)
            state.tickerBottomSheetConfig.type.assertNotEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and first not time show disable live to vod in report page, then it should not show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = TickerBottomSheetPage.LIVE_REPORT,
            type = TickerBottomSheetType.BOTTOM_SHEET,
        )
        val isFirstTime = false

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodTickerPref(
            page = TickerBottomSheetPage.LIVE_REPORT.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_REPORT
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.UNKNOWN)
            state.tickerBottomSheetConfig.type.assertEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and fail get disable live to vod in report page, then it should not show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val isFirstTime = false

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } throws Throwable("Fail?")
        coEvery { mockHydraSharedPreferences.getLiveToVodTickerPref(
            page = TickerBottomSheetPage.LIVE_REPORT.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_REPORT
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.UNKNOWN)
            state.tickerBottomSheetConfig.type.assertEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user account eligible and disable live to vod in unknown page and type, then it should not show`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse()
        val isFirstTime = false

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodTickerPref(
            page = TickerBottomSheetPage.LIVE_REPORT.value,
            authorId = accountMock.find { it.isShop }?.id.orEmpty()
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration(TYPE_SHOP)
                it.getViewModel().submitAction(PlayBroadcastAction.GetTickerBottomSheetConfig(
                    TickerBottomSheetPage.LIVE_REPORT
                ))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
            state.tickerBottomSheetConfig.page.assertEqualTo(TickerBottomSheetPage.UNKNOWN)
            state.tickerBottomSheetConfig.type.assertEqualTo(TickerBottomSheetType.UNKNOWN)
        }
    }

    @Test
    fun `when user set live to vod pref in preparation live`() {
        val type = TickerBottomSheetType.BOTTOM_SHEET
        val page = TickerBottomSheetPage.LIVE_PREPARATION
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = page,
            type = type,
        )
        val isFirstTime = false
        val authorId = accountMock.find { it.isShop }?.id.orEmpty()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodBottomSheetPref(
            page = page.value,
            authorId = authorId
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            it.getViewModel().submitAction(PlayBroadcastAction.SetLiveToVodPref(
                type = type,
                page = page,
            ))
            mockHydraSharedPreferences.getLiveToVodBottomSheetPref(page = page.value, authorId = authorId)
                .assertEqualTo(false)
        }
    }

    @Test
    fun `when user set live to vod pref in report page`() {
        val type = TickerBottomSheetType.TICKER
        val page = TickerBottomSheetPage.LIVE_REPORT
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()
        val responseMock = uiModelBuilder.buildTickerBottomSheetResponse(
            page = page,
            type = type,
        )
        val isFirstTime = false
        val authorId = accountMock.find { it.isShop }?.id.orEmpty()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockRepo.getTickerBottomSheetConfig(any()) } returns responseMock
        coEvery { mockHydraSharedPreferences.getLiveToVodTickerPref(
            page = page.value,
            authorId = authorId
        ) } returns isFirstTime

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            sharedPref = mockHydraSharedPreferences,
        )

        robot.use {
            it.getViewModel().submitAction(PlayBroadcastAction.SetLiveToVodPref(
                type = type,
                page = page,
            ))
            mockHydraSharedPreferences.getLiveToVodTickerPref(page = page.value, authorId = authorId)
                .assertEqualTo(false)
        }
    }

    @Test
    fun `when user as shop setup channel and success`() {

        val configMock = uiModelBuilder.buildConfigurationUiModel(channelId = "123")
        val accountMock = uiModelBuilder.buildAccountListModel()
        val mockCover = PlayCoverUiModel(croppedCover = CoverSetupState.Blank, state = SetupDataState.Draft)
        val mockMaxProduct = 17

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockHydraConfigStore.getChannelId() } returns "123"
        coEvery { mockHydraConfigStore.getMaxProduct() } returns mockMaxProduct

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            mDataStore = mockDataStore,
            hydraConfigStore = mockHydraConfigStore,
        )

        robot.use {
            it.recordState {
                getAccountConfiguration(TYPE_SHOP)
            }
            it.getViewModel().channelId.assertEqualTo("123")
            it.getViewModel().maxProduct.assertEqualTo(mockMaxProduct)
            it.getViewModel().remainingDurationInMillis.assertEqualTo(0L)
            it.getViewModel().productSectionList.assertEqualTo(mockProductTagSectionList)


            val configInfo = it.getViewModel().observableConfigInfo.getOrAwaitValue()
            configInfo.assertEqualTo(NetworkResult.Success(configMock))
        }
    }

    @Test
    fun `when user as shop setup channel and empty`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel()
        val accountMock = uiModelBuilder.buildAccountListModel()

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockHydraConfigStore.getChannelId() } returns ""

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
            getChannelUseCase = mockGetChannelUseCase,
            getAddedChannelTagsUseCase = mockGetAddedTagUseCase,
            productMapper = PlayBroProductUiMapper(),
            mDataStore = mockDataStore,
            hydraConfigStore = mockHydraConfigStore,
        )

        robot.use {
            it.recordState { getAccountConfiguration(TYPE_SHOP) }
            it.getViewModel().channelId.assertEqualTo("")
            it.getViewModel().maxProduct.assertEqualTo(0)
            it.getViewModel().remainingDurationInMillis.assertEqualTo(0L)
            it.getViewModel().productSectionList.assertEqualTo(mockProductTagSectionList)

            val configInfo = it.getViewModel().observableConfigInfo.getOrAwaitValue()
            configInfo.assertEqualTo(NetworkResult.Success(configMock))
        }
    }

    @Test
    fun `when user save state`() {
        val accountMock = uiModelBuilder.buildAccountListModel().first()
        val mockConfig = uiModelBuilder.buildConfigurationUiModel()
        val bundle = Bundle().apply {
            putParcelable("channel_id", mockConfig)
            putBoolean("key_is_live_stream_ended", true)
            putParcelable("key_author", accountMock)
        }

        coEvery { mockHydraConfigStore.getAuthor() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig

        val robot = PlayBroadcastViewModelRobot(hydraConfigStore = mockHydraConfigStore, channelRepo = mockRepo)

        robot.use {
            it.getViewModel().saveState(bundle)
            it.getViewModel().restoreState(bundle)
        }
    }

    @Test
    fun `when user end live stream`() {
        val robot = PlayBroadcastViewModelRobot()

        robot.use {
            it.getViewModel().setIsLiveStreamEnded()

            it.getViewModel().isLiveStreamEnded().assertTrue()
        }
    }

    @Test
    fun `when user set up schedule and scheduled`() {
        val calendar = Calendar.getInstance()
        val mockDate = Date(calendar.timeInMillis)

        coEvery { mockRepo.updateSchedule(any(), mockDate) } returns BroadcastScheduleUiModel.Scheduled(
            mockDate, "yyyy/mm/dd"
        )

        val robot = PlayBroadcastViewModelRobot(channelRepo = mockRepo)

        robot.use {
            val state = robot.recordEvent {
                getViewModel().submitAction(PlayBroadcastAction.SetSchedule(mockDate))
            }
            state.last().assertEvent(PlayBroadcastEvent.SetScheduleSuccess(true))
        }
    }

    @Test
    fun `when user set up schedule and not scheduled`() {
        val calendar = Calendar.getInstance()
        val mockDate = Date(calendar.timeInMillis)

        coEvery { mockRepo.updateSchedule(any(), mockDate) } returns BroadcastScheduleUiModel.NoSchedule

        val robot = PlayBroadcastViewModelRobot(channelRepo = mockRepo)

        robot.use {
            val state = robot.recordEvent {
                getViewModel().submitAction(PlayBroadcastAction.SetSchedule(mockDate))
            }
            state.last().assertEvent(PlayBroadcastEvent.SetScheduleSuccess(false))
        }
    }

    @Test
    fun `when user set up schedule and fails`() {
        val calendar = Calendar.getInstance()
        val mockDate = Date(calendar.timeInMillis)

        coEvery { mockRepo.updateSchedule(any(), mockDate) } throws Exception("any exception")

        val robot = PlayBroadcastViewModelRobot(channelRepo = mockRepo)

        robot.use {
            val state = robot.recordEvent {
                getViewModel().submitAction(PlayBroadcastAction.SetSchedule(mockDate))
            }
            state.last().assertEvent(PlayBroadcastEvent.ShowScheduleError(Exception("any exception")))
        }
    }

    @Test
    fun `when user delete schedule and success`() {
        coEvery { mockRepo.updateSchedule(any(), null) } returns BroadcastScheduleUiModel.NoSchedule

        val robot = PlayBroadcastViewModelRobot(channelRepo = mockRepo)

        robot.use {
            val state = robot.recordEvent {
                getViewModel().submitAction(PlayBroadcastAction.DeleteSchedule)
            }
            state.last().assertEvent(PlayBroadcastEvent.DeleteScheduleSuccess)
        }
    }

    @Test
    fun `when user delete schedule and fails`() {
        coEvery { mockRepo.updateSchedule(any(), null) } throws Exception("any exception")

        val robot = PlayBroadcastViewModelRobot(channelRepo = mockRepo)

        robot.use {
            val state = robot.recordEvent {
                getViewModel().submitAction(PlayBroadcastAction.DeleteSchedule)
            }
            state.last().assertEvent(PlayBroadcastEvent.ShowScheduleError(Exception("any exception")))
        }
    }

    @Test
    fun `when user click pin product and failed from network` () {
        coEvery { mockRepo.setPinProduct(any(), any()) } returns false

        val product = ProductUiModel(id = "1", "Wafer", false,"", 10L, false, "", 0L, OriginalPrice("20",20.0),  PinProductUiModel(isPinned = false, canPin = true), "")

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val event = it.recordEvent {
                it.getViewModel().submitAction(PlayBroadcastAction.ClickPinProduct(product))
            }
            event.assertEmpty()
        }
    }

    @Test
    fun `when user click pin product and error from network` () {
        val error = MessageErrorException("Error kak")

        coEvery { mockRepo.setPinProduct(any(), any()) } throws error

        val product = ProductUiModel(id = "1", "Wafer", false,"", 10L, false, "", 0L, OriginalPrice("20",20.0),  PinProductUiModel(isPinned = false, canPin = true), "")

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val event = it.recordEvent {
                it.getViewModel().submitAction(PlayBroadcastAction.ClickPinProduct(product))
            }
            event.last().assertEqualTo(PlayBroadcastEvent.FailPinUnPinProduct(error, product.pinStatus.isPinned))
        }
    }
}
