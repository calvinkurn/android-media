package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import com.tokopedia.buy_more_get_more.minicart.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
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

            coEvery {
                miniCartLocalCacheUseCases.getCartData()
            } returns cartData

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.cartData.collect {
                    result = it
                }
            }

            viewModel.getCartData()

            coEvery {
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

            coEvery {
                miniCartLocalCacheUseCases.getCartData()
            } throws throwable

            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.cartData.collect {
                    result = it
                }
            }

            viewModel.getCartData()

            coEvery {
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