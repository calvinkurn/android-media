package com.tokopedia.cart.view.viewmodel

import androidx.lifecycle.Observer
import com.tokopedia.cart.utils.DataProvider
import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartrevamp.view.CartViewModel
import com.tokopedia.cartrevamp.view.helper.CartDataHelper
import com.tokopedia.cartrevamp.view.uimodel.CartGlobalEvent
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerData
import com.tokopedia.cartrevamp.view.uimodel.CartShopGroupTickerState
import com.tokopedia.cartrevamp.view.uimodel.UpdateCartCheckoutState
import com.tokopedia.cartrevamp.view.util.CartPageAnalyticsUtil
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout.Companion.KEY_CHECKOUT
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceCheckout.Companion.KEY_PRODUCT
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateCartTest : BaseCartViewModelTest() {

    private lateinit var observer: Observer<UpdateCartCheckoutState>
    private lateinit var cartGlobalEventObserver: Observer<CartGlobalEvent>

    override fun setUp() {
        super.setUp()
        mockkObject(CartPageAnalyticsUtil)
        observer = mockk { every { onChanged(any()) } just Runs }
        cartGlobalEventObserver = mockk { every { onChanged(any()) } just Runs }

        cartViewModel.updateCartForCheckoutState.observeForever(observer)
        cartViewModel.globalEvent.observeForever(cartGlobalEventObserver)
    }

    @Test
    fun `WHEN update cart for checkout success THEN should navigate to checkout page`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isError = false
                    isBundlingItem = false
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                    trackerAttribution = "t"
                    category = "c"
                }
            )
            add(
                CartItemHolderData().apply {
                    isError = false
                    isBundlingItem = true
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                    trackerAttribution = "t"
                    category = "c"
                }
            )
            add(
                CartItemHolderData().apply {
                    isError = true
                    isBundlingItem = true
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                    trackerAttribution = "t"
                    category = "c"
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { cartItemDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    true,
                    CartViewModel.ITEM_CHECKED_ALL_WITHOUT_CHANGES
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with eligible COD THEN should navigate to checkout page with eligible COD`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    true,
                    CartViewModel.ITEM_CHECKED_ALL_WITHOUT_CHANGES
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with not eligible COD THEN should navigate to checkout page with not eligible COD`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = false
                    productPrice = 1000000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    false,
                    CartViewModel.ITEM_CHECKED_ALL_WITHOUT_CHANGES
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked all without changes THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        cartViewModel.cartModel.hasPerformChecklistChange = false
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(CartItemHolderData()) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    false,
                    CartViewModel.ITEM_CHECKED_ALL_WITHOUT_CHANGES
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked all with changes THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        cartViewModel.cartModel.hasPerformChecklistChange = true
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(CartItemHolderData()) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    false,
                    CartViewModel.ITEM_CHECKED_ALL_WITH_CHANGES
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial item THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllShopGroupDataList(any()) } answers { shopDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(CartItemHolderData()) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    false,
                    CartViewModel.ITEM_CHECKED_PARTIAL_ITEM
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial shop THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = true
                            }
                        )
                    }
                    isAllSelected = true
                }
            )
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                    isAllSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }

        every { CartDataHelper.getAllShopGroupDataList(any()) } answers { shopDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(CartItemHolderData()) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    false,
                    CartViewModel.ITEM_CHECKED_PARTIAL_SHOP
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with item checked partial shop and item THEN should navigate to checkout page with matched condition`() {
        // GIVEN
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = true
                            }
                        )
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                    isAllSelected = false
                    isPartialSelected = true
                }
            )
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf<CartItemHolderData>().apply {
                        add(
                            CartItemHolderData().apply {
                                isSelected = false
                            }
                        )
                    }
                    isAllSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllShopGroupDataList(any()) } answers { shopDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(CartItemHolderData()) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { hashMapOf() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(
                UpdateCartCheckoutState.Success(
                    hashMapOf(),
                    false,
                    CartViewModel.ITEM_CHECKED_PARTIAL_SHOP_AND_ITEM
                )
            )
        }
    }

    @Test
    fun `WHEN update cart for checkout success with not blank and afford BO affordability THEN should send EE dimension119 value with fulfill`() {
        // GIVEN
        val tickerText = "tickerText"
        val boType = 0
        val cartItem = CartItemHolderData().apply {
            isSelected = true
            shopCartShopGroupTickerData = CartShopGroupTickerData(
                tickerText = tickerText,
                state = CartShopGroupTickerState.SUCCESS_AFFORD
            )
            shopBoMetadata = BoMetadata(
                boType = boType
            )
        }
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf(cartItem)
                    isAllSelected = true
                    isPartialSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllShopGroupDataList(any()) } answers { shopDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(cartItem) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { callOriginal() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        val successStateValue =
            cartViewModel.updateCartForCheckoutState.getOrAwaitValue() as UpdateCartCheckoutState.Success
        val checkoutMap = successStateValue.eeCheckoutData[KEY_CHECKOUT] as Map<String, Any>
        val productList = checkoutMap[KEY_PRODUCT] as List<Any>
        val dimensionMap = productList[0] as Map<String, Any>
        assertTrue(dimensionMap["dimension119"] == "fulfill_$boType")
    }

    @Test
    fun `WHEN update cart for checkout success with not blank and not afford BO affordability THEN should send EE dimension119 value with unfulfill`() {
        // GIVEN
        val tickerText = "tickerText"
        val boType = 0
        val cartItem = CartItemHolderData().apply {
            isSelected = true
            shopCartShopGroupTickerData = CartShopGroupTickerData(
                tickerText = tickerText,
                state = CartShopGroupTickerState.SUCCESS_NOT_AFFORD
            )
            shopBoMetadata = BoMetadata(
                boType = boType
            )
        }
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf(cartItem)
                    isAllSelected = true
                    isPartialSelected = false
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllShopGroupDataList(any()) } answers { shopDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(cartItem) }
        every {
            CartPageAnalyticsUtil.generateCheckoutDataAnalytics(
                any(),
                any()
            )
        } answers { callOriginal() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        val successStateValue =
            cartViewModel.updateCartForCheckoutState.getOrAwaitValue() as UpdateCartCheckoutState.Success
        val checkoutMap = successStateValue.eeCheckoutData[KEY_CHECKOUT]!! as Map<String, Any>
        val productList = checkoutMap[KEY_PRODUCT] as List<Any>
        val dimensionMap = productList[0] as Map<String, Any>
        assertTrue(dimensionMap["dimension119"] == "unfulfill_$boType")
    }

    @Test
    fun `WHEN update cart for checkout success with blank BO affordability THEN should send EE dimension119 empty`() {
        // GIVEN
        val tickerText = ""
        val cartItem = CartItemHolderData().apply {
            isSelected = true
            shopCartShopGroupTickerData = CartShopGroupTickerData(
                tickerText = tickerText
            )
        }
        val shopDataList = mutableListOf<CartGroupHolderData>().apply {
            add(
                CartGroupHolderData().apply {
                    productUiModelList = mutableListOf(cartItem)
                    isAllSelected = true
                    isPartialSelected = false
                }
            )
        }
        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllShopGroupDataList(any()) } answers { shopDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(cartItem) }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        val successStateValue = cartViewModel.updateCartForCheckoutState.getOrAwaitValue() as UpdateCartCheckoutState.Success
        val checkoutMap = successStateValue.eeCheckoutData[KEY_CHECKOUT]!! as Map<String, Any>
        val productList = checkoutMap[KEY_PRODUCT]!! as List<Any>
        val dimensionMap = productList[0] as Map<String, Any>
        assertTrue(dimensionMap["dimension119"] == String.EMPTY)
    }

    @Test
    fun `WHEN update cart for checkout failed with out of service THEN should render out of service error`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val outOfService = OutOfService(id = "503")
        val mockResponse =
            UpdateCartV2Data(data = Data(status = false, outOfService = outOfService))
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { cartItemDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(UpdateCartCheckoutState.ErrorOutOfService(outOfService))
        }
    }

    @Test
    fun `WHEN update cart for checkout failed with other error and no need to show cta THEN should render error with no cta`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val errorMessage = "error message"
        val mockResponse = UpdateCartV2Data(
            data = Data(
                status = false,
                error = errorMessage,
                toasterAction = ToasterAction(showCta = false)
            )
        )
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { cartItemDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(UpdateCartCheckoutState.UnknownError(errorMessage, ""))
        }
    }

    @Test
    fun `WHEN update cart for checkout failed with other error and need to show cta THEN should render error with cta`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val errorMessage = "error message"
        val cta = "Oke"
        val mockResponse = UpdateCartV2Data(
            data = Data(
                status = false,
                error = errorMessage,
                toasterAction = ToasterAction(showCta = true, text = cta)
            )
        )
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { cartItemDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(UpdateCartCheckoutState.UnknownError(errorMessage, cta))
        }
    }

    @Test
    fun `WHEN update cart failed THEN should render error`() {
        // GIVEN
        val throwable = ResponseErrorException("error")

        every { CartDataHelper.getSelectedCartItemData(any()) } answers { listOf(CartItemHolderData()) }
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify {
            observer.onChanged(UpdateCartCheckoutState.Failed(throwable))
        }
    }

    @Test
    fun `WHEN update cart with empty params THEN should not call API`() {
        // GIVEN
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { emptyList() }

        // WHEN
        cartViewModel.processUpdateCartData(false)

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for save state with empty params THEN should not call API`() {
        // GIVEN
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { emptyList() }

        // WHEN
        cartViewModel.processUpdateCartData(true)

        // THEN
        verify(inverse = true) {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for on fragment stopped THEN should call API`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { cartItemDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }

        // WHEN
        cartViewModel.processUpdateCartData(true)

        // THEN
        verify {
            updateCartUseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN update cart for on fragment stopped THEN should not show loading`() {
        // GIVEN
        val cartItemDataList = mutableListOf<CartItemHolderData>().apply {
            add(
                CartItemHolderData().apply {
                    isCod = true
                    productPrice = 1000.0
                    quantity = 10
                }
            )
        }

        val mockResponse = DataProvider.provideUpdateCartSuccess()
        coEvery { updateCartUseCase.setParams(any(), any()) } just Runs
        coEvery { updateCartUseCase.execute(any(), any()) } answers {
            firstArg<(UpdateCartV2Data) -> Unit>().invoke(mockResponse)
        }
        every { CartDataHelper.getAllAvailableCartItemData(any()) } answers { cartItemDataList }
        every { CartDataHelper.getSelectedCartItemData(any()) } answers { cartItemDataList }

        // WHEN
        cartViewModel.processUpdateCartData(true)

        // THEN
        verify(inverse = true) {
            cartGlobalEventObserver.onChanged(CartGlobalEvent.ProgressLoading(true))
        }
    }

    override fun tearDown() {
        super.tearDown()
        cartViewModel.updateCartForCheckoutState.removeObserver(observer)
        cartViewModel.globalEvent.observeForever(cartGlobalEventObserver)
    }
}
