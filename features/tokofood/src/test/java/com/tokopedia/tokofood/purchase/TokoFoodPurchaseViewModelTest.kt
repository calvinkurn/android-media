package com.tokopedia.tokofood.purchase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokofood.common.domain.metadata.CartMetadataTokoFoodWithVariant
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData
import com.tokopedia.tokofood.common.domain.response.CartTokoFood
import com.tokopedia.tokofood.common.domain.response.CartTokoFoodData
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.CheckoutGeneralTokoFoodResponse
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.PurchaseUiEvent
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getProductById
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper.TokoFoodPurchaseUiModelMapper
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseFragmentUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseProductTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchasePromoTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseShippingTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.utils.JsonResourcesUtil
import com.tokopedia.tokofood.utils.collectFromSharedFlow
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

@FlowPreview
@ExperimentalCoroutinesApi
class TokoFoodPurchaseViewModelTest : TokoFoodPurchaseViewModelTestFixture() {

    @Test
    fun `when resetValues, should reset to initial state`() {
        runBlocking {
            viewModel.shouldRefreshCartData.collectFromSharedFlow(
                whenAction = {
                    viewModel.resetValues()
                },
                then = {
                    assertEquals(viewModel.updateQuantityStateFlow.value, null)
                    assertEquals(false, it)
                }
            )
        }
    }

    @Test
    fun `when getNextItems, should return next item`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            val expectedIndex = 5
            val expectedCount = 2
            val expectedNextItems = viewModel.visitables.value?.subList(expectedIndex + 1, expectedIndex + expectedCount + 1)

            val actualNextItems = viewModel.getNextItems(expectedIndex, expectedCount)

