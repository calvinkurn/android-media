package com.tokopedia.play.broadcaster.viewmodel.summary.action

import com.tokopedia.play.broadcaster.robot.PlayBroadcastSummaryViewModelRobot
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
class PlayBroadcastSummaryActionViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    @Test
    fun `when user click close button on report page, it should emit event close report page`() {
        rule.runBlockingTest {
            val robot = PlayBroadcastSummaryViewModelRobot(
                dispatcher = testDispatcher,
            )

            advanceUntilIdle()

            robot.use {
                val event = robot.recordEvent {
                    submitAction(PlayBroadcastSummaryAction.ClickCloseReportPage)
                }

                event.last().assertEqualTo(PlayBroadcastSummaryEvent.CloseReportPage)
            }
        }
    }

    @Test
    fun `when user click view leaderboard, it should emit event open leaderboard bottom sheet`() {
        rule.runBlockingTest {
            val robot = PlayBroadcastSummaryViewModelRobot(
                dispatcher = testDispatcher,
            )

            advanceUntilIdle()

            robot.use {
                val event = robot.recordEvent {
                    submitAction(PlayBroadcastSummaryAction.ClickViewLeaderboard)
                }

                event.last().assertEqualTo(PlayBroadcastSummaryEvent.OpenLeaderboardBottomSheet)
            }
        }
    }

    @Test
    fun `when user click post video on report page, it should emit event open post video page`() {
        rule.runBlockingTest {
            val robot = PlayBroadcastSummaryViewModelRobot(
                dispatcher = testDispatcher,
            )

            advanceUntilIdle()

            robot.use {
                val event = robot.recordEvent {
                    submitAction(PlayBroadcastSummaryAction.ClickPostVideo)
                }

                event.last().assertEqualTo(PlayBroadcastSummaryEvent.OpenPostVideoPage)
            }
        }
    }

    @Test
    fun `when user click back to report page, it should emit event back to report page`() {
        rule.runBlockingTest {
            val robot = PlayBroadcastSummaryViewModelRobot(
                dispatcher = testDispatcher,
            )

            advanceUntilIdle()

            robot.use {
                val event = robot.recordEvent {
                    submitAction(PlayBroadcastSummaryAction.ClickBackToReportPage)
                }

                event.last().assertEqualTo(PlayBroadcastSummaryEvent.BackToReportPage)
            }
        }
    }

    @Test
    fun `when user click edit cover, it should emit event open select cover bottom sheet`() {
        rule.runBlockingTest {
            val robot = PlayBroadcastSummaryViewModelRobot(
                dispatcher = testDispatcher,
            )

            advanceUntilIdle()

            robot.use {
                val event = robot.recordEvent {
                    submitAction(PlayBroadcastSummaryAction.ClickEditCover)
                }

                event.last().assertEqualTo(PlayBroadcastSummaryEvent.OpenSelectCoverBottomSheet)
            }
        }
    }
}