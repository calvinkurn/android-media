package com.tokopedia.buy_more_get_more.minicart.presentation.viewmodel

import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.GetMiniCartUseCase
import com.tokopedia.buy_more_get_more.minicart.domain.usecase.MiniCartLocalCacheUseCases
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Field

/**
 * Created by @ilhamsuaib on 28/08/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BmgmMiniCartViewModelTest : BaseCartCheckboxViewModelTest<BmgmMiniCartViewModel>() {

    @RelaxedMockK
    lateinit var localCacheUseCase: MiniCartLocalCacheUseCases

    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartUseCase

    private lateinit var privateMiniCartData: Field

    override fun initVariables() {
        super.initVariables()
        privateMiniCartData = viewModel::class.java.getDeclaredField("_cartData").apply {
            isAccessible = true
        }
    }

    override fun createViewModel(): BmgmMiniCartViewModel {
        return BmgmMiniCartViewModel(
            { setCartListCheckboxStateUseCase },
            { getMiniCartUseCase },
            { localCacheUseCase },
            { coroutineTestRule.dispatchers },
        )
    }

    @Test
    fun `get mini cart data with loading state then return success result`() {
        runTest {
            val showLoadingState = true
            getMiniCartSuccessTestSetup(showLoadingState) { sates, data ->
                assertEquals(BmgmState.Loading, sates[0])
                assertEquals(BmgmState.Success(data), sates[1])
            }
        }
    }

    @Test
    fun `get mini cart data without loading state then return success result`() {
        runTest {
            val showLoadingState = false
            getMiniCartSuccessTestSetup(showLoadingState) { _, data ->
                assertEquals(BmgmState.Success(data), viewModel.cartData.value)
            }
        }
    }

    @Test
    fun `get mini cart data with loading state then return failed result`() {
        runTest {
            val showLoadingState = true
            getMiniCartFailedTestSetup(showLoadingState) { sates, t ->
                assertEquals(BmgmState.Loading, sates[0])
                viewModel.cartData.value.verifyErrorEquals(BmgmState.Error(t))
            }
        }
    }

    @Test
    fun `get mini cart data without loading state then return failed result`() {
        runTest {
            val showLoadingState = false
            getMiniCartFailedTestSetup(showLoadingState) { _, t ->
                viewModel.cartData.value.verifyErrorEquals(BmgmState.Error(t))
            }
        }
    }

    @Test
    fun clearCartDataLocalCache() {
        runTest {
            viewModel.clearCartDataLocalCache()

            coVerify {
                localCacheUseCase.clearLocalCache()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when state is success then store cart data to local cache should be saved`() {
        runTest {
            val state = BmgmState.Success(getMiniCartDummyData())
            val shopId = anyLong()
            val warehouseId = anyLong()
            val offerEndData = anyString()
            val miniCartData =
                privateMiniCartData.get(viewModel) as MutableStateFlow<BmgmState<BmgmMiniCartDataUiModel>>
            miniCartData.value = state

            viewModel.saveCartDataToLocalStorage(shopId, warehouseId, offerEndData)

            coVerify {
                localCacheUseCase.saveToLocalCache(state.data, shopId, warehouseId, offerEndData)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when state is not success then store cart data to local cache shouldn't be saved`() {
        runTest {
            val data = getMiniCartDummyData()
            val state = BmgmState.Loading
            val shopId = anyLong()
            val warehouseId = anyLong()
            val offerEndData = anyString()
            val miniCartData =
                privateMiniCartData.get(viewModel) as MutableStateFlow<BmgmState<BmgmMiniCartDataUiModel>>
            miniCartData.value = state

            viewModel.saveCartDataToLocalStorage(shopId, warehouseId, offerEndData)

            coVerify(inverse = true) {
                localCacheUseCase.saveToLocalCache(data, shopId, warehouseId, offerEndData)
            }
        }
    }

    private fun getMiniCartDummyData() = BmgmMiniCartDataUiModel()

    private fun TestScope.getMiniCartSuccessTestSetup(
        showLoadingState: Boolean, assert: TestScope.(
            sates: MutableList<BmgmState<BmgmMiniCartDataUiModel>>, data: BmgmMiniCartDataUiModel
        ) -> Unit
    ) {
        val shopIds = anyList<Long>()
        val dummy = getMiniCartDummyData()
        val state = mutableListOf<BmgmState<BmgmMiniCartDataUiModel>>()
        val param = MiniCartParam(shopIds = shopIds)

        coEvery {
            getMiniCartUseCase.invoke(param)
        } returns dummy

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.cartData.toList(state)
        }

        viewModel.getMiniCartData(param, showLoadingState)

        coVerify {
            getMiniCartUseCase.invoke(param)
        }

        assert(state, dummy)

        job.cancel()
    }

    private fun TestScope.getMiniCartFailedTestSetup(
        showLoadingState: Boolean, assert: TestScope.(
            sates: MutableList<BmgmState<BmgmMiniCartDataUiModel>>, throwable: Throwable
        ) -> Unit
    ) {
        val shopIds = anyList<Long>()
        val throwable = Throwable()
        val state = mutableListOf<BmgmState<BmgmMiniCartDataUiModel>>()
        val param = MiniCartParam(shopIds = shopIds)

        coEvery {
            getMiniCartUseCase.invoke(param)
        } throws throwable

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.cartData.toList(state)
        }

        viewModel.getMiniCartData(param, showLoadingState)

        coVerify {
            getMiniCartUseCase.invoke(param)
        }

        assert(state, throwable)

        job.cancel()
    }
}
