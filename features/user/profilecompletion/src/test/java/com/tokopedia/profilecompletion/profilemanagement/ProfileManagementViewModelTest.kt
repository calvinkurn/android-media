package com.tokopedia.profilecompletion.profilemanagement

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.profilecompletion.data.SeamlessData
import com.tokopedia.profilecompletion.domain.GetGotoCookieResult
import com.tokopedia.profilecompletion.domain.GetGotoCookieUseCase
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

    private val getGotoCookieUseCase = mockk<GetGotoCookieUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ProfileManagementViewModel(
            getGotoCookieUseCase,
            dispatcher
        )
    }

    @Test
    fun `when get url profile management then return success`() {
        val seamlessData = SeamlessData(
            backUrl = "tokopedia://back",
            expiredAt = 100000L,
            token = "qwerty"
        )
        val expected = GetGotoCookieResult.Success(seamlessData)

        coEvery { getGotoCookieUseCase.invoke(any()) } returns expected
        viewModel.getProfileManagementData()

        val result = viewModel.getUrlProfileManagement.getOrAwaitValue()
        assertTrue(result is GetGotoCookieResult.Success)
        assertEquals(seamlessData, result.seamlessData)
    }

    @Test
    fun `when get url profile management then return failed`() {
        val throwable = Throwable(message = "error")
        val expected = GetGotoCookieResult.Failed(throwable)

        coEvery { getGotoCookieUseCase.invoke(any()) } returns expected
        viewModel.getProfileManagementData()

        val result = viewModel.getUrlProfileManagement.getOrAwaitValue()
        assertTrue(result is GetGotoCookieResult.Failed)
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `when get url profile management then return error`() {
        val throwable = Throwable()

        coEvery { getGotoCookieUseCase.invoke(any()) } throws throwable
        viewModel.getProfileManagementData()

        val result = viewModel.getUrlProfileManagement.getOrAwaitValue()
        assertTrue(result is GetGotoCookieResult.Failed)
        assertEquals(throwable, result.throwable)
    }

}
