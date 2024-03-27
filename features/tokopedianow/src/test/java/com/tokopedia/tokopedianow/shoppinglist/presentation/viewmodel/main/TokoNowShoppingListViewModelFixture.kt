package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartMultiUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.domain.model.WarehouseData
import com.tokopedia.tokopedianow.common.helper.ResourceProvider
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.util.AddressMapper.mapToWarehousesData
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.shoppinglist.domain.model.GetShoppingListDataResponse
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.GetShoppingListUseCase
import com.tokopedia.tokopedianow.shoppinglist.domain.usecase.SaveShoppingListStateUseCase
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

abstract class TokoNowShoppingListViewModelFixture {

    protected lateinit var viewModel: TokoNowShoppingListViewModel

    @RelaxedMockK
    lateinit var addressData: TokoNowLocalAddress

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var addToWishlistUseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var productRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var getShoppingListUseCase: GetShoppingListUseCase

    @RelaxedMockK
    lateinit var saveShoppingListStateUseCase: SaveShoppingListStateUseCase

    @RelaxedMockK
    lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase

    @RelaxedMockK
    lateinit var deleteFromWishlistUseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var addToCartMultiUseCase: AddToCartMultiUseCase

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowShoppingListViewModel(
            addressData,
            userSession,
            productRecommendationUseCase,
            getShoppingListUseCase,
            saveShoppingListStateUseCase,
            getMiniCartUseCase,
            addToWishlistUseCase,
            deleteFromWishlistUseCase,
            addToCartMultiUseCase,
            resourceProvider,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun StateFlow<UiState<*>>.verifySuccess(expectedResult: Any) {
        Assert.assertEquals(expectedResult, (value as UiState.Success).data)
    }

    protected fun StateFlow<UiState<*>>.verifyError(throwable: Throwable, expectedResult: Any) {
        Assert.assertEquals(throwable, (value as UiState.Error).throwable)
        Assert.assertEquals(expectedResult, (value as UiState.Error).data)
    }

    protected fun StateFlow<UiState<*>>.verifyIsError() {
        Assert.assertTrue(value is UiState.Error)
    }

    protected fun StateFlow<Any?>.verifyValue(expectedResult: Any?) {
        Assert.assertEquals(expectedResult, value)
    }

    protected fun verifyIsTrue(expectedResult: Boolean) {
        Assert.assertTrue(expectedResult)
    }

    protected fun stubOutOfCoverage(
        isOoc: Boolean
    ) {
        every {
            addressData.isOutOfCoverage()
        } returns isOoc
    }

    protected fun stubShopId(
        shopId: Long
    ) {
        every {
            addressData.getShopId()
        } returns shopId
    }

    protected fun stubLoggedIn(
        isLoggedIn: Boolean
    ) {
        every {
            userSession.isLoggedIn
        } returns isLoggedIn
    }

    protected fun stubGetMiniCart(
        response: MiniCartSimplifiedData
    ) {
        coEvery {
            getMiniCartUseCase.setParams(
                shopIds = listOf(addressData.getShopId().toString()),
                source = MiniCartSource.TokonowShoppingList
            )
            getMiniCartUseCase.executeOnBackground()
        } returns response
    }

    protected fun stubGetMiniCart(
        throwable: Throwable
    ) {
        coEvery {
            getMiniCartUseCase.setParams(
                shopIds = listOf(addressData.getShopId().toString()),
                source = MiniCartSource.TokonowShoppingList
            )
            getMiniCartUseCase.executeOnBackground()
        } throws throwable
    }

    protected fun stubGetShoppingList(
        response: GetShoppingListDataResponse.Data
    ) {
        coEvery {
            val warehouses = mapToWarehousesData(addressData.getAddressData())
            getShoppingListUseCase.execute(warehouses)
        } returns response
    }

    protected fun stubGetShoppingList(
        throwable: Throwable
    ) {
        coEvery {
            val warehouses = mapToWarehousesData(addressData.getAddressData())
            getShoppingListUseCase.execute(warehouses)
        } throws throwable
    }

    protected fun stubGetProductRecommendation(
        param: GetRecommendationRequestParam,
        response: RecommendationWidget
    ) {
        coEvery {
            productRecommendationUseCase.getData(param)
        } returns response
    }

    protected fun stubGetProductRecommendation(
        param: GetRecommendationRequestParam,
        throwable: Throwable
    ) {
        coEvery {
            productRecommendationUseCase.getData(param)
        } throws throwable
    }

    protected fun stubAddToWishlist(
        response: Result<AddToWishlistV2Response.Data.WishlistAddV2>
    ) {
        coEvery {
            addToWishlistUseCase.executeOnBackground()
        } returns response
    }

    protected fun stubAddToWishlist(
        throwable: Throwable
    ) {
        coEvery {
            addToWishlistUseCase.executeOnBackground()
        } throws throwable
    }

    protected fun stubShoppingList(
        warehouses: List<WarehouseData>,
        response: GetShoppingListDataResponse.Data
    ) {
        coEvery {
            getShoppingListUseCase.execute(warehouses)
        } returns response
    }

    protected fun verifyProductRecommendationUseCase(
        param: GetRecommendationRequestParam
    ) {
        coVerify {
            productRecommendationUseCase.getData(param)
        }
    }

    protected fun verifyAddToWishlistUseCase(
        productId: String,
        userId: String
    ) {
        coVerify {
            addToWishlistUseCase.setParams(productId, userId)
            addToWishlistUseCase.executeOnBackground()
        }
    }
}
