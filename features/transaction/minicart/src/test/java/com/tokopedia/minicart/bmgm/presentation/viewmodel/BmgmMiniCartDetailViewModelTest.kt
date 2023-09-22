package com.tokopedia.minicart.bmgm.presentation.viewmodel

import com.tokopedia.minicart.bmgm.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by @ilhamsuaib on 29/08/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BmgmMiniCartDetailViewModelTest :
    BaseCartCheckboxViewModelTest<BmgmMiniCartDetailViewModel>() {

    @RelaxedMockK
    lateinit var miniCartLocalCacheUseCases: MiniCartLocalCacheUseCases

    override fun createViewModel(): BmgmMiniCartDetailViewModel {
        return BmgmMiniCartDetailViewModel(
            { miniCartLocalCacheUseCases },
            { setCartListCheckboxStateUseCase },
            { coroutineTestRule.dispatchers }
        )
    }

    @Test
    fun `get cart data then return success result`() {
        runTest {
            val cartData = mockCartData()
            var result: BmgmState<BmgmCommonDataModel> = BmgmState.None

            every {
                miniCartLocalCacheUseCases.getCartData()
            } returns cartData

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.cartData.collect {
                    result = it
                }
            }

            viewModel.getCartData()

            verify {
                miniCartLocalCacheUseCases.getCartData()
            }

            assertEquals(BmgmState.Success(cartData), result)

            job.cancel()
        }
    }

    @Test
    fun `get cart data then return failed result`() {
        runTest {
            val throwable = Throwable()
            var result: BmgmState<BmgmCommonDataModel> = BmgmState.None

            every {
                miniCartLocalCacheUseCases.getCartData()
            } throws throwable

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.cartData.collect {
                    result = it
                }
            }

            viewModel.getCartData()

            verify {
                miniCartLocalCacheUseCases.getCartData()
            }

            result.verifyErrorEquals(BmgmState.Error(throwable))

            job.cancel()
        }
    }

    private fun mockCartData(): BmgmCommonDataModel {
        return BmgmCommonDataModel()
    }
}