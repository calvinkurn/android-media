package com.tokopedia.tokopedianow.recipedetail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipecommon.domain.model.RecipeResponse
import com.tokopedia.tokopedianow.recipedetail.domain.usecase.GetRecipeUseCase
import com.tokopedia.tokopedianow.util.TestUtils.mockPrivateField
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class TokoNowRecipeDetailViewModelTestFixture {

    companion object {
        private const val GET_ADDRESS_SOURCE = "tokonow"
    }

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var getRecipeUseCase: GetRecipeUseCase
    private lateinit var getAddressUseCase: GetChosenAddressWarehouseLocUseCase
    private lateinit var addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase
    private lateinit var removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase
    private lateinit var addressData: TokoNowLocalAddress
    private lateinit var userSession: UserSessionInterface
    private lateinit var addToCartUseCase: AddToCartUseCase
    private lateinit var updateCartUseCase: UpdateCartUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var getMiniCartUseCase: GetMiniCartListSimplifiedUseCase

    protected lateinit var viewModel: TokoNowRecipeDetailViewModel

    @Before
    fun setUp() {
        getRecipeUseCase = mockk(relaxed = true)
        getAddressUseCase = mockk(relaxed = true)
        addRecipeBookmarkUseCase = mockk(relaxed = true)
        removeRecipeBookmarkUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)
        userSession = mockk(relaxed = true)
        addToCartUseCase = mockk(relaxed = true)
        updateCartUseCase = mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        getMiniCartUseCase = mockk(relaxed = true)

        viewModel = TokoNowRecipeDetailViewModel(
            getRecipeUseCase,
            getAddressUseCase,
            addRecipeBookmarkUseCase,
            removeRecipeBookmarkUseCase,
            addressData,
            userSession,
            addToCartUseCase,
            updateCartUseCase,
            deleteCartUseCase,
            getMiniCartUseCase,
            coroutineTestRule.dispatchers
        )
    }

    protected fun onGetShopId_thenReturn(vararg shopId: Long) {
        val firstCall = coEvery { addressData.getShopId() } returns shopId.first()

        shopId.forEach {
            firstCall andThen it
        }
    }

    protected fun onGetWarehouseId_thenReturn(warehouseId: String) {
        coEvery { addressData.getWarehouseId() } returns warehouseId.toLong()
    }

    protected fun onGetIsOutOfCoverage_thenReturn(outOfCoverage: Boolean) {
        coEvery { addressData.isOutOfCoverage() } returns outOfCoverage
    }

    protected fun onGetAddressData_thenReturn(response: GetStateChosenAddressResponse) {
        every {
            getAddressUseCase.getStateChosenAddress(any(), any(), GET_ADDRESS_SOURCE)
        } answers {
            firstArg<(GetStateChosenAddressResponse) -> Unit>().invoke(response)
        }
    }

    protected fun onGetAddressData_thenReturn(throwable: Throwable) {
        every {
            getAddressUseCase.getStateChosenAddress(any(), any(), GET_ADDRESS_SOURCE)
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }
    }

    protected fun onGetRecipe_thenReturn(response: RecipeResponse) {
        coEvery { getRecipeUseCase.execute(any(), any(), any()) } returns response
    }

    protected fun onGetRecipe_thenReturn(throwable: Throwable) {
        coEvery { getRecipeUseCase.execute(any(), any(), any()) } throws throwable
    }

    protected fun onAddRecipeBookmark_thenReturn(response: AddRecipeBookmarkResponse) {
        coEvery { addRecipeBookmarkUseCase.execute(any()) } returns response.tokonowAddRecipeBookmark
    }

    protected fun onAddRecipeBookmark_thenReturn(throwable: Throwable) {
        coEvery { addRecipeBookmarkUseCase.execute(any()) } throws throwable
    }

    protected fun onRemoveRecipeBookmark_thenReturn(response: RemoveRecipeBookmarkResponse) {
        coEvery { removeRecipeBookmarkUseCase.execute(any()) } returns response.tokonowRemoveRecipeBookmark
    }

    protected fun onRemoveRecipeBookmark_thenReturn(throwable: Throwable) {
        coEvery { removeRecipeBookmarkUseCase.execute(any()) } throws throwable
    }

    protected fun verifyGetAddressDataUseCaseCalled() {
        coVerify { getAddressUseCase.getStateChosenAddress(any(), any(), GET_ADDRESS_SOURCE) }
    }

    protected fun verifyGetAddressDataUseCaseNotCalled() {
        coVerify(exactly = 0) { getAddressUseCase.getStateChosenAddress(any(), any(), any()) }
    }

    protected fun verifyGetRecipeUseCaseCalled(
        recipeId: String = "",
        slug: String = "",
        warehouseId: String = "0",
    ) {
        coVerify { getRecipeUseCase.execute(eq(recipeId), eq(slug), eq(warehouseId)) }
    }

    protected fun verifyAddBookmarkUseCaseCalled(recipeId: String = "") {
        coVerify(exactly = 1) { addRecipeBookmarkUseCase.execute(eq(recipeId)) }
    }

    protected fun verifyAddBookmarkUseCaseNotCalled() {
        coVerify(exactly = 0) { addRecipeBookmarkUseCase.execute(any()) }
    }

    protected fun verifyRemoveBookmarkUseCaseCalled(recipeId: String = "") {
        coVerify(exactly = 1) { removeRecipeBookmarkUseCase.execute(eq(recipeId)) }
    }

    protected fun verifyRemoveBookmarkUseCaseNotCalled() {
        coVerify(exactly = 0) { removeRecipeBookmarkUseCase.execute(any()) }
    }

    protected fun verifyUpdateAddressDataCalled() {
        coVerify { addressData.updateLocalData() }
    }

    protected fun onGetLayoutItemList_returnNull() {
        viewModel.mockPrivateField("layoutItemList", null)
    }
}