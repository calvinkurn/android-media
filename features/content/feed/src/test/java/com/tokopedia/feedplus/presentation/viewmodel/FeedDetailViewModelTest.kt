package com.tokopedia.feedplus.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedplus.detail.data.FeedDetailRepository
import com.tokopedia.feedplus.presentation.robot.createFeedDetailViewModelRobot
import com.tokopedia.tokopedia.feedplus.helper.assertEqualTo
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
 * Created by meyta.taliti on 10/09/23.
 */
class FeedDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun `action to get title by source, then it should return title`() {
        val robot = createFeedDetailViewModelRobot(mockRepo)

        val source = "xxx"
        val expectedTitle = "this is new title"

        coEvery { mockRepo.getTitle(source) } returns expectedTitle

        robot.use {
            it.run {
                it.viewModel.getTitle(source)
            }
            it.viewModel.titleLiveData.value.assertEqualTo(expectedTitle)
        }
    }
}
