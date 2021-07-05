package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play_common.player.PlayVideoWrapper
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `given video player instance is created, when retrieved, it should return the correct video player instance`() {
        val mockPlayerBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true)
        val mockPlayer: PlayVideoWrapper = mockk(relaxed = true)
        every { mockPlayerBuilder.build() } returns mockPlayer

        givenPlayViewModelRobot(
                playVideoBuilder = mockPlayerBuilder
        ) andWhen {
            getVideoPlayer()
        } thenVerify { result ->
            result.isEqualTo(mockPlayer)
        }
    }
}