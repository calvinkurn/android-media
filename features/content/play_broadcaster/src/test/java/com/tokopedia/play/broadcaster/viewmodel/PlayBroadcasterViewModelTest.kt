package com.tokopedia.play.broadcaster.viewmodel

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_SHOP
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_USER
import com.tokopedia.content.common.ui.bottomsheet.WarningInfoBottomSheet
import com.tokopedia.content.common.ui.model.AccountStateInfoType
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
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
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.title.PlayTitleUiModel
import com.tokopedia.play.broadcaster.util.assertEmpty
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertEvent
import com.tokopedia.play.broadcaster.util.assertFailed
import com.tokopedia.play.broadcaster.util.assertFalse
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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
            state.accountStateInfo.type.assertEqualTo(AccountStateInfoType.NotAcceptTNC)
            state.accountStateInfo.selectedAccount.type.assertEqualTo(TYPE_SHOP)
            it.getViewModel().isAllowChangeAccount.assertFalse()
            it.getViewModel().tncList.assertEqualTo(mockTnc)
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
        val accountMock = uiModelBuilder.buildAccountListModel(tncShop = false, usernameBuyer = false)

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
                getViewModel().submitAction(PlayBroadcastAction.SetCover(mockCover))
            }
            state.selectedContentAccount.type.assertEqualTo(TYPE_USER)
            it.getViewModel().contentAccountList.assertEqualTo(accountMock)
        }
    }

    @Test
    fun `when user as shop setup channel and success`() {
        val configMock = uiModelBuilder.buildConfigurationUiModel(channelId = "123")
        val accountMock = uiModelBuilder.buildAccountListModel()
        val mockTitle = PlayTitleUiModel.HasTitle("Title 1")
        val mockCover = PlayCoverUiModel(croppedCover = CoverSetupState.Blank, state = SetupDataState.Draft)

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockDataStore.getSetupDataStore().getTitle() } returns mockTitle
        coEvery { mockHydraConfigStore.getChannelId() } returns "123"

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
                getViewModel().submitAction(PlayBroadcastAction.SetCover(mockCover))
            }
            it.getViewModel().channelId.assertEqualTo("123")
            it.getViewModel().channelTitle.assertEqualTo("Title 1")
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
        val mockTitle = PlayTitleUiModel.NoTitle

        coEvery { mockRepo.getAccountList() } returns accountMock
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns configMock
        coEvery { mockDataStore.getSetupDataStore().getTitle() } returns mockTitle
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
            it.getViewModel().channelTitle.assertEqualTo("")
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

}
