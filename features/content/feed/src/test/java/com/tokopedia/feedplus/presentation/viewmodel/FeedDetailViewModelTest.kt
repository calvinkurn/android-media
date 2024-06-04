package com.tokopedia.feedplus.presentation.viewmodel

import com.tokopedia.content.test.util.assertEmpty
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertFalse
import com.tokopedia.content.test.util.assertTrue
import com.tokopedia.feedplus.detail.data.FeedDetailRepository
import com.tokopedia.feedplus.presentation.robot.createFeedDetailViewModelRobot
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by meyta.taliti on 10/09/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FeedDetailViewModelTest {

    private val testDispatcher = CoroutineTestDispatchers

    private val mockRepo: FeedDetailRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when show search bar is true, it should not hit getTitle API & search model should be true`() = runTestUnconfined {
        val robot = createFeedDetailViewModelRobot(mockRepo)

        val source = "xxx"

        robot.use {
            it.run {
                it.viewModel.getHeader(source, isShowSearchbar = true)
            }
            it.viewModel.headerDetail.value.isShowSearchBar.assertTrue()
            it.viewModel.headerDetail.value.title.assertEmpty()
            coVerify(exactly = 0) { mockRepo.getTitle(source) }
        }
    }

    @Test
    fun `when show search bar is false, it should get title`() = runTestUnconfined {
        val robot = createFeedDetailViewModelRobot(mockRepo)

        val source = "xxx"
        val mockTitle = "pokemon"

        coEvery { mockRepo.getTitle(source) } returns mockTitle

        robot.use {
            it.run {
                it.viewModel.getHeader(source, isShowSearchbar = false)
            }
            it.viewModel.headerDetail.value.isShowSearchBar.assertFalse()
            it.viewModel.headerDetail.value.title.assertEqualTo(mockTitle)
            coVerify(exactly = 1) { mockRepo.getTitle(source) }
        }
    }

    private fun runTestUnconfined(testBody: suspend TestScope.() -> Unit) = runTest(
        UnconfinedTestDispatcher(),
        testBody = testBody
    )
}
