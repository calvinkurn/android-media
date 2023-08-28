package com.tokopedia.minicart.bmgm.presentation.viewmodel

import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.domain.usecase.SetCartListCheckboxStateUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList

/**
 * Created by @ilhamsuaib on 28/08/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
open class BaseCartCheckboxViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    protected lateinit var setCartListCheckboxStateUseCase: SetCartListCheckboxStateUseCase

    private lateinit var viewModel: BaseCartCheckboxViewModel

    override fun initVariables() {
        viewModel = BaseCartCheckboxViewModel(
            setCartListCheckboxStateUseCase,
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `when set check box state then return success result`() {
        runTest {
            val cartIds = anyList<String>()
            val results = mutableListOf<BmgmState<Boolean>>()
            coEvery {
                setCartListCheckboxStateUseCase.invoke(cartIds)
            } returns true

            viewModel.setCheckListState.observeForever {
                results.add(it)
            }

            viewModel.setCartListCheckboxState(cartIds)

            coVerify {
                setCartListCheckboxStateUseCase.invoke(cartIds)
            }

            //expected loading state first
            assertEquals(BmgmState.Loading, results[0])

            //then set to success state
            assertEquals(BmgmState.Success(true), results[1])
        }
    }

    @Test
    fun `when set check box state then return failed result`() {
        runTest {
            val cartIds = anyList<String>()
            val throwable = RuntimeException("message")
            val results = mutableListOf<BmgmState<Boolean>>()

            coEvery {
                setCartListCheckboxStateUseCase.invoke(cartIds)
            } throws throwable

            viewModel.setCheckListState.observeForever {
                results.add(it)
            }

            viewModel.setCartListCheckboxState(cartIds)

            coVerify {
                setCartListCheckboxStateUseCase.invoke(cartIds)
            }

            //expected loading state first
            assertEquals(BmgmState.Loading, results[0])

            //then set to error state
            viewModel.setCheckListState.verifyErrorEquals(BmgmState.Error(throwable))
        }
    }
}