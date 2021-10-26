package com.tokopedia.play.viewmodel.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayCartInfoModelBuilder
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.robot.play.*
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.view.uimodel.action.ClickCartAction
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play.view.uimodel.state.PlayCartCount
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
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

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val cartInfoModelBuilder = PlayCartInfoModelBuilder()

    @Test
    fun `given cart is not shown, when focus page, then cart should not be shown`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = false)
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher
        )

        val state = robot.recordState {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }

        state.cart.shouldShow.assertFalse()
    }

    @Test
    fun `given cart is shown, when focus page, then cart should be shown`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher
        )

        robot.use {
            val state = robot.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }

            state.cart.shouldShow.assertTrue()
        }
    }

    @Test
    fun `given cart count is 0, when focus page, then cart count should be hidden`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        val mockRepo: PlayViewerRepository = mockk(relaxed = true)
        coEvery { mockRepo.getItemCountInCart() } returns 0

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher
        )

        robot.use {
            val state = robot.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }

            state.cart.count.isInstanceOf(PlayCartCount.Hide::class.java)
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

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher
        )

        robot.use {
            val state = robot.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }

            state.cart.count.isInstanceOf(PlayCartCount.Show::class.java)
            (state.cart.count as PlayCartCount.Show).countText
                .isEqualTo(cartCount.toString())
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

        val robot = createPlayViewModelRobot(
            repo = mockRepo,
            dispatchers = testDispatcher
        )

        robot.use {
            val state = robot.recordState {
                createPage(mockChannelData)
                focusPage(mockChannelData)
            }

            state.cart.count.isInstanceOf(PlayCartCount.Show::class.java)
            (state.cart.count as PlayCartCount.Show).countText
                .isEqualTo("99+")
        }
    }

    @Test
    fun `given cart is shown, when cart clicked, then should open cart page`() {
        val mockChannelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoModelBuilder.build(shouldShow = true)
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher
        )

        robot.use {
            val event = robot.recordEvent {
                setLoggedIn(true)
                createPage(mockChannelData)
                focusPage(mockChannelData)

                submitAction(ClickCartAction)
            }

            event.last()
                .isEqualTo(
                    OpenPageEvent(
                        applink = ApplinkConst.CART
                    )
                )
        }
    }
}