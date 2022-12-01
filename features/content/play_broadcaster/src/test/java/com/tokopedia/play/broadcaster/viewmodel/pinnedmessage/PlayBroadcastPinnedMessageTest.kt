package com.tokopedia.play.broadcaster.viewmodel.pinnedmessage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
import com.tokopedia.play.broadcaster.util.assertEmpty
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertEvent
import com.tokopedia.play.broadcaster.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlayBroadcastPinnedMessageTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val uiModelBuilder = UiModelBuilder()
    private val productSetupUiModelBuilder = ProductSetupUiModelBuilder()

    private val mockConfig = uiModelBuilder.buildConfigurationUiModel(
        streamAllowed = true,
        channelId = "123"
    )
    private val mockException = Exception("any fail")
    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockProductTagSectionList = productSetupUiModelBuilder.buildProductTagSectionList()

    @Before
    fun setUp() {
        coEvery { mockRepo.getAccountList() } returns uiModelBuilder.buildAccountListModel()
        coEvery { mockRepo.getChannelConfiguration(any(), any()) } returns mockConfig
    }

    @Test
    fun `given there is active pinned message, when live stream start, then show correct pinned message`() {
        val message = "message test"
        val mockPinned = uiModelBuilder.buildPinnedMessageUiModel(
            id = "1",
            message = message,
            isActive = true
        )
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns mockPinned

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                startLive()
            }

            state.pinnedMessage
                .message
                .assertEqualTo(message)
        }
    }

    @Test
    fun `given there is no active pinned message, when live stream start, then pinned message will be empty`() {
        val message = "message test"
        val mockPinned = uiModelBuilder.buildPinnedMessageUiModel(
            message = message,
            isActive = false
        )
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns mockPinned

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                startLive()
            }

            state.pinnedMessage
                .message
                .assertEmpty()
        }
    }

    @Test
    fun `given there is no pinned message, when live stream start, then pinned message will be empty`() {
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns null

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                startLive()
            }

            state.pinnedMessage
                .message
                .assertEmpty()
        }
    }

    @Test
    fun `given network error, when set pinned message, then pinned message will not be changed`() {
        val prevPinned = uiModelBuilder.buildPinnedMessageUiModel(id = "1", message = "message 1")
        val newMessage = "abc"
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns prevPinned
        coEvery { mockRepo.setPinnedMessage(any(), any(), any()) } throws IllegalStateException()

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                startLive()

                setPinned(newMessage)
            }

            state.pinnedMessage
                .message
                .assertEqualTo(prevPinned.message)
        }
    }

    @Test
    fun `given network is fine, when set pinned message, then pinned message will be changed`() {
        val prevPinned = uiModelBuilder.buildPinnedMessageUiModel(id = "1", message = "message 1")
        val newMessage = "abc"
        coEvery { mockRepo.getActivePinnedMessage(any()) } returns prevPinned
        coEvery { mockRepo.setPinnedMessage(any(), any(), any()) } returns prevPinned.copy(
            message = newMessage
        )

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                startLive()

                setPinned(newMessage)
            }

            state.pinnedMessage
                .message
                .assertEqualTo(newMessage)
        }
    }

    @Test
    fun `when edit pinned message, then status should be edit`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val state = robot.recordState {
                getAccountConfiguration()
                startLive()

                editPinned()
            }

            state.pinnedMessage
                .editStatus.isEditing
                .assertTrue()
        }
    }

    @Test
    fun `when cancel edit pinned message, then status should be nothing`() {
        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val stateList = robot.recordStateAsList {
                getAccountConfiguration()
                startLive()

                editPinned()
                cancelEditPinned()
            }.takeLast(2)

            stateList.first().pinnedMessage
                .editStatus.isEditing
                .assertTrue()

            stateList.last().pinnedMessage
                .editStatus.isNothing
                .assertTrue()
        }
    }

    @Test
    fun `when user click pin and request success`() {
        val pinnedProduct = mockProductTagSectionList.first().products.first()

        coEvery { mockRepo.setPinProduct(any(), pinnedProduct) } returns true

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
            val stateList = robot.recordStateAsList {
                getAccountConfiguration()
                startLive()

                getViewModel().submitAction(PlayBroadcastAction.SetProduct(mockProductTagSectionList))
                getViewModel().submitAction(PlayBroadcastAction.ClickPinProduct(pinnedProduct))
            }.takeLast(2)

            val result = stateList.last().selectedProduct.map {
                it.products.findLast { product ->
                    product.id == pinnedProduct.id
                }
            }.first()

            result?.assertEqualTo(pinnedProduct.copy(pinStatus = PinProductUiModel(
                isPinned = !pinnedProduct.pinStatus.isPinned,
                canPin = pinnedProduct.pinStatus.canPin,
                isLoading = pinnedProduct.pinStatus.isLoading,
            )))
        }
    }

    @Test
    fun `when user click pin and request fail wont do anything`() {
        val pinnedProduct = mockProductTagSectionList.first().products.first()

        coEvery { mockRepo.setPinProduct(any(), pinnedProduct) } returns false

        val robot = PlayBroadcastViewModelRobot(
            dispatchers = testDispatcher,
            channelRepo = mockRepo,
        )

        robot.use {
           robot.recordStateAsList {
                getAccountConfiguration()
                startLive()
                getViewModel().submitAction(PlayBroadcastAction.ClickPinProduct(pinnedProduct))
            }
        }
    }

    @Test
    fun `when user click pin and request throws exception`() {
        val pinnedProduct = mockProductTagSectionList.first().products.first()

        coEvery { mockRepo.setPinProduct(any(), pinnedProduct) } throws mockException

        val robot = PlayBroadcastViewModelRobot(dispatchers = testDispatcher, channelRepo = mockRepo)

        robot.use {
            val event = robot.recordEvent {
                getAccountConfiguration()
                startLive()
                getViewModel().submitAction(PlayBroadcastAction.ClickPinProduct(pinnedProduct))
            }
            event.last().assertEvent(
                PlayBroadcastEvent.FailPinUnPinProduct(mockException, pinnedProduct.pinStatus.isPinned)
            )
        }
    }


}