            assertEquals(expectedNextItems, actualNextItems)
        }
    }

    @Test
    fun `when getNextItems but count is too big, should return empty list`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            val expectedIndex = 5
            val expectedCount = 200
            val expectedNextItems = listOf<Visitable<*>>()

            val actualNextItems = viewModel.getNextItems(expectedIndex, expectedCount)

            assertEquals(expectedNextItems, actualNextItems)
        }
    }

    @Test
    fun `when getNextItems but no visitables yet, should return empty list`() {
        val expectedIndex = 5
        val expectedCount = 2
        val expectedNextItems = listOf<Visitable<*>>()

        val actualNextItems = viewModel.getNextItems(expectedIndex, expectedCount)

        assertEquals(expectedNextItems, actualNextItems)
    }

    @Test
    fun `when loadData success should emit tracker load checkout data`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val isHasPinpoint = true

            viewModel.trackerLoadCheckoutData.collectFromSharedFlow(
                whenAction = {
                    viewModel.setIsHasPinpoint("123", isHasPinpoint)
                    viewModel.loadData()
                },
                then = {
                    assertEquals(successResponse.cartListTokofood.data, it)
                }
            )
        }
    }

    @Test
    fun `when loadData success and pinpoint has been set, should show success state`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val isHasPinpoint = true

            viewModel.setIsHasPinpoint("123", isHasPinpoint)
            viewModel.loadData()

            val expectedFragmentUiModel =
                TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(successResponse.cartListTokofood.data.shop)
            val expectedVisitablesCount =
                TokoFoodPurchaseUiModelMapper.mapCheckoutResponseToUiModels(
                    successResponse.cartListTokofood,
                    successResponse.cartListTokofood.isEnabled(),
                    !isHasPinpoint
                ).size
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assertEquals(expectedVisitablesCount, viewModel.visitables.value?.size)
        }
    }

    @Test
    fun `when loadData success but pinpoint hasn't been set and cacheAddressId has no pinpoint, should show no pinpoint state`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val isHasPinpoint = false

            viewModel.setIsHasPinpoint("123", isHasPinpoint)
            viewModel.loadData()

            val expectedUiEventState = PurchaseUiEvent.EVENT_NO_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData success but pinpoint hasn't been set and cacheAddressId is empty, should check pinpoint remotely`() {
        runBlocking {
            val addressId = "123"
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            userAddress = successResponse.cartListTokofood.data.userAddress.copy(
                                addressId = addressId
                            )
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)
            val isHasPinpoint = false
            onGetAddressUseCase_thenReturn(addressId, KeroAddressParamData(secondAddress = "123,123"))

            viewModel.setIsHasPinpoint("", isHasPinpoint)
            viewModel.loadData()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData success but pinpoint hasn't been set and remote second address is empty, should show no pinpoint state`() {
        runBlocking {
            val addressId = "123"
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            userAddress = successResponse.cartListTokofood.data.userAddress.copy(
                                addressId = addressId
                            )
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)
            val isHasPinpoint = false
            onGetAddressUseCase_thenReturn(addressId, KeroAddressParamData(secondAddress = ""))

            viewModel.setIsHasPinpoint("", isHasPinpoint)
            viewModel.loadData()

            val expectedUiEventState = PurchaseUiEvent.EVENT_NO_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData success but pinpoint hasn't been set and get address response is null, should show no pinpoint state`() {
        runBlocking {
            val addressId = "456"
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            userAddress = successResponse.cartListTokofood.data.userAddress.copy(
                                addressId = addressId
                            )
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)
            val isHasPinpoint = false
            onGetAddressUseCase_thenReturn(addressId, null)

            viewModel.setIsHasPinpoint("", isHasPinpoint)
            viewModel.loadData()

            val expectedUiEventState = PurchaseUiEvent.EVENT_NO_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadData twice and first checkout toaster is promo type, should set isPreviousPopupPromo to true`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()
            viewModel.loadData()

            val expectedFlag = true
            assertEquals(expectedFlag, (viewModel.purchaseUiEvent.value?.data as? Pair<*,*>)?.second)

        }
    }

    @Test
    fun `when loadData twice and first checkout toaster is not a promo type, should set isPreviousPopupPromo to false`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            popupMessageType = "not promo type"
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()
            viewModel.loadData()

            val expectedFlag = false
            assertEquals(expectedFlag, (viewModel.purchaseUiEvent.value?.data as? Pair<*,*>)?.second)
        }
    }

    @Test
    fun `when loadData twice and first checkout toaster is not a promo type and no pinpoint in second time, should set isPreviousPopupPromo to false`() {
        runBlocking {
            val addressId = "999"
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            popupMessageType = "not promo type",
                            userAddress = successResponse.cartListTokofood.data.userAddress.copy(
                                addressId = addressId
                            )
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)
            onGetAddressUseCase_thenReturn(addressId, KeroAddressParamData(secondAddress = "123,456"))

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()
            viewModel.setIsHasPinpoint("", false)
            viewModel.loadData()

            val expectedFlag = false
            assertEquals(expectedFlag, (viewModel.purchaseUiEvent.value?.data as? Pair<*,*>)?.second)
        }
    }

    @Test
    fun `when loadData twice and first checkout toaster is a promo type and no pinpoint in second time, should set isPreviousPopupPromo to false`() {
        runBlocking {
            val addressId = "999"
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            popupMessageType = "promo",
                            userAddress = successResponse.cartListTokofood.data.userAddress.copy(
                                addressId = addressId
                            )
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)
            onGetAddressUseCase_thenReturn(addressId, KeroAddressParamData(secondAddress = "123,456"))

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()
            viewModel.setIsHasPinpoint("", false)
            viewModel.loadData()

            val expectedFlag = true
            assertEquals(expectedFlag, (viewModel.purchaseUiEvent.value?.data as? Pair<*,*>)?.second)
        }
    }

    @Test
    fun `when loadData success but data is not enabled, should set visitables to be disabled`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_DISABLED_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()

            assert(
                viewModel.visitables.value?.any {
                    it is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel || it is TokoFoodPurchasePromoTokoFoodPurchaseUiModel
                } == false
            )
        }
    }

    @Test
    fun `when loadData fails and pinpoint has been set, should show failed state`() {
        runBlocking {
            val throwable = MessageErrorException("")
            onGetCheckoutTokoFood_thenThrow(throwable)

            viewModel.setIsHasPinpoint("123", true)

            val expectedUiEvent = PurchaseUiEvent(
                state = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE,
                throwable = throwable
            )
            val expectedFragmentUiModel = TokoFoodPurchaseFragmentUiModel(
                isLastLoadStateSuccess = false,
                shopName = "",
                shopLocation = ""
            )

            viewModel.loadData()

            assertEquals(expectedUiEvent.state, viewModel.purchaseUiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
        }
    }

    @Test
    fun `when loadData fails and pinpoint hasn't been set, should show no pinpoint state`() {
        runBlocking {
            val throwable = MessageErrorException("")
            onGetCheckoutTokoFood_thenThrow(throwable)

            viewModel.setIsHasPinpoint("123", false)

            val expectedUiEvent = PurchaseUiEvent(state = PurchaseUiEvent.EVENT_NO_PINPOINT)

            viewModel.loadData()

            assertEquals(expectedUiEvent.state, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadDataPartial success and pinpoint has been set, should show success state`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadDataPartial()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE
            val expectedFragmentUiModel =
                TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(successResponse.cartListTokofood.data.shop)

            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
        }
    }

    @Test
    fun `when loadDataPartial success but pinpoint hasn't been set, should show success state`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", false)
            viewModel.loadDataPartial()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_LOAD_PURCHASE_PAGE
            val expectedFragmentUiModel =
                TokoFoodPurchaseUiModelMapper.mapShopInfoToUiModel(successResponse.cartListTokofood.data.shop)

            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
            assertEquals(expectedFragmentUiModel, viewModel.fragmentUiModel.value)
            assert(
                viewModel.visitables.value?.any { it is TokoFoodPurchaseShippingTokoFoodPurchaseUiModel && it.isNeedPinpoint } == true
            )
        }
    }

    @Test
    fun `when loadDataPartial success but empty product, should send empty product event`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_EMPTY_PRODUCT_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadDataPartial()

            val expectedUiEventState = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadDataPartial after first loadData and first checkout toaster is promo type, should set isPreviousPopupPromo to true`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()
            viewModel.loadDataPartial()

            val expectedFlag = true
            assertEquals(expectedFlag, (viewModel.purchaseUiEvent.value?.data as? Pair<*,*>)?.second)
        }
    }

    @Test
    fun `when loadDataPartial after first loadData and first checkout toaster is not promo type, should set isPreviousPopupPromo to false`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val updatedSuccessResponse =
                successResponse.copy(
                    cartListTokofood = successResponse.cartListTokofood.copy(
                        data = successResponse.cartListTokofood.data.copy(
                            popupMessageType = "not promo type"
                        )
                    )
                )
            onGetCheckoutTokoFood_thenReturn(updatedSuccessResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadData()
            viewModel.loadDataPartial()

            val expectedFlag = false
            assertEquals(expectedFlag, (viewModel.purchaseUiEvent.value?.data as? Pair<*,*>)?.second)
        }
    }

    @Test
    fun `when loadDataPartial success but data is not enabled, should set visitables to be disabled`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_DISABLED_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("", true)
            viewModel.loadDataPartial()

            assert(
                viewModel.visitables.value?.any {
                    it is TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel || it is TokoFoodPurchasePromoTokoFoodPurchaseUiModel
                } == false
            )
        }
    }

    @Test
    fun `when loadDataPartial fails and pinpoint has been set, should show partial failed state`() {
        runBlocking {
            val throwable = MessageErrorException("")
            onGetCheckoutTokoFood_thenThrow(throwable)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadDataPartial()

            val expectedUiEventState = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE_PARTIAL

            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadDataPartial fails and visitables aren't null, should show partial failed state`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            val throwable = MessageErrorException("")
            onGetCheckoutTokoFood_thenThrow(throwable)
            viewModel.loadDataPartial()

            val expectedUiEventState = PurchaseUiEvent.EVENT_FAILED_LOAD_PURCHASE_PAGE_PARTIAL

            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when loadDataPartial fails and pinpoint hasn't been set, should show partial failed state`() {
        runBlocking {
            val throwable = MessageErrorException("")
            onGetCheckoutTokoFood_thenThrow(throwable)

            viewModel.setIsHasPinpoint("123", false)
            viewModel.loadDataPartial()

            val expectedUiEventState = PurchaseUiEvent.EVENT_NO_PINPOINT

            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when deleteProduct, should delete the particular product from visitables`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val deletedProductId: String
            val deletedCartId: String
            successResponse.cartListTokofood.data.availableSection.products.getOrNull(0)
                .let { deletedProduct ->
                    deletedProductId = deletedProduct?.productId.orEmpty()
                    deletedCartId = deletedProduct?.cartId.orEmpty()
                }

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()
            viewModel.deleteProduct(deletedProductId, deletedCartId)

            assert(
                viewModel.visitables.value?.any {
                    (it as? TokoFoodPurchaseProductTokoFoodPurchaseUiModel)?.let { product ->
                        product.id == deletedProductId && product.cartId == deletedCartId
                    } ?: false
                } == false
            )
        }
    }

    @Test
    fun `when deleteProduct and no remaining products, should set ui event into empty`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_SINGLE_PRODUCT_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val deletedProductId: String
            val deletedCartId: String
            successResponse.cartListTokofood.data.availableSection.products.getOrNull(0)
                .let { deletedProduct ->
                    deletedProductId = deletedProduct?.productId.orEmpty()
                    deletedCartId = deletedProduct?.cartId.orEmpty()
                }

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()
            viewModel.deleteProduct(deletedProductId, deletedCartId)

            val expectedState = PurchaseUiEvent.EVENT_EMPTY_PRODUCTS

            assertEquals(expectedState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when deleteProduct, have single product and errorsUnblocking, should remove the ticker`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val deletedProductId: String
            val deletedCartId: String
            successResponse.cartListTokofood.data.availableSection.products.getOrNull(0)
                .let { deletedProduct ->
                    deletedProductId = deletedProduct?.productId.orEmpty()
                    deletedCartId = deletedProduct?.cartId.orEmpty()
                }

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()
            viewModel.deleteProduct(deletedProductId, deletedCartId)

            assert(viewModel.visitables.value?.any {
                it is TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel
            } == false)
        }
    }

    @Test
    fun `when deleteProduct but no product associated, should not do anything`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            val deletedProductId = "ga ada id-nya"
            val deletedCartId = "ga ada id-nya"

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()
            viewModel.deleteProduct(deletedProductId, deletedCartId)

            val expectedTotalProducts =
                successResponse.cartListTokofood.data.availableSection.products.size +
                        successResponse.cartListTokofood.data.unavailableSections.firstOrNull()?.products?.size.orZero()
            assertEquals(
                expectedTotalProducts,
                viewModel.visitables.value?.count { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel }
                    .orZero()
            )
        }
    }

    @Test
    fun `when deleteProduct but product is empty, should not do anything`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_EMPTY_PRODUCT_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()
            viewModel.deleteProduct(anyString(), anyString())

            val expectedTotalProducts =
                successResponse.cartListTokofood.data.availableSection.products.size +
                        successResponse.cartListTokofood.data.unavailableSections.firstOrNull()?.products?.size.orZero()
            assertEquals(
                expectedTotalProducts,
                viewModel.visitables.value?.count { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel }
                    .orZero()
            )
        }
    }

    @Test
    fun `when validateBulkDelete should show not collapsed unavailable product count`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.validateBulkDelete()

        val expectedUiEventState = PurchaseUiEvent.EVENT_SHOW_BULK_DELETE_CONFIRMATION_DIALOG
        val expectedUnavailableProductCount = successResponse.cartListTokofood.data.unavailableSections.firstOrNull()?.products?.size.orZero()
        assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        assertEquals(expectedUnavailableProductCount, viewModel.purchaseUiEvent.value?.data as? Int)
    }

    @Test
    fun `when bulkDeleteUnavailableProducts should remove all unavailable product section`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.bulkDeleteUnavailableProducts()

        assert(
            viewModel.visitables.value?.any {
                val isUnavailableProduct =
                    it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !it.isEnabled
                val isAccordionUiModel =
                    it is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
                isUnavailableProduct || isAccordionUiModel
            } == false
        )
    }

    @Test
    fun `when bulkDeleteUnavailableProducts but no unavailable products, should not do anything`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_SINGLE_PRODUCT_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.bulkDeleteUnavailableProducts()

        val expectedUnavailableProductCount = successResponse.cartListTokofood.data.unavailableSections.firstOrNull()?.products?.size.orZero()
        assertEquals(
            expectedUnavailableProductCount,
            viewModel.visitables.value?.count { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !it.isAvailable })
    }

    @Test
    fun `when toggleUnavailableProductsAccordion on expanded products, should collapse`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()

        viewModel.toggleUnavailableProductsAccordion()

        assert(
            (viewModel.visitables.value?.find {
                it is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
            } as? TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel)?.isCollapsed == true
        )
    }

    @Test
    fun `when toggleUnavailableProductsAccordion on collapsed products, should expand`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()

        viewModel.toggleUnavailableProductsAccordion()
        viewModel.toggleUnavailableProductsAccordion()

        assert(
            (viewModel.visitables.value?.find {
                it is TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel
            } as? TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel)?.isCollapsed == false
        )
    }

    @Test
    fun `when toggleUnavailableProductsAccordion but no unavailable products, should do nothing`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_SINGLE_PRODUCT_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()

        viewModel.toggleUnavailableProductsAccordion()

        val expectedUnavailableProductCount = successResponse.cartListTokofood.data.unavailableSections.firstOrNull()?.products?.size.orZero()
        assertEquals(
            expectedUnavailableProductCount,
            viewModel.visitables.value?.count { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !it.isAvailable })
    }

    @Test
    fun `when toggleUnavailableProductsAccordion but no unavailable reason data on expanded products, should only remove product items`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.toggleUnavailableProductsAccordion()

            val expectedUnavailableProductCount = successResponse.cartListTokofood.data.unavailableSections.firstOrNull()?.products?.size.orZero()
            assertEquals(
                expectedUnavailableProductCount,
                viewModel.visitables.value?.count { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !it.isAvailable })
        }
    }

    @Test
    fun `when scrollToUnavailableItem, should set ui event to scroll`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_UNAVAILABLE_PRODUCTS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.scrollToUnavailableItem()

        val expectedUiEventState = PurchaseUiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS
        assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
    }

    @Test
    fun `when scrollToUnavailableItem but no unavailable products, should not do anything`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_SINGLE_PRODUCT_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.scrollToUnavailableItem()

        val unexpectedUiEventState = PurchaseUiEvent.EVENT_SCROLL_TO_UNAVAILABLE_ITEMS
        assertNotEquals(unexpectedUiEventState, viewModel.purchaseUiEvent.value?.state)
    }

    @Test
    fun `when updateNotes should update the product notes`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
        val expectedNotes = "notes"
        val updatedProduct = successResponse.cartListTokofood.data.availableSection.products.first().let {
            CartTokoFood(
                productId = it.productId,
                cartId = it.cartId,
                metadata = CartMetadataTokoFoodWithVariant(
                    notes = expectedNotes
                ).generateString()
            )
        }

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.updateNotes(updatedProduct)

        assertEquals(
            expectedNotes,
            viewModel.visitables.value?.getProductById(updatedProduct.productId, updatedProduct.cartId)?.second?.notes
        )
    }

    @Test
    fun `when updateNotes but productData is not found, should not update value`() {

        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
        val expectedNotes = "notes"
        val updatedProduct =
            CartTokoFood(
                productId = "false product id",
                cartId = "false cart id",
                metadata = CartMetadataTokoFoodWithVariant(
                    notes = expectedNotes
                ).generateString()
            )

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.updateNotes(updatedProduct)

        assertNotEquals(
            expectedNotes,
            viewModel.visitables.value?.getProductById(updatedProduct.productId, updatedProduct.cartId)?.second?.notes
        )
    }

    @Test
    fun `when updateNotes but metadata is empty should not update the product notes`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
        val expectedNotes = "notes"
        val updatedProduct = successResponse.cartListTokofood.data.availableSection.products.first().let {
            CartTokoFood(
                productId = it.productId,
                cartId = it.cartId,
                metadata = ""
            )
        }

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()
        viewModel.updateNotes(updatedProduct)

        assertNotEquals(
            expectedNotes,
            viewModel.visitables.value?.getProductById(updatedProduct.productId, updatedProduct.cartId)?.second?.notes
        )
    }

    @Test
    fun `when updateCartId, should update product cartId`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()

        val expectedCartId = "1234"
        val updateParam = TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(
            viewModel.visitables.value?.filterIsInstance<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
                ?.filter { it.isAvailable }.orEmpty(),
            successResponse.cartListTokofood.data.shop.shopId
        )
        val changedCartId = updateParam.productList.first().cartId
        val cartData = CartTokoFoodData(
            carts = updateParam.productList.map {
                CartTokoFood(
                    cartId = if (it.cartId == changedCartId) expectedCartId else it.cartId,
                    productId = it.productId
                )
            }
        )
        viewModel.updateCartId(updateParam, cartData)

        assert(
            viewModel.visitables.value?.any { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && it.cartId == expectedCartId } == true
        )
    }

    @Test
    fun `when updateCartId but no cart data is not matched, should not update product cartId`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()

        val expectedCartId = "1234"
        val updateParam = TokoFoodPurchaseUiModelMapper.mapUiModelToUpdateParam(
            viewModel.visitables.value?.filterIsInstance<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>()
                ?.filter { it.isAvailable }.orEmpty(),
            successResponse.cartListTokofood.data.shop.shopId
        )
        val cartData = CartTokoFoodData(
            carts = updateParam.productList.map {
                CartTokoFood(
                    cartId = "false cart id",
                    productId = "false product id"
                )
            }
        )
        viewModel.updateCartId(updateParam, cartData)

        assert(
            viewModel.visitables.value?.any { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && it.cartId == expectedCartId } == false
        )
    }

    @Test
    fun `when updateCartId but no product found, should not update product cartId`() {
        val successResponse =
            JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                PURCHASE_SUCCESS_EMPTY_PRODUCT_JSON
            )
        onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

        viewModel.setIsHasPinpoint("123", true)
        viewModel.loadData()

        val expectedCartId = "1234"
        val updateParam = UpdateParam(productList = listOf(
            UpdateProductParam("", "", "", 0, listOf())
        ))
        val cartData = CartTokoFoodData(
            carts = updateParam.productList.map {
                CartTokoFood(
                    cartId = it.cartId,
                    productId = it.productId
                )
            }
        )
        viewModel.updateCartId(updateParam, cartData)

        assert(
            viewModel.visitables.value?.any { it is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && it.cartId == expectedCartId } != true
        )
    }

    @Test
    fun `when triggerEditQuantity, should emit update quantity state`() {
        coroutineTestRule.runBlockingTest {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.triggerEditQuantity()

            advanceTimeBy(1000L)

            assertNotNull(viewModel.updateQuantityStateFlow.value)
        }
    }

    @Test
    fun `when validateSetPinpoint, should set ui event state into set pinpoint`() {
        viewModel.validateSetPinpoint()

        val expectedUiEventState = PurchaseUiEvent.EVENT_NAVIGATE_TO_SET_PINPOINT
        assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
    }

    @Test
    fun `when updateAddressPinpoint and no addressId set, should set ui event to failed edit pinpoint`() {
        viewModel.updateAddressPinpoint(anyString(), anyString())

        val expectedUiEventState = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT
        assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
    }

    @Test
    fun `when updateAddressPinpoint, should success edit pinpoint`() {
        runBlocking {
            val addressId = "123"
            val latitude = "1"
            val longitude = "2"
            viewModel.setIsHasPinpoint(addressId, false)

            coEvery {
                keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
            } returns true

            viewModel.updateAddressPinpoint(latitude, longitude)

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when updateAddressPinpoint but lat is empty, should still success edit pinpoint`() {
        runBlocking {
            val addressId = "123"
            val latitude = ""
            val longitude = "2"
            viewModel.setIsHasPinpoint(addressId, false)

            coEvery {
                keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
            } returns true

            viewModel.updateAddressPinpoint(latitude, longitude)

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when updateAddressPinpoint but long is empty, should still success edit pinpoint`() {
        runBlocking {
            val addressId = "123"
            val latitude = "1"
            val longitude = ""
            viewModel.setIsHasPinpoint(addressId, false)

            coEvery {
                keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
            } returns true

            viewModel.updateAddressPinpoint(latitude, longitude)

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when updateAddressPinpoint but lat and long is empty, should still success edit pinpoint`() {
        runBlocking {
            val addressId = "123"
            val latitude = ""
            val longitude = ""
            viewModel.setIsHasPinpoint(addressId, false)

            coEvery {
                keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
            } returns true

            viewModel.updateAddressPinpoint(latitude, longitude)

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_EDIT_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when updateAddressPinpoint and update success is false, should failed edit pinpoint`() {
        runBlocking {
            val addressId = "123"
            val latitude = "1"
            val longitude = "2"
            viewModel.setIsHasPinpoint(addressId, false)

            coEvery {
                keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
            } returns false

            viewModel.updateAddressPinpoint(latitude, longitude)

            val expectedUiEventState = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when updateAddressPinpoint and update fails, should failed edit pinpoint`() {
        runBlocking {
            val addressId = "123"
            val latitude = "1"
            val longitude = "2"
            viewModel.setIsHasPinpoint(addressId, false)

            coEvery {
                keroEditAddressUseCase.get().execute(addressId, latitude, longitude)
            } throws MessageErrorException("")

            viewModel.updateAddressPinpoint(latitude, longitude)

            val expectedUiEventState = PurchaseUiEvent.EVENT_FAILED_EDIT_PINPOINT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when checkUserConsent and user consent is already true, should set ui event state to success`() {
        viewModel.setConsentAgreed(true)

        viewModel.checkUserConsent()

        val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT
        assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
    }

    @Test
    fun `when checkUserConsent but user consent is still false, should check from checkout data`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.checkUserConsent()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when checkUserConsent and checkout data should show bottomsheet, will update ui event state to success get consent`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_SHOW_CONSENT_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.checkUserConsent()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_GET_CONSENT
            val expectedConsentData = successResponse.cartListTokofood.data.checkoutConsentBottomSheet
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
            assertEquals(expectedConsentData, viewModel.purchaseUiEvent.value?.data)
        }
    }

    @Test
    fun `when checkUserConsent and partial checkout data should show bottomsheet, will update ui event state to success get consent`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_SHOW_CONSENT_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadDataPartial()

            viewModel.checkUserConsent()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_GET_CONSENT
            val expectedConsentData = successResponse.cartListTokofood.data.checkoutConsentBottomSheet
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
            assertEquals(expectedConsentData, viewModel.purchaseUiEvent.value?.data)
        }
    }

    @Test
    fun `when checkUserConsent and checkout data should not show bottomsheet, will update ui event state to success validate consent`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.setConsentAgreed(false)

            viewModel.checkUserConsent()

            val expectedUiEventState = PurchaseUiEvent.EVENT_SUCCESS_VALIDATE_CONSENT
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when checkUserConsent and checkout data still empty, should not do anything`() {
        runBlocking {
            val expectedValue = viewModel.purchaseUiEvent.value

            viewModel.checkUserConsent()

            assertEquals(expectedValue, viewModel.purchaseUiEvent.value)
        }
    }

    @Test
    fun `when checkoutGeneral and checkout data is exist, should success hit checkout general`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            val successCheckoutGeneralResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutGeneralTokoFoodResponse>(
                    PURCHASE_SUCCESS_CHECKOUT_GENERAL_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)
            onCheckoutGeneral_thenReturn(successResponse.cartListTokofood, successCheckoutGeneralResponse)

            viewModel.trackerPaymentCheckoutData.collectFromSharedFlow(
                whenAction = {
                    viewModel.setIsHasPinpoint("123", true)
                    viewModel.loadData()
                    viewModel.checkoutGeneral()
                },
                then = {
                    val expectedUiModelState = PurchaseUiEvent.EVENT_SUCCESS_CHECKOUT_GENERAL
                    assertEquals(expectedUiModelState, viewModel.purchaseUiEvent.value?.state)
                    assertEquals(successResponse.cartListTokofood.data, it)
                }
            )
        }
    }

    @Test
    fun `when checkoutGeneral and checkout data is exist but checkout general response is failed, should set failed event`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            val failedCheckoutGeneralResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutGeneralTokoFoodResponse>(
                    PURCHASE_FAILED_CHECKOUT_GENERAL_JSON
                )
            onCheckoutGeneral_thenReturn(successResponse.cartListTokofood, failedCheckoutGeneralResponse)
            viewModel.checkoutGeneral()

            val expectedUiModelState = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_TOASTER
            assertEquals(expectedUiModelState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when checkoutGeneral and checkout data is exist but checkout general failed, should send failed event`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            onCheckoutGeneral_thenThrow(successResponse.cartListTokofood, MessageErrorException(""))
            viewModel.checkoutGeneral()

            val expectedUiModelState = PurchaseUiEvent.EVENT_FAILED_CHECKOUT_GENERAL_BOTTOMSHEET
            assertEquals(expectedUiModelState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when checkoutGeneral but checkout data is not existed should not do anything`() {
        runBlocking {
            viewModel.checkoutGeneral()

            val expectedUiModelState = PurchaseUiEvent.EVENT_SUCCESS_CHECKOUT_GENERAL
            assertNotEquals(expectedUiModelState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when setPaymentButtonLoading true, should set total amount button to loading`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.setPaymentButtonLoading(true)

            assert(
                viewModel.visitables.value?.find { it is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel }?.let { totalAmount ->
                    (totalAmount as TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel).isButtonLoading
                } == true
            )
        }
    }

    @Test
    fun `when setPaymentButtonLoading false, should set total amount button to not loading`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.setPaymentButtonLoading(false)

            assert(
                viewModel.visitables.value?.find { it is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel }?.let { totalAmount ->
                    (totalAmount as TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel).isButtonLoading
                } == false
            )
        }
    }

    @Test
    fun `when setPaymentButtonLoading true but the model is disabled, should set total amount button to not loading`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_DISABLED_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.setPaymentButtonLoading(true)

            assert(
                viewModel.visitables.value?.find { it is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel }?.let { totalAmount ->
                    (totalAmount as TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel).isButtonLoading
                } == false
            )
        }
    }

    @Test
    fun `when setPaymentButtonLoading false but the model is disabled, should set total amount button to not loading`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_DISABLED_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.setPaymentButtonLoading(false)

            assert(
                viewModel.visitables.value?.find { it is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel }?.let { totalAmount ->
                    (totalAmount as TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel).isButtonLoading
                } == false
            )
        }
    }

    @Test
    fun `when setPaymentButtonLoading true but no visitables yet, should do nothing`() {
        viewModel.setPaymentButtonLoading(true)

        assert(
            viewModel.visitables.value?.find { it is TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel }?.let { totalAmount ->
                (totalAmount as TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel).isButtonLoading
            } != true
        )
    }

    @Test
    fun `when updateProductVariant and available products existed, should send order customization event`() {
        runBlocking {
            val successResponse =
                JsonResourcesUtil.createSuccessResponse<CheckoutTokoFoodResponse>(
                    PURCHASE_SUCCESS_JSON
                )
            onGetCheckoutTokoFood_thenReturn(successResponse.cartListTokofood)

            viewModel.setIsHasPinpoint("123", true)
            viewModel.loadData()

            viewModel.updateProductVariant(TokoFoodPurchaseProductTokoFoodPurchaseUiModel())

            val expectedUiEventState = PurchaseUiEvent.EVENT_GO_TO_ORDER_CUSTOMIZATION
            assertEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

    @Test
    fun `when updateProductVariant but available products not existed, should not send order customization event`() {
        runBlocking {
            viewModel.updateProductVariant(TokoFoodPurchaseProductTokoFoodPurchaseUiModel())

            val expectedUiEventState = PurchaseUiEvent.EVENT_GO_TO_ORDER_CUSTOMIZATION
            assertNotEquals(expectedUiEventState, viewModel.purchaseUiEvent.value?.state)
        }
    }

}