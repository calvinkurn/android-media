package com.tokopedia.play.broadcaster.viewmodel.summary.postvideo

import android.net.Uri
import com.tokopedia.play.broadcaster.domain.model.GetChannelResponse
import com.tokopedia.play.broadcaster.domain.model.GetLiveStatisticsResponse
import com.tokopedia.play.broadcaster.domain.model.SetChannelTagsResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.GetSellerLeaderboardSlotResponse
import com.tokopedia.play.broadcaster.domain.model.interactive.quiz.GetInteractiveSummaryLivestreamResponse
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetLiveStatisticsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveSummaryLivestreamUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetSellerLeaderboardUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play_common.domain.model.ChannelId
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.error.DefaultErrorThrowable
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
class PlayBroadcastSummaryPostVideoModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockSetChannelTagUseCase: SetChannelTagsUseCase = mockk(relaxed = true)
    private val mockUpdateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockGetChannelUseCase: GetChannelUseCase = mockk(relaxed = true)
    private val mockGetLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true)
    private val mockGetSellerLeaderboardUseCase: GetSellerLeaderboardUseCase = mockk(relaxed = true)
    private val mockGetInteractiveSummaryLivestreamUseCase: GetInteractiveSummaryLivestreamUseCase = mockk(relaxed = true)

    private val modelBuilder = UiModelBuilder()

    private val mockException = modelBuilder.buildException()
    private val mockSetChannelTagResponse = SetChannelTagsResponse(SetChannelTagsResponse.SetTags(true))
    private val mockSetChannelTagsResponseFail = SetChannelTagsResponse(SetChannelTagsResponse.SetTags(false))
    private val mockUpdateChannelStatusResponse = ChannelId("")
    private val mockShopName = "Jonathan's Shop"

    private val mockLocalUri = mockk<Uri>(relaxed = true)
    private val mockCoverUri = mockk<Uri>(relaxed = true)
    private val mockInitialCoverUrl = "https://tokopedia.com"
    private val mockLocalUriString = "localUri"
    private val mockCoverUriString = "https://tokopdia.com/coverUri"

    private val mockChannel = GetChannelResponse.Channel(
        basic = GetChannelResponse.ChannelBasic(
            coverUrl = mockInitialCoverUrl,
            channelId = "12345",
        )
    )

    @Before
    fun setUp() {
        coEvery { mockUserSession.shopName } returns mockShopName
        coEvery { mockLocalUri.toString() } returns mockLocalUriString
        coEvery { mockCoverUri.toString() } returns mockCoverUriString
        coEvery { mockGetChannelUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetLiveStatisticsUseCase.executeOnBackground() } returns GetLiveStatisticsResponse.ReportChannelSummary(duration = "10:00:00")
        coEvery { mockGetSellerLeaderboardUseCase.executeOnBackground() } returns GetSellerLeaderboardSlotResponse()
        coEvery { mockGetInteractiveSummaryLivestreamUseCase.executeOnBackground() } returns GetInteractiveSummaryLivestreamResponse()
    }

    @Test
    fun `when save video success, it should return success state`() {

        coEvery { mockSetChannelTagUseCase.executeOnBackground() } returns mockSetChannelTagResponse
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } returns mockUpdateChannelStatusResponse

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            events.last().assertEqualTo(
                PlayBroadcastSummaryEvent.PostVideo(NetworkResult.Success(true))
            )
        }
    }

    @Test
    fun `when save video & save tag failed, it should return fail state`() {

        coEvery { mockSetChannelTagUseCase.executeOnBackground() } throws mockException
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } returns mockUpdateChannelStatusResponse

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            assertTrue(events.last() is PlayBroadcastSummaryEvent.PostVideo)

            val event = events.last() as PlayBroadcastSummaryEvent.PostVideo
            assertTrue(event.networkResult is NetworkResult.Fail)

            val networkResultFail = event.networkResult as NetworkResult.Fail
            assertTrue(networkResultFail.error.message == mockException.message)
        }
    }

    @Test
    fun `when save video & save tag returns failed, it should return fail state`() {
        coEvery { mockSetChannelTagUseCase.executeOnBackground() } returns mockSetChannelTagsResponseFail
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } returns mockUpdateChannelStatusResponse

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            assertTrue(events.last() is PlayBroadcastSummaryEvent.PostVideo)

            val event = events.last() as PlayBroadcastSummaryEvent.PostVideo
            assertTrue(event.networkResult is NetworkResult.Fail)

            val networkResultFail = event.networkResult as NetworkResult.Fail
            assertTrue(networkResultFail.error.message == "${DefaultErrorThrowable.DEFAULT_MESSAGE}: Error Tag")
        }
    }

    @Test
    fun `when save video & update channel status failed, it should return fail state`() {
        coEvery { mockSetChannelTagUseCase.executeOnBackground() } returns mockSetChannelTagResponse
        coEvery { mockUpdateChannelUseCase.executeOnBackground() } throws mockException

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
        )

        robot.use {
            val events = robot.recordEvent {
                submitAction(PlayBroadcastSummaryAction.ClickPostVideoNow)
            }

            assertTrue(events.last() is PlayBroadcastSummaryEvent.PostVideo)

            val event = events.last() as PlayBroadcastSummaryEvent.PostVideo
            assertTrue(event.networkResult is NetworkResult.Fail)

            val networkResultFail = event.networkResult as NetworkResult.Fail
            assertTrue(networkResultFail.error.message == mockException.message)
        }
    }

    /** Constant */
    @Test
    fun `when app tries to get shopName, it should return shopName from user session`() {
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
            userSession = mockUserSession
        )

        robot.use {
            it.getViewModel().shopName.assertEqualTo(mockShopName)
        }
    }

    /** Set Cover */
    @Test
    fun `given cover cropped uploaded, it should emit the new coverUrl`() {
        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
            userSession = mockUserSession,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
        )

        testDispatcher.coroutineDispatcher.resumeDispatcher()

        robot.use {
            val state = it.recordState {
                submitAction(
                    PlayBroadcastSummaryAction.SetCover(
                        PlayCoverUiModel(
                            croppedCover = CoverSetupState.Cropped.Uploaded(
                                localImage = mockLocalUri,
                                coverImage = mockCoverUri,
                                coverSource = CoverSource.Camera
                            ),
                            state = SetupDataState.Uploaded
                        )
                    )
                )
            }

            state.channelSummary.coverUrl.assertEqualTo(mockCoverUriString)
        }
    }

    @Test
    fun `given cover cropped from local, it should emit the new coverUrl`() {

        coEvery { mockCoverUri.toString() } returns "coverUri"

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
            userSession = mockUserSession,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
        )

        testDispatcher.coroutineDispatcher.resumeDispatcher()

        robot.use {
            val state = it.recordState {
                submitAction(
                    PlayBroadcastSummaryAction.SetCover(
                        PlayCoverUiModel(
                            croppedCover = CoverSetupState.Cropped.Uploaded(
                                localImage = mockLocalUri,
                                coverImage = mockCoverUri,
                                coverSource = CoverSource.Camera
                            ),
                            state = SetupDataState.Uploaded
                        )
                    )
                )
            }

            state.channelSummary.coverUrl.assertEqualTo(mockLocalUriString)
        }
    }

    @Test
    fun `given cover cropped but both coverUri and localUri are empty, it should retain the old coverUrl`() {

        coEvery { mockCoverUri.toString() } returns ""
        coEvery { mockLocalUri.toString() } returns ""

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
            userSession = mockUserSession,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
        )

        robot.use {
            val state = it.recordState {
                submitAction(
                    PlayBroadcastSummaryAction.SetCover(
                        PlayCoverUiModel(
                            croppedCover = CoverSetupState.Cropped.Uploaded(
                                localImage = mockLocalUri,
                                coverImage = mockCoverUri,
                                coverSource = CoverSource.Camera
                            ),
                            state = SetupDataState.Uploaded
                        )
                    )
                )
            }

            state.channelSummary.coverUrl.assertEqualTo(mockInitialCoverUrl)
        }
    }

    @Test
    fun `given generated cover, it should emit the new coverUrl`() {

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
            userSession = mockUserSession,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
        )

        testDispatcher.coroutineDispatcher.resumeDispatcher()

        robot.use {
            val state = it.recordState {
                submitAction(
                    PlayBroadcastSummaryAction.SetCover(
                        PlayCoverUiModel(
                            croppedCover = CoverSetupState.GeneratedCover(
                                coverImage = mockCoverUriString
                            ),
                            state = SetupDataState.Uploaded
                        )
                    )
                )
            }

            state.channelSummary.coverUrl.assertEqualTo(mockCoverUriString)
        }
    }

    @Test
    fun `given generated cover but the url is empty, it should retain the old coverUrl`() {

        val robot = PlayBroadcastSummaryViewModelRobot(
            dispatcher = testDispatcher,
            updateChannelUseCase = mockUpdateChannelUseCase,
            setChannelTagsUseCase =  mockSetChannelTagUseCase,
            userSession = mockUserSession,
            getChannelUseCase = mockGetChannelUseCase,
            getLiveStatisticsUseCase = mockGetLiveStatisticsUseCase,
            getSellerLeaderboardUseCase = mockGetSellerLeaderboardUseCase,
            getInteractiveSummaryLivestreamUseCase = mockGetInteractiveSummaryLivestreamUseCase,
        )

        robot.use {
            val state = it.recordState {
                submitAction(
                    PlayBroadcastSummaryAction.SetCover(
                        PlayCoverUiModel(
                            croppedCover = CoverSetupState.GeneratedCover(
                                coverImage = ""
                            ),
                            state = SetupDataState.Uploaded
                        )
                    )
                )
            }

            state.channelSummary.coverUrl.assertEqualTo(mockInitialCoverUrl)
        }
    }
}
