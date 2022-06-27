package com.tokopedia.tokofood.common

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.RemoveCartTokoFoodParam
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.MiniCartTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.UiEvent
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModelTest: MultipleFragmentsViewModelTestFixture() {

    @Test
    fun `when onSavedInstanceState, the data can be restored using onRestoreSavednstanceState`() {
        runBlocking {
            val cartListData = CheckoutTokoFood()
            onLoadCartList_shouldReturn(cartListData)

            viewModel.loadInitial(SOURCE)

            val expectedMiniCartUiModel =
                cartListData.data.getMiniCartUiModel()

            viewModel.onSavedInstanceState()
            viewModel.onRestoreSavedInstanceState()

            assertEquals(
                expectedMiniCartUiModel,
                (viewModel.miniCartFlow.value as? Result.Success)?.data
            )
        }
    }

    @Test
    fun `when loadInitial and no cart data yet, should load cart list`() {
        runBlocking {
            val cartListData = CheckoutTokoFood()
            onLoadCartList_shouldReturn(cartListData)

            viewModel.loadInitial(SOURCE)

            val expectedMiniCartUiModel =
                cartListData.data.getMiniCartUiModel()

            assertEquals(cartListData.data, viewModel.cartDataFlow.value)
            assertEquals(
                expectedMiniCartUiModel,
                (viewModel.miniCartFlow.value as? Result.Success)?.data
            )
        }
    }

    @Test
    fun `when loadInitial but cart data is already exist, should set mini cart value straight`() {
        runBlocking {
            val cartListData = CheckoutTokoFood()
            onLoadCartList_shouldReturn(cartListData)

            viewModel.loadInitial(SOURCE)
            viewModel.loadInitial(SOURCE)

            val expectedMiniCartUiModel =
                cartListData.data.getMiniCartUiModel()

            assertEquals(
                expectedMiniCartUiModel,
                (viewModel.miniCartFlow.value as? Result.Success)?.data
            )
        }
    }

    @Test
    fun `when loadCartList should set cart data`() {
        val cartListData = CheckoutTokoFood()
        viewModel.loadCartList(cartListData)

        assertEquals(cartListData.data, viewModel.cartDataFlow.value)
    }

    @Test
    fun `when deleteProduct, should set ui event state to success delete`() {
        runBlocking {
            val response = CartTokoFoodResponse(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = CartTokoFoodData(
                    success = TokoFoodCartUtil.SUCCESS_STATUS_INT
                )
            )
            val productId = "123"
            val cartId = "123"
            val shopId = ""
            val param = RemoveCartTokoFoodParam.getProductParamById(productId, cartId, shopId)
            onRemoveCart_shouldReturn(param, response)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteProduct(productId, cartId, SOURCE)
                },
                then = {
                    val expectedUiModelState = UiEvent.EVENT_SUCCESS_DELETE_PRODUCT
                    assertEquals(expectedUiModelState, it)
                }
            )
        }
    }

    @Test
    fun `when deleteProduct and should not refresh cart, should not call load cart`() {
        runBlocking {
            val response = CartTokoFoodResponse(
                status = TokoFoodCartUtil.SUCCESS_STATUS,
                data = CartTokoFoodData(
                    success = TokoFoodCartUtil.SUCCESS_STATUS_INT
                )
            )
            val productId = "123"
            val cartId = "123"
            val shopId = ""
            val param = RemoveCartTokoFoodParam.getProductParamById(productId, cartId, shopId)
            onRemoveCart_shouldReturn(param, response)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteProduct(productId, cartId, SOURCE, shouldRefreshCart = false)
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
            val productId = "123"
            val cartId = "123"
            val shopId = "123"
            val param = RemoveCartTokoFoodParam.getProductParamById(productId, cartId, shopId)
            onRemoveCart_shouldThrow(param, MessageErrorException(""))

            collectFromSharedFlow(
                whenAction = {
                    viewModel.deleteProduct(productId, cartId, SOURCE, shopId)
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
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val removeCartParam = RemoveCartTokoFoodParam(
                carts = successResponse.miniCartTokofood.data.getProductListFromCart().map {
                    it.mapToRemoveItemParam(successResponse.miniCartTokofood.data.shop.shopId)
                }
            )
            val response = CartTokoFoodResponse()
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
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val removeCartParam = RemoveCartTokoFoodParam(
                carts = successResponse.miniCartTokofood.data.getProductListFromCart().map {
                    it.mapToRemoveItemParam(successResponse.miniCartTokofood.data.shop.shopId)
                }
            )
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
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val removeCartParam = RemoveCartTokoFoodParam(
                carts = successResponse.miniCartTokofood.data.unavailableSections.firstOrNull()?.products?.map {
                    it.mapToRemoveItemParam(successResponse.miniCartTokofood.data.shop.shopId)
                }.orEmpty()
            )
            onRemoveCart_shouldReturn(removeCartParam, getSuccessUpdateCartResponse())

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
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val removeCartParam = RemoveCartTokoFoodParam(
                carts = successResponse.miniCartTokofood.data.unavailableSections.firstOrNull()?.products?.map {
                    it.mapToRemoveItemParam(successResponse.miniCartTokofood.data.shop.shopId)
                }.orEmpty()
            )
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
    fun `when updateNotes success, should set ui event state to success update notes`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateNotes(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_UPDATE_NOTES
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateNotes and should not refresh cart, should not call load cart`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

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
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldThrow(updateParam, MessageErrorException(""))

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
    fun `when updateQuantity success, should set ui event state to success update quantity`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateQuantity(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_UPDATE_QUANTITY
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateQuantity but should not refresh cart, should not load cart list`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

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
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldThrow(updateParam, MessageErrorException(""))

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
    fun `when updateCart success, should set ui event state to success update cart`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.updateCart(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_UPDATE_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

    @Test
    fun `when updateCart failed, should set ui event state to failed update cart`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onUpdateCart_shouldThrow(updateParam, MessageErrorException(""))

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
    fun `when addToCart success, should set ui event state to success add to cart`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

            val updateParam = UpdateParam()
            onAddToCart_shouldReturn(updateParam, getSuccessUpdateCartResponse())

            viewModel.loadInitial(SOURCE)

            collectFromSharedFlow(
                whenAction = {
                    viewModel.addToCart(updateParam, SOURCE)
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_ADD_TO_CART
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }


    @Test
    fun `when addToCart failed, should set ui event state to failed add to cart`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<MiniCartTokoFoodResponse>(
                    PURCHASE_SUCCESS_MINI_CART_JSON
                )
            onLoadCartList_shouldReturn(successResponse.miniCartTokofood)

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
                    viewModel.clickMiniCart()
                },
                then = {
                    val expectedUiEventState = UiEvent.EVENT_SUCCESS_VALIDATE_CHECKOUT
                    assertEquals(expectedUiEventState, it)
                }
            )
        }
    }

}