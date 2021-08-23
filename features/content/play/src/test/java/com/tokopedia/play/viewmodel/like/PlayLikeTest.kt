package com.tokopedia.play.viewmodel.like

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayPartnerInfoModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.uimodel.action.ClickLikeAction
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 23/08/21
 */
class PlayLikeTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given user is logged in and channel is not liked, when click like, then channel should be liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns false

        givenPlayViewModelRobot(
                repo = mockRepo,
                dispatchers = testDispatcher,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickLikeAction)
        } thenVerify {
            withState {
                like.isLiked.isTrue()
            }
        }
    }

    @Test
    fun `given user is logged in and channel is liked, when click like, then channel should not be liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns true

        givenPlayViewModelRobot(
                repo = mockRepo,
                dispatchers = testDispatcher,
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickLikeAction)
        } thenVerify {
            withState {
                like.isLiked.isFalse()
            }
        }
    }

    @Test
    fun `given user is not logged in, when click like, then channel should not be liked`() {
        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getIsLiked(any(), any()) } returns false

        givenPlayViewModelRobot(
                repo = mockRepo,
                dispatchers = testDispatcher,
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickLikeAction)
        } thenVerify {
            withState {
                like.isLiked.isFalse()
            }
        }
    }

}