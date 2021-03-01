package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

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