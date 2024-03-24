package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.bottomsheet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.helper.ResourceProvider
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.AnotherOptionBottomSheetVisitableExtension.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.AnotherOptionBottomSheetVisitableExtension.switchToProductRecommendationAdded
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.resetIndices
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel.TokoNowShoppingListAnotherOptionBottomSheetViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.StateFlow
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class TokoNowShoppingListAnotherOptionBottomSheetFixture {

    protected lateinit var viewModel: TokoNowShoppingListAnotherOptionBottomSheetViewModel

    @RelaxedMockK
    lateinit var addressData: TokoNowLocalAddress

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var addToWishlistUseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var productRecommendationUseCase: GetSingleRecommendationUseCase

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoNowShoppingListAnotherOptionBottomSheetViewModel(
            userSession,
            addToWishlistUseCase,
            productRecommendationUseCase,
            resourceProvider,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `When processing loading state then should return the state`() {
        // load loading state
        viewModel
            .loadLoadingState()

        // provide expected result
        val expectedResult: MutableList<Visitable<*>> = mutableListOf()

        expectedResult
            .addLoadingState()

        // verify section
        viewModel
            .layoutState
            .verifyLoading(expectedResult)
    }

    protected fun StateFlow<UiState<List<Visitable<*>>>>.verifySuccess(expectedResult: Any) {
        Assert.assertEquals(expectedResult, (value as UiState.Success).data)
    }

    protected fun StateFlow<UiState<List<Visitable<*>>>>.verifyLoading(expectedResult: Any) {
        Assert.assertEquals(expectedResult, (value as UiState.Loading).data)
    }

    protected fun StateFlow<UiState<List<Visitable<*>>>>.verifyError(throwable: Throwable, expectedResult: Any) {
        Assert.assertEquals(throwable, (value as UiState.Error).throwable)
        Assert.assertEquals(expectedResult, (value as UiState.Error).data)
    }

    protected fun StateFlow<Any?>.verifyValue(expectedResult: Any) {
        Assert.assertEquals(expectedResult, value)
    }

    protected fun stubUserId(
        userId: String
    ) {
        every {
            userSession.userId
        } returns userId
    }

    protected fun stubProductRecommendation(
        param: GetRecommendationRequestParam,
        response: RecommendationWidget
    ) {
        coEvery {
            productRecommendationUseCase.getData(param)
        } returns response
    }

    protected fun stubProductRecommendation(
        param: GetRecommendationRequestParam,
        throwable: Throwable
    ) {
        coEvery {
            productRecommendationUseCase.getData(param)
        } throws  throwable
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

    protected fun filterProductRecommendationWithAvailableProduct(
        recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel>,
        availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel>
    ): List<ShoppingListHorizontalProductCardItemUiModel> = recommendedProducts.switchToProductRecommendationAdded(
        availableProducts = availableProducts
    ).resetIndices()
}
