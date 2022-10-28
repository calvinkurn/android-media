package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipebookmark.domain.model.AddRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.model.RemoveRecipeBookmarkResponse
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.AddRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipebookmark.domain.usecase.RemoveRecipeBookmarkUseCase
import com.tokopedia.tokopedianow.recipelist.domain.model.TokoNowGetRecipes
import com.tokopedia.tokopedianow.recipelist.domain.param.RecipeListParam
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import com.tokopedia.tokopedianow.util.TestUtils.getPrivateField
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.CapturingSlot
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseTokoNowRecipeListViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val privateVisitableItems by lazy {
        viewModel.getPrivateField<MutableList<Visitable<*>>>("visitableItems")
    }

    private val showProgressBar by lazy {
        viewModel.getPrivateField<MutableLiveData<Boolean>>("_showProgressBar")
    }

    private lateinit var getRecipeListUseCase: GetRecipeListUseCase
    private lateinit var addRecipeBookmarkUseCase: AddRecipeBookmarkUseCase
    private lateinit var removeRecipeBookmarkUseCase: RemoveRecipeBookmarkUseCase
    private lateinit var addressData: TokoNowLocalAddress

    protected lateinit var viewModel: BaseTokoNowRecipeListViewModel

    @Before
    fun setUp() {
        getRecipeListUseCase = mockk(relaxed = true)
        addRecipeBookmarkUseCase = mockk(relaxed = true)
        removeRecipeBookmarkUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)

        viewModel = BaseTokoNowRecipeListViewModel(
            getRecipeListUseCase,
            addRecipeBookmarkUseCase,
            removeRecipeBookmarkUseCase,
            addressData,
            CoroutineTestDispatchers
        )
    }

    fun onGetRecipes_thenReturn(
        response: TokoNowGetRecipes,
        recipeListParamSlot: CapturingSlot<RecipeListParam> = slot(),
    ) {
        coEvery { getRecipeListUseCase.execute(capture(recipeListParamSlot)) } returns response.response
    }

    fun onGetRecipes_thenReturn(throwable: Throwable) {
        coEvery { getRecipeListUseCase.execute(any()) } throws throwable
    }

    fun onGetWarehouseId_thenReturn(warehouseId: Long) {
        coEvery { addressData.getWarehouseId() } returns warehouseId
    }

    fun onAddBookmark_thenReturn(response: AddRecipeBookmarkResponse) {
        coEvery { addRecipeBookmarkUseCase.execute(any()) } returns response.tokonowAddRecipeBookmark
    }

    fun onAddBookmark_thenReturn(throwable: Throwable) {
        coEvery { addRecipeBookmarkUseCase.execute(any()) } throws throwable
    }

    fun onRemoveBookmark_thenReturn(response: RemoveRecipeBookmarkResponse) {
        coEvery { removeRecipeBookmarkUseCase.execute(any()) } returns response.tokonowRemoveRecipeBookmark
    }

    fun onRemoveBookmark_thenReturn(throwable: Throwable) {
        coEvery { removeRecipeBookmarkUseCase.execute(any()) } throws throwable
    }

    fun verifyGetRecipeListUseCaseCalled(times: Int = 1) {
        coVerify(exactly = times) { getRecipeListUseCase.execute(any()) }
    }

    fun verifyGetRecipeListParams(
        expectedPage: Int,
        expectedSourcePage: String = "",
        expectedWarehouseId: String,
        expectedSortByParams: String?,
        expectedTagIdsParam: String?,
        expectedIngredientIdsParam: String?,
        expectedDurationParam: String?,
        expectedPortionParam: String?,
        actualRecipeListParam: RecipeListParam
    ) {
        val expectedPerPage = 5
        val actualQueryParamsMap = actualRecipeListParam.queryParamsMap
        assertEquals(expectedPage, actualRecipeListParam.page)
        assertEquals(expectedPerPage, actualRecipeListParam.perPage)
        assertEquals(expectedSourcePage, actualRecipeListParam.sourcePage)
        assertEquals(expectedWarehouseId, actualRecipeListParam.warehouseID)
        assertEquals(expectedSortByParams, actualQueryParamsMap["sort_by"])
        assertEquals(expectedTagIdsParam, actualQueryParamsMap["tag_ids"])
        assertEquals(expectedIngredientIdsParam, actualQueryParamsMap["ingredient_ids"])
        assertEquals(expectedDurationParam, actualQueryParamsMap["duration"])
        assertEquals(expectedPortionParam, actualQueryParamsMap["portion"])
    }

    fun addItemToVisitableList(item: Visitable<*>) {
        privateVisitableItems.add(item)
    }

    fun setShowProgressBar(shown: Boolean) {
        showProgressBar.value = shown
    }
}
