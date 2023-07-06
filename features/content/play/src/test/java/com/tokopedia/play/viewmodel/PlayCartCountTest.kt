package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
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
 * Created by kenny.hadisaputra on 14/03/23
 */
class PlayCartCountTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockRepo = mockk<PlayViewerRepository>(relaxed = true)

    private val modelBuilder = UiModelBuilder.get()

    private val showCartData = modelBuilder.buildChannelData(
        channelDetail = modelBuilder.buildChannelDetail(
            showCart = true,
        )
    )

    private val notShowCartData = modelBuilder.buildChannelData(
        channelDetail = modelBuilder.buildChannelDetail(
            showCart = false,
        )
    )

    private val cartCount = 150

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        coEvery { mockRepo.getCartCount() } returns cartCount
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test show cart state matches the one from repo`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val showState = it.recordState {
                createPage(showCartData)
            }
            showState.channel.showCart.assertTrue()

            val notShowState = it.recordState {
                createPage(notShowCartData)
            }
            notShowState.channel.showCart.assertFalse()
        }
    }

    @Test
    fun `test cart count when user is not logged in (1)`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(showCartData)
                focusPage(showCartData)
            }

            state.combinedState.cartCount.assertEqualTo(DEFAULT_CART_COUNT)
        }
    }

    @Test
    fun `test cart count when user is not logged in (2)`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(showCartData)
                focusPage(showCartData)
            }

            state.combinedState.cartCount.assertEqualTo(DEFAULT_CART_COUNT)
        }
    }

    @Test
    fun `test cart count when show cart is false`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(notShowCartData)
                focusPage(notShowCartData)
            }

            state.combinedState.cartCount.assertEqualTo(DEFAULT_CART_COUNT)
        }
    }

    @Test
    fun `test cart count when logged in and show cart`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
            userSession = mockk<UserSessionInterface>(relaxed = true).apply {
                every { isLoggedIn } returns true
            }
        )

        robot.use {
            val state = it.recordState {
                createPage(showCartData)
                focusPage(showCartData)
            }

            state.combinedState.cartCount.assertEqualTo(cartCount)
        }
    }

    companion object {
        private const val DEFAULT_CART_COUNT = 0
    }
}
