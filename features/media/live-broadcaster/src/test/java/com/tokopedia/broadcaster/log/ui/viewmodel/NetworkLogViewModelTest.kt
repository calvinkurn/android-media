package com.tokopedia.broadcaster.log.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.broadcaster.log.data.entity.NetworkLog
import com.tokopedia.broadcaster.log.data.repository.ChuckerLogRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class NetworkLogViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private val repository = mockk<ChuckerLogRepository>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private val viewModel = NetworkLogViewModel(
        repository,
        dispatcher
    )

    @Before fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel.chuckers.observeForever {}
    }

    @Test fun `Should be return list of chucker`() = dispatcher.runBlockingTest {
        // Given
        val givenUrl = "https://ingest.tokopedia.net/live/isfa"
        every { repository.chuckers() } returns mutableListOf(
            NetworkLog(url = givenUrl)
        )

        // When
        viewModel.getChuckers()

        // Then
        assertThat(viewModel.chuckers.value?.first()?.url, `is`(givenUrl))
    }

    @Test fun `Should be return empty list of chucker`() = dispatcher.runBlockingTest {
        // Given
        every { repository.chuckers() } returns mutableListOf()

        // When
        viewModel.getChuckers()

        // Then
        assertTrue { viewModel.chuckers.value.isNullOrEmpty() }
    }

    @After fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

}