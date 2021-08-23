package com.tokopedia.play.viewmodel.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayCartInfoModelBuilder
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.uimodel.state.PlayCartCount
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
class PlayCartTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val cartInfoModelBuilder = PlayCartInfoModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given cart is not shown, when focus page, then cart should not be shown`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = false)
        )

        givenPlayViewModelRobot(
                dispatchers = testDispatcher,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                cart.shouldShow.isFalse()
            }
        }
    }

    @Test
    fun `given cart is shown, when focus page, then cart should be shown`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        givenPlayViewModelRobot(
                dispatchers = testDispatcher,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                cart.shouldShow.isTrue()
            }
        }
    }

    @Test
    fun `given cart count is 0, when focus page, then cart count should be hidden`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getItemCountInCart() } returns 0

        givenPlayViewModelRobot(
                repo = mockRepo,
                dispatchers = testDispatcher,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                cart.count.isInstanceOf(PlayCartCount.Hide::class.java)
            }
        }
    }

    @Test
    fun `given cart count is greater than 0 and less than 100, when focus page, then cart count should be shown with exact number`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        val cartCount = 50

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getItemCountInCart() } returns cartCount

        givenPlayViewModelRobot(
                repo = mockRepo,
                dispatchers = testDispatcher,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                cart.count.isInstanceOf(PlayCartCount.Show::class.java)
                (cart.count as PlayCartCount.Show).countText
                        .isEqualTo(cartCount.toString())
            }
        }
    }

    @Test
    fun `given cart count is greater than 100, when focus page, then cart count should be shown as 99+`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        val cartCount = 123

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getItemCountInCart() } returns cartCount

        givenPlayViewModelRobot(
                repo = mockRepo,
                dispatchers = testDispatcher,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } thenVerify {
            withState {
                cart.count.isInstanceOf(PlayCartCount.Show::class.java)
                (cart.count as PlayCartCount.Show).countText
                        .isEqualTo("99+")
            }
        }
    }
}