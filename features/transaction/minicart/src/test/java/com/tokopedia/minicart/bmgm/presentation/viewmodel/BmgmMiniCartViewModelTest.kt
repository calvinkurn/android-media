package com.tokopedia.minicart.bmgm.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.minicart.bmgm.domain.model.BmgmParamModel
import com.tokopedia.minicart.bmgm.domain.usecase.GetBmgmMiniCartDataUseCase
import com.tokopedia.minicart.bmgm.domain.usecase.LocalCacheUseCase
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmState
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Field

/**
 * Created by @ilhamsuaib on 28/08/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BmgmMiniCartViewModelTest : BaseCartCheckboxViewModelTest<BmgmMiniCartViewModel>() {

    @RelaxedMockK
    lateinit var getBmgmMiniCartDataUseCase: GetBmgmMiniCartDataUseCase

    @RelaxedMockK
    lateinit var localCacheUseCase: LocalCacheUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var param: BmgmParamModel
    private lateinit var privateMiniCartData: Field

    override fun initVariables() {
        super.initVariables()
        param = BmgmParamModel()
        privateMiniCartData = viewModel::class.java.getDeclaredField("_cartData").apply {
            isAccessible = true
        }
    }

    override fun createViewModel(): BmgmMiniCartViewModel {
        return BmgmMiniCartViewModel(
            { setCartListCheckboxStateUseCase },
            { getBmgmMiniCartDataUseCase },
            { localCacheUseCase },
            { userSession },
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

                coVerify {
                    localCacheUseCase.saveToLocalCache(data)
                }
            }
        }
    }

    @Test
    fun `get mini cart data without loading state then return success result`() {
        runTest {
            val showLoadingState = false
            getMiniCartSuccessTestSetup(showLoadingState) { _, data ->
                viewModel.cartData.verifySuccessEquals(BmgmState.Success(data))

                coVerify {
                    localCacheUseCase.saveToLocalCache(data)
                }
            }
        }
    }

    @Test
    fun `get mini cart data with loading state then return failed result`() {
        runTest {
            val showLoadingState = true
            getMiniCartFailedTestSetup(showLoadingState) { sates, t ->
                assertEquals(BmgmState.Loading, sates[0])
                viewModel.cartData.verifyErrorEquals(BmgmState.Error(t))

                coVerify(inverse = true) {
                    localCacheUseCase.saveToLocalCache(any())
                }
            }
        }
    }

    @Test
    fun `get mini cart data without loading state then return failed result`() {
        runTest {
            val showLoadingState = false
            getMiniCartFailedTestSetup(showLoadingState) { _, t ->
                viewModel.cartData.verifyErrorEquals(BmgmState.Error(t))

                coVerify(inverse = true) {
                    localCacheUseCase.saveToLocalCache(any())
                }
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
            val miniCartData =
                privateMiniCartData.get(viewModel) as MutableLiveData<BmgmState<BmgmMiniCartDataUiModel>>
            miniCartData.value = state

            viewModel.storeCartDataToLocalCache()

            coVerify {
                localCacheUseCase.saveToLocalCache(state.data)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when state is not success then store cart data to local cache shouldn't be saved`() {
        runTest {
            val data = getMiniCartDummyData()
            val state = BmgmState.Loading
            val miniCartData =
                privateMiniCartData.get(viewModel) as MutableLiveData<BmgmState<BmgmMiniCartDataUiModel>>
            miniCartData.value = state

            viewModel.storeCartDataToLocalCache()

            coVerify(inverse = true) {
                localCacheUseCase.saveToLocalCache(data)
            }
        }
    }

    private fun getMiniCartDummyData() = BmgmMiniCartDataUiModel()

    private fun TestScope.getMiniCartSuccessTestSetup(
        showLoadingState: Boolean, assert: TestScope.(
            sates: MutableList<BmgmState<BmgmMiniCartDataUiModel>>, data: BmgmMiniCartDataUiModel
        ) -> Unit
    ) {
        val shopId = anyString()
        val dummy = getMiniCartDummyData()
        val state = mutableListOf<BmgmState<BmgmMiniCartDataUiModel>>()

        coEvery {
            getBmgmMiniCartDataUseCase.invoke(shopId, param)
        } returns dummy

        viewModel.cartData.observeForever {
            state.add(it)
        }

        viewModel.getMiniCartData(param, showLoadingState)

        coVerify {
            getBmgmMiniCartDataUseCase.invoke(shopId, param)
        }

        assert(state, dummy)
    }

    private fun TestScope.getMiniCartFailedTestSetup(
        showLoadingState: Boolean, assert: TestScope.(
            sates: MutableList<BmgmState<BmgmMiniCartDataUiModel>>, throwable: Throwable
        ) -> Unit
    ) {
        val shopId = anyString()
        val throwable = Throwable()
        val state = mutableListOf<BmgmState<BmgmMiniCartDataUiModel>>()

        coEvery {
            getBmgmMiniCartDataUseCase.invoke(shopId, param)
        } throws throwable

        viewModel.cartData.observeForever {
            state.add(it)
        }

        viewModel.getMiniCartData(param, showLoadingState)

        coVerify {
            getBmgmMiniCartDataUseCase.invoke(shopId, param)
        }

        assert(state, throwable)
    }
}
