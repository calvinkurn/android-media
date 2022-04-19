package com.tokopedia.play.viewmodel.like

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.ClickLikeAction
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.event.ShowLikeBubbleEvent
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 23/08/21
 */
class PlayLikeTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val mockVODChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.VOD
            )
        )
    )
    private val mockLiveChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.Live
            )
        )
    )

    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    @Before
    fun setUp() {
        every {
            mockRemoteConfig.getBoolean(any(), any())
        } returns true
    }

    @Test
    fun `given user is logged in, channel is VOD, and channel is not liked, when click like, then channel should be liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns false

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                setLoggedIn(true)
                createPage(mockVODChannelData)
                focusPage(mockVODChannelData)

                submitAction(ClickLikeAction)
            }

            state.like.isLiked.assertTrue()
        }
    }

    @Test
    fun `given user is logged in, channel is Live, and channel is not liked, when click like, then channel should be liked and show bubble`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns false

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val (state, event) = robot.recordStateAndEvent {
                setLoggedIn(true)
                createPage(mockLiveChannelData)
                focusPage(mockLiveChannelData)

                submitAction(ClickLikeAction)
            }

            state.like.isLiked.assertTrue()

            event.last()
                .isEqualToIgnoringFields(
                    ShowLikeBubbleEvent.Single(1, reduceOpacity = false, config = mockk(relaxed = true)),
                    ShowLikeBubbleEvent.Single::config
                )
        }
    }

    @Test
    fun `given user is logged in, channel is VOD and channel is liked, when click like, then channel should not be liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns true

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val state = robot.recordState {
                setLoggedIn(true)
                createPage(mockVODChannelData)
                focusPage(mockVODChannelData)

                submitAction(ClickLikeAction)
            }

            state.like.isLiked.assertFalse()
        }
    }

    @Test
    fun `given user is logged in, channel is Live and channel is liked, when click like, then channel is still liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns true

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val (state, event) = robot.recordStateAndEvent {
                setLoggedIn(true)
                createPage(mockLiveChannelData)
                focusPage(mockLiveChannelData)

                submitAction(ClickLikeAction)
            }

            state.like.isLiked.assertTrue()

            event.last()
                .isEqualToIgnoringFields(
                    ShowLikeBubbleEvent.Single(1, reduceOpacity = false, config = mockk(relaxed = true)),
                    ShowLikeBubbleEvent.Single::config
                )
        }
    }

    @Test
    fun `given user is not logged in, when click like, then channel should not be liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns false

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            val (state, event) = robot.recordStateAndEvent {
                setLoggedIn(false)
                createPage(mockVODChannelData)
                focusPage(mockVODChannelData)

                submitAction(ClickLikeAction)
            }

            state.like.isLiked.assertFalse()
            event.last()
                .isEqualToIgnoringFields(
                    OpenPageEvent(ApplinkConst.LOGIN),
                    OpenPageEvent::requestCode
                )
        }
    }

}