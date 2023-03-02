package com.tokopedia.tokofood.common

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodParam
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralAddToCartDataData
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartGeneralRemoveCartData
import com.tokopedia.tokofood.common.domain.response.CartGeneralUpdateCartQuantityData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataBottomSheet
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataCustomResponse
import com.tokopedia.tokofood.common.domain.usecase.AddToCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.MiniCartListTokofoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.RemoveCartTokofoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateCartTokoFoodUseCase
import com.tokopedia.tokofood.common.domain.usecase.UpdateQuantityTokofoodUseCase
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
    protected lateinit var loadCartTokoFoodUseCase: Lazy<MiniCartListTokofoodUseCase>

    @RelaxedMockK
    protected lateinit var addToCartTokoFoodUseCase: Lazy<AddToCartTokoFoodUseCase>

    @RelaxedMockK
    protected lateinit var updateCartTokoFoodUseCase: Lazy<UpdateCartTokoFoodUseCase>

    @RelaxedMockK
    protected lateinit var updateQuantityTokofoodUseCase: Lazy<UpdateQuantityTokofoodUseCase>

    @RelaxedMockK
    protected lateinit var removeCartTokoFoodUseCase: Lazy<RemoveCartTokofoodUseCase>

    protected lateinit var viewModel: MultipleFragmentsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MultipleFragmentsViewModel(
            savedStateHandle,
            loadCartTokoFoodUseCase,
            addToCartTokoFoodUseCase,
            updateCartTokoFoodUseCase,
            updateQuantityTokofoodUseCase,
            removeCartTokoFoodUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun cleanUp() {
        unmockkAll()
    }

    protected fun onLoadCartList_shouldReturn(response: CartGeneralCartListData?) {
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
                                           response: CartGeneralAddToCartDataData) {
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
                                            source: String,
                                            response: CartGeneralAddToCartDataData) {
        coEvery {
            updateCartTokoFoodUseCase.get().execute(updateParam, source)
        } returns response
    }

    protected fun onUpdateCart_shouldThrow(updateParam: UpdateParam,
                                           source: String,
                                           throwable: Throwable) {
        coEvery {
            updateCartTokoFoodUseCase.get().execute(updateParam, source)
        } throws throwable
    }

    protected fun onUpdateQuantity_shouldReturn(updateParam: UpdateQuantityTokofoodParam,
                                                response: CartGeneralUpdateCartQuantityData) {
        coEvery {
            updateQuantityTokofoodUseCase.get().execute(updateParam)
        } returns response
    }

    protected fun onUpdateQuantity_shouldThrow(updateParam: UpdateQuantityTokofoodParam,
                                               throwable: Throwable) {
        coEvery {
            updateQuantityTokofoodUseCase.get().execute(updateParam)
        } throws throwable
    }

    protected fun onRemoveCart_shouldReturn(removeCartParam: RemoveCartTokofoodParam,
                                            response: CartGeneralRemoveCartData) {
        coEvery {
            removeCartTokoFoodUseCase.get().execute(removeCartParam)
        } returns response
    }

    protected fun onRemoveCart_shouldThrow(removeCartParam: RemoveCartTokofoodParam,
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

    protected fun getSuccessUpdateCartResponse(): CartGeneralAddToCartDataData {
        return CartGeneralAddToCartDataData(
            businessData = listOf(
                CartListBusinessData(
                    businessId = TokoFoodCartUtil.getBusinessId()
                )
            )
        )
    }

    protected fun getSuccessRemoveCartResponse(): CartGeneralRemoveCartData {
        return CartGeneralRemoveCartData(
            success = TokoFoodCartUtil.SUCCESS_STATUS_INT
        )
    }

    protected fun getSuccessUpdateQuantityResponse(): CartGeneralUpdateCartQuantityData {
        return CartGeneralUpdateCartQuantityData(
            success = TokoFoodCartUtil.SUCCESS_STATUS_INT
        )
    }

    protected fun getPhoneVerificationAddToCartResponse(): CartGeneralAddToCartDataData {
        return CartGeneralAddToCartDataData(
            businessData = listOf(
                CartListBusinessData(
                    businessId = TokoFoodCartUtil.getBusinessId(),
                    customResponse = CartListBusinessDataCustomResponse(
                        bottomSheet = CartListBusinessDataBottomSheet(
                            isShowBottomSheet = true,
                            title = "Need phone verification"
                        )
                    )
                )
            )
        )
    }

    companion object {

        const val SOURCE = "source"

        const val PURCHASE_SUCCESS_MINI_CART_JSON = "json/purchase/purchase_success_mini_cart.json"
        const val PURCHASE_UNAVAILABLE_MINI_CART_JSON = "json/purchase/purchase_success_unavailable_products.json"

    }

}
