package com.tokopedia.tokofood.common

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokofoodParam
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartList
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataCustomResponse
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataShop
import com.tokopedia.tokofood.common.domain.response.CartListData
import com.tokopedia.tokofood.common.domain.response.CartListTokofoodResponse
import com.tokopedia.tokofood.common.minicartwidget.view.MiniCartUiModel
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModelTest: MultipleFragmentsViewModelTestFixture() {

    @Test
    fun `when onSavedInstanceState, the data can be restored using onRestoreSavednstanceState`() {
        runBlocking {
            val cartListData = CartGeneralCartListData()
            onLoadCartList_shouldReturn(cartListData)

            viewModel.loadInitial(SOURCE)

            val expectedMiniCartUiModel =
                cartListData.getMiniCartUiModel()

            viewModel.onSavedInstanceState()
            viewModel.onRestoreSavedInstanceState()

            assertEquals(
                expectedMiniCartUiModel,
                (viewModel.miniCartFlow.value as? Result.Success)?.data
            )
        }
    }

    @Test
    fun `when onSavedInstanceState, but cartListData is failure, the data cannot be saved but can be restorred as success`() {
        runBlocking {
            onLoadCartList_shouldThrow(MessageErrorException())

            viewModel.loadInitial(SOURCE)

            viewModel.onSavedInstanceState()
            viewModel.onRestoreSavedInstanceState()

            assert(
                viewModel.miniCartFlow.value is Result.Success
            )
        }
    }

    @Test
    fun `when onRestoreSavednstanceState, should get the mini cart ui state`() {
        runBlocking {
            val miniCartUiModel =
                MiniCartUiModel(
                    "shop test name"
                )
            coEvery<Any?> {
                savedStateHandle.get(anyString())
            } returns miniCartUiModel

            viewModel.onRestoreSavedInstanceState()

            assert(
                (viewModel.miniCartFlow.value as? Result.Success)?.data is MiniCartUiModel
            )
        }
    }

    @Test
    fun `when onRestoreSavednstanceState but saved state handle store wrong data, should get default mini cart ui state`() {
        runBlocking {
            coEvery<Any?> {
                savedStateHandle.get(anyString())
            } returns String.EMPTY

            viewModel.onRestoreSavedInstanceState()

            assert(
                (viewModel.miniCartFlow.value as? Result.Success)?.data?.shopName?.isEmpty() == true
            )
        }
    }

    @Test
    fun `when loadInitial and no cart data yet, should load cart list`() {
        runBlocking {
            val cartListData = CartGeneralCartListData()
            onLoadCartList_shouldReturn(cartListData)

            viewModel.loadInitial(SOURCE)

            val expectedMiniCartUiModel =
                cartListData.getMiniCartUiModel()

            assertEquals(cartListData, viewModel.cartDataFlow.value)
            assertEquals(
                expectedMiniCartUiModel,
                (viewModel.miniCartFlow.value as? Result.Success)?.data
            )
        }
    }

    @Test
    fun `when loadInitial but cart data is null, should not set mini cart value`() {
        runBlocking {
            onLoadCartList_shouldReturn(null)

            val expectedMiniCartUiPrice = String.EMPTY

            viewModel.loadInitial(SOURCE)

            assertEquals(
                expectedMiniCartUiPrice,
                (viewModel.miniCartFlow.value as? Result.Success)?.data?.totalPriceFmt
            )
        }
    }

    @Test
    fun `when loadInitial but cart data is already exist, should set mini cart value straight`() {
        runBlocking {
            val cartListData = CartGeneralCartListData()
            onLoadCartList_shouldReturn(cartListData)

            viewModel.loadInitial(SOURCE)
            viewModel.loadInitial(SOURCE)

            val expectedMiniCartUiModel =
                cartListData.getMiniCartUiModel()

            assertEquals(
                expectedMiniCartUiModel,
                (viewModel.miniCartFlow.value as? Result.Success)?.data
            )
        }
    }

    @Test
    fun `when loadCartList should set cart data`() {
        val shopId = "123"
        val cartListData = CartGeneralCartListData(
            success = 1,
            data = CartListData(
                businessData = listOf(
                    CartListBusinessData(
                        businessId = TokoFoodCartUtil.getBusinessId(),
                        customResponse = CartListBusinessDataCustomResponse(
                            shop = CartListBusinessDataShop(
                                shopId = shopId
                            )
                        )
                    )
                )
            )
        )
        viewModel.loadCartList(cartListData)

        assertEquals(cartListData, viewModel.cartDataFlow.value)
        assertEquals(shopId, viewModel.shopId)
        assert(viewModel.hasCartUpdatedIntoLatestState.value == true)
    }

    @Test
    fun `when loadCartList null, should set cart data null`() {
        viewModel.loadCartList(null)

        assertEquals(null, viewModel.cartDataFlow.value)
        assertEquals(true, viewModel.hasCartUpdatedIntoLatestState.value)
        assertEquals(String.EMPTY, viewModel.shopId)
        assertEquals(UiEvent.EVENT_FAILED_LOAD_CART, viewModel.cartDataValidationFlow.value.state)
    }

    @Test
    fun `when deleteProduct and should refresh cart, should load cart`() {
        runBlocking {
            val response = getSuccessRemoveCartResponse()
            val cartId = "123"
            val param = RemoveCartTokofoodParam.getProductParamById(cartId)
            onRemoveCart_shouldReturn(param, response)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteProduct(cartId, SOURCE, true)
                },
                then = {
                    coVerify(exactly = 1) {
                        loadCartTokoFoodUseCase.get()
                    }
                }
            )
        }
    }

    @Test
    fun `when deleteProduct and should not refresh cart, should not call load cart`() {
        runBlocking {
            val response = getSuccessRemoveCartResponse()
            val cartId = "123"
            val param = RemoveCartTokofoodParam.getProductParamById(cartId)
            onRemoveCart_shouldReturn(param, response)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteProduct(cartId, SOURCE, false)
                },
                then = {
                    coVerify(exactly = 0) {
                        loadCartTokoFoodUseCase.get()
                    }
                    val expectedUiModelState = UiEvent.EVENT_SUCCESS_DELETE_PRODUCT
                    assertEquals(expectedUiModelState, it)
                }
            )
        }
    }

    @Test
    fun `when deleteProduct and failed, should set ui event state to failed delete`() {
        runBlocking {
            val cartId = "123"
            val param = RemoveCartTokofoodParam.getProductParamById(cartId)
            onRemoveCart_shouldThrow(param, MessageErrorException(""))

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteProduct(cartId, SOURCE)
                },
                then = {
                    val expectedUiModelState = UiEvent.EVENT_FAILED_DELETE_PRODUCT
                    assertEquals(expectedUiModelState, it)
                }
            )
        }
    }

    @Test
    fun `when deleteAllAtcAndAddProduct, should run add to cart afterwards`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.cartGeneralCartList.data)

            val removeCartParam = successResponse.cartGeneralCartList.data.getRemoveAllCartParam()
            val response = getSuccessRemoveCartResponse()
            val updateParam = UpdateParam()
            onRemoveCart_shouldReturn(removeCartParam, response)

            viewModel.loadInitial(SOURCE)

            viewModel.deleteAllAtcAndAddProduct(updateParam, SOURCE)

            coVerify {
                addToCartTokoFoodUseCase.get()
            }
        }
    }

    @Test
    fun `when deleteAllAtcAndAddProduct but no cart data, should do nothing`() {
        runBlocking {
            viewModel.deleteAllAtcAndAddProduct(UpdateParam(), SOURCE)

            coVerify(exactly = 0) {
                removeCartTokoFoodUseCase.get()
            }
        }
    }

    @Test
    fun `when deleteAllAtcAndAddProduct and delete is error, should set ui event to failed delete`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val removeCartParam = successResponse.data.getRemoveAllCartParam()
            val updateParam = UpdateParam()
            onRemoveCart_shouldThrow(removeCartParam, MessageErrorException(""))

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteAllAtcAndAddProduct(updateParam, SOURCE)
                },
                then = {
                    coVerify(exactly = 0) {
                        addToCartTokoFoodUseCase.get()
                    }
                    val expectedUiEventState = UiEvent.EVENT_FAILED_DELETE_PRODUCT
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when deleteUnavailableProducts success, should set ui event to success delete`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_UNAVAILABLE_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val removeCartParam = successResponse.data.getRemoveUnavailableCartParam()
            onRemoveCart_shouldReturn(removeCartParam, getSuccessRemoveCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteUnavailableProducts()
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_DELETE_UNAVAILABLE_PRODUCTS
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when deleteUnavailableProducts but no cart data, should do nothing`() {
        runBlocking {
            viewModel.deleteUnavailableProducts()

            coVerify(exactly = 0) {
                removeCartTokoFoodUseCase.get()
            }
        }
    }

    @Test
    fun `when deleteUnavailableProducts failed, should set ui event to failed delete`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_UNAVAILABLE_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val removeCartParam = successResponse.data.getRemoveUnavailableCartParam()
            onRemoveCart_shouldThrow(removeCartParam, MessageErrorException(""))

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteUnavailableProducts()
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_FAILED_DELETE_PRODUCT
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateNotes success, should load cart`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, SOURCE, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateNotes(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateNotes and should not refresh cart, should not call load cart`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CartGeneralCartList>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, SOURCE, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateNotes(updateParam, SOURCE, false)
                },
                then = {
                    coVerify(exactly = 1) {
                        loadCartTokoFoodUseCase.get()
                    }
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_UPDATE_NOTES
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateNotes failed, should set ui event state to failed update notes`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CartGeneralCartList>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onUpdateCart_shouldThrow(updateParam, SOURCE, MessageErrorException(""))

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateNotes(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_FAILED_UPDATE_NOTES
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity success, should loadCart`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateQuantityTokofoodParam()
            onUpdateQuantity_shouldReturn(updateParam, getSuccessUpdateQuantityResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity but should not refresh cart, should not load cart list`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CartGeneralCartList>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateQuantityTokofoodParam()
            onUpdateQuantity_shouldReturn(updateParam, getSuccessUpdateQuantityResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity(updateParam, SOURCE, false)
                },
                then = {
                    coVerify(exactly = 1) {
                        loadCartTokoFoodUseCase.get()
                    }
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity failed, should set ui event state to failed update quantity`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CartGeneralCartList>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateQuantityTokofoodParam()
            onUpdateQuantity_shouldThrow(updateParam, MessageErrorException(""))

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_FAILED_UPDATE_QUANTITY
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateCart success, should load cart`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, SOURCE, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateCart(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateCart failed, should set ui event state to failed update cart`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onUpdateCart_shouldThrow(updateParam, SOURCE, MessageErrorException(""))

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateCart(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_FAILED_UPDATE_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when addToCart success, should load mini cart`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onAddToCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)
            viewModel.addToCart(updateParam, SOURCE)
            val expectedUiEventState = UiEvent.EVENT_SUCCESS_LOAD_CART
            assertEquals(expectedUiEventState, viewModel.cartDataValidationFlow.value.state)
        }
    }

    @Test
    fun `when addToCart success but should show bottomsheet, should send phone verification event`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onAddToCart_shouldReturn(updateParam, getPhoneVerificationAddToCartResponse())

            viewModel.loadInitial(SOURCE)
            viewModel.addToCart(updateParam, SOURCE)
            val expectedUiEventState = UiEvent.EVENT_PHONE_VERIFICATION
            assertEquals(expectedUiEventState, viewModel.cartDataValidationFlow.value.state)
        }
    }

    @Test
    fun `when addToCart failed, should set ui event state to failed add to cart`() {
        runBlocking {
            val rawSuccessResponse =
                JsonResourcesUtil.createSuccessResponse<CartListTokofoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                ).cartGeneralCartList
            val successResponse = rawSuccessResponse.copy(
                data = rawSuccessResponse.data.copy(
                    data = rawSuccessResponse.data.data.copy(
                        businessData = rawSuccessResponse.data.data.businessData.map {
                            it.copy(businessId = TokoFoodCartUtil.getBusinessId())
                        }
                    )
                )
            )
            onLoadCartList_shouldReturn(successResponse.data)

            val updateParam = UpdateParam()
            onAddToCart_shouldThrow(updateParam, MessageErrorException(""))

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.addToCart(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_FAILED_ADD_TO_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when clickMiniCart, should set ui event to success validate checkout`() {
        runBlocking {
            collectFromSharedFlow(
                whenAction = {
                    viewModel.clickMiniCart(SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

}
