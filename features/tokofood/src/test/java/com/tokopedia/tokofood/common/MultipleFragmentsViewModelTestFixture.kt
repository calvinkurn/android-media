package com.tokopedia.tokofood.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodBottomSheet
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.usecase.AddToCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.LoadCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Rule

@FlowPreview
@ExperimentalCoroutinesApi
abstract class MultipleFragmentsViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    protected lateinit var savedStateHandle: SavedStateHandle

    @RelaxedMockK
    protected lateinit var loadCartTokoFoodUseCase: Lazy<LoadCartTokoFoodUseCase>

    @RelaxedMockK
    protected lateinit var addToCartTokoFoodUseCase: Lazy<AddToCartTokoFoodUseCase>

    @RelaxedMockK
    protected lateinit var updateCartTokoFoodUseCase: Lazy<UpdateCartTokoFoodUseCase>

    @RelaxedMockK
    protected lateinit var removeCartTokoFoodUseCase: Lazy<RemoveCartTokoFoodUseCase>

    protected lateinit var viewModel: MultipleFragmentsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MultipleFragmentsViewModel(
            savedStateHandle,
            loadCartTokoFoodUseCase,
            addToCartTokoFoodUseCase,
            updateCartTokoFoodUseCase,
            removeCartTokoFoodUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    protected fun onLoadCartList_shouldReturn(response: CheckoutTokoFood?) {
        coEvery {
            loadCartTokoFoodUseCase.get().execute(SOURCE)
        } returns response
    }

    protected fun onLoadCartList_shouldThrow(throwable: Throwable) {
        coEvery {
            loadCartTokoFoodUseCase.get().execute(SOURCE)
        } throws throwable
    }

    protected fun onAddToCart_shouldReturn(updateParam: UpdateParam,
                                           response: CartTokoFoodResponse) {
        coEvery {
            addToCartTokoFoodUseCase.get().execute(updateParam)
        } returns response
    }

    protected fun onAddToCart_shouldThrow(updateParam: UpdateParam,
                                          throwable: Throwable) {
        coEvery {
            addToCartTokoFoodUseCase.get().execute(updateParam)
        } throws throwable
    }

    protected fun onUpdateCart_shouldReturn(updateParam: UpdateParam,
                                            response: CartTokoFoodResponse) {
        coEvery {
            updateCartTokoFoodUseCase.get().execute(updateParam)
        } returns response
    }

    protected fun onUpdateCart_shouldThrow(updateParam: UpdateParam,
                                           throwable: Throwable) {
        coEvery {
            updateCartTokoFoodUseCase.get().execute(updateParam)
        } throws throwable
    }

    protected fun onRemoveCart_shouldReturn(removeCartParam: RemoveCartTokoFoodParam,
                                            response: CartTokoFoodResponse) {
        coEvery {
            removeCartTokoFoodUseCase.get().execute(removeCartParam)
        } returns response
    }

    protected fun onRemoveCart_shouldThrow(removeCartParam: RemoveCartTokoFoodParam,
                                            throwable: Throwable) {
        coEvery {
            removeCartTokoFoodUseCase.get().execute(removeCartParam)
        } throws throwable
    }

    protected fun collectFromSharedFlow(whenAction: () -> Unit,
                                        then: (Int?) -> Unit) {
        val testCoroutineScope = TestCoroutineScope().apply {
            pauseDispatcher()
        }
        var actualUiModelState: Int? = null
        val job = testCoroutineScope.launch {
            viewModel.cartDataValidationFlow.collect {
                actualUiModelState = it.state
            }
        }
        testCoroutineScope.runCurrent()
        whenAction()
        testCoroutineScope.runCurrent()
        then(actualUiModelState)
        job.cancel()
    }

    protected fun getSuccessUpdateCartResponse(): CartTokoFoodResponse {
        return CartTokoFoodResponse(
            status = TokoFoodCartUtil.SUCCESS_STATUS,
            data = CartTokoFoodData(
                success = TokoFoodCartUtil.SUCCESS_STATUS_INT
            )
        )
    }

    protected fun getPhoneVerificationAddToCartResponse(): CartTokoFoodResponse {
        return CartTokoFoodResponse(
            status = TokoFoodCartUtil.SUCCESS_STATUS,
            data = CartTokoFoodData(
                success = TokoFoodCartUtil.SUCCESS_STATUS_INT,
                bottomSheet = CartTokoFoodBottomSheet(
                    isShowBottomSheet = true,
                    title = "Need phone verification"
                )
            )
        )
    }

    companion object {

        const val SOURCE = "source"

        const val PURCHASE_SUCCESS_MINI_CART_JSON = "json/purchase/purchase_success_mini_cart.json"

    }

}
