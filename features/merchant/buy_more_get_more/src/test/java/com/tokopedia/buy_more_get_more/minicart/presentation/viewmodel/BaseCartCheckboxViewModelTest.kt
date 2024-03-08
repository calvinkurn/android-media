package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.cartcommon.data.request.checkbox.SetCartlistCheckboxStateRequest
import com.tokopedia.cartcommon.domain.usecase.SetCartlistCheckboxStateUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by @ilhamsuaib on 28/08/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseCartCheckboxViewModelTest<T : BaseCartCheckboxViewModel> : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var setCartListCheckboxStateUseCase: SetCartlistCheckboxStateUseCase

    lateinit var viewModel: T

    override fun initVariables() {
        viewModel = createViewModel()
    }

    abstract fun createViewModel(): T

    @Test
    fun `when set check box state then return success result with true value`() {
        runTest {
            val cartIds = listOf("1")
            val request = cartIds.map { SetCartlistCheckboxStateRequest(it, true) }
            val results = mutableListOf<BmgmState<Boolean>>()
            coEvery {
                setCartListCheckboxStateUseCase.invoke(request)
            } returns true

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.setCheckListState.toList(results)
            }

            viewModel.setCartListCheckboxState(cartIds)

            coVerify {
                setCartListCheckboxStateUseCase.invoke(request)
            }

            // expected loading state first
            assertEquals(BmgmState.Loading, results[0])

            // then set to success state
            assertEquals(BmgmState.Success(true), results[1])

            job.cancel()
        }
    }

    @Test
    fun `when set check box state then return success result with false value`() {
        runTest {
            val cartIds = listOf("1")
            val request = cartIds.map { SetCartlistCheckboxStateRequest(it, true) }
            val results = mutableListOf<BmgmState<Boolean>>()
            coEvery {
                setCartListCheckboxStateUseCase.invoke(request)
            } returns false

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.setCheckListState.toList(results)
            }

            viewModel.setCartListCheckboxState(cartIds)

            coVerify {
                setCartListCheckboxStateUseCase.invoke(request)
            }

            // expected loading state first
            assertEquals(BmgmState.Loading, results[0])

            // then set to success state
            assertEquals(BmgmState.Success(false), results[1])

            job.cancel()
        }
    }

    @Test
    fun `when set check box state then return failed result`() {
        runTest {
            val cartIds = listOf("1")
            val request = cartIds.map { SetCartlistCheckboxStateRequest(it, true) }
            val throwable = RuntimeException("message")
            val results = mutableListOf<BmgmState<Boolean>>()

            coEvery {
                setCartListCheckboxStateUseCase.invoke(request)
            } throws throwable

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.setCheckListState.toList(results)
            }

            viewModel.setCartListCheckboxState(cartIds)

            coVerify {
                setCartListCheckboxStateUseCase.invoke(request)
            }

            // expected loading state first
            assertEquals(BmgmState.Loading, results[0])

            // then set to error state
            results[1].verifyErrorEquals(BmgmState.Error(throwable))

            job.cancel()
        }
    }
}
