package com.tokopedia.profilecompletion.profilemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.profilecompletion.domain.GetUrlProfileManagementResult
import com.tokopedia.profilecompletion.domain.GetUrlProfileManagementUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProfileManagementViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: ProfileManagementViewModel

    private val getUrlProfileManagementUseCase = mockk<GetUrlProfileManagementUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ProfileManagementViewModel(
            getUrlProfileManagementUseCase,
            dispatcher
        )
    }

    @Test
    fun `when get url profile management then return success`() {
        val url = "https://accounts.goto-products.com"
        val expected = GetUrlProfileManagementResult.Success(url)

        coEvery { getUrlProfileManagementUseCase.invoke(Unit) } returns expected
        viewModel.getProfileManagementData()

        val result = viewModel.getUrlProfileManagement.getOrAwaitValue()
        assertTrue(result is GetUrlProfileManagementResult.Success)
        assertEquals(url, viewModel.url)
    }

    @Test
    fun `when get url profile management then return failed`() {
        val throwable = Throwable()

        coEvery { getUrlProfileManagementUseCase.invoke(Unit) } throws throwable
        viewModel.getProfileManagementData()

        val result = viewModel.getUrlProfileManagement.getOrAwaitValue()
        assertTrue(result is GetUrlProfileManagementResult.Failed)
        assertTrue(viewModel.url.isEmpty())
        assertEquals(throwable, result.throwable)
    }

}
