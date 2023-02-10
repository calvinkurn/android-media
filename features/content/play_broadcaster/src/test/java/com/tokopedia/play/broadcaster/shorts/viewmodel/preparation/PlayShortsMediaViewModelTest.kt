package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.util.assertEqualTo
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsMediaViewModelTest {

    private val mediaUri1 = "mediaUri1"
    private val mediaUri2 = "mediaUri2"

    @Test
    fun playShorts_preparation_media_firstTimeSetMedia() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.SetMedia(mediaUri1))
            }

            state.media.mediaUri.assertEqualTo(mediaUri1)
        }
    }

    @Test
    fun playShorts_preparation_media_setMediaTwoTimesWithTheSameUri() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.SetMedia(mediaUri1))
            }

            state.media.mediaUri.assertEqualTo(mediaUri1)

            val state2 = it.recordState {
                submitAction(PlayShortsAction.SetMedia(mediaUri1))
            }

            state2.media.mediaUri.assertEqualTo(state.media.mediaUri)
        }
    }

    @Test
    fun playShorts_preparation_media_changeMediaUri() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState {
                submitAction(PlayShortsAction.SetMedia(mediaUri1))
            }

            state.media.mediaUri.assertEqualTo(mediaUri1)

            val state2 = it.recordState {
                submitAction(PlayShortsAction.SetMedia(mediaUri2))
            }

            state2.media.mediaUri.assertEqualTo(mediaUri2)
        }
    }
}
