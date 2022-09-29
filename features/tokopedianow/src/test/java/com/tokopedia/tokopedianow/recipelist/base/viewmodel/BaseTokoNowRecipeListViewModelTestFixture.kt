package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
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

    private lateinit var getRecipeListUseCase: GetRecipeListUseCase
    private lateinit var addressData: TokoNowLocalAddress

    protected lateinit var viewModel: BaseTokoNowRecipeListViewModel

    @Before
    fun setUp() {
        getRecipeListUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)

        viewModel = BaseTokoNowRecipeListViewModel(
            getRecipeListUseCase,
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

    fun verifyGetRecipeListUseCaseCalled(times: Int = 1) {
        coVerify(exactly = times) { getRecipeListUseCase.execute(any()) }
    }

    fun verifyGetRecipeListParams(
        expectedPage: Int,
        expectedSourcePage: String = "",
        expectedWarehouseId: String,
        expectedSortByParams: String?,
        expectedIngredientIdsParams: String?,
        actualRecipeListParam: RecipeListParam
    ) {
        val expectedPerPage = 5
        val actualQueryParamsMap = actualRecipeListParam.queryParamsMap
        assertEquals(expectedPage, actualRecipeListParam.page)
        assertEquals(expectedPerPage, actualRecipeListParam.perPage)
        assertEquals(expectedSourcePage, actualRecipeListParam.sourcePage)
        assertEquals(expectedWarehouseId, actualRecipeListParam.warehouseID)
        assertEquals(expectedSortByParams, actualQueryParamsMap["sort_by"])
        assertEquals(expectedIngredientIdsParams, actualQueryParamsMap["ingredient_ids"])
    }

    fun addItemToVisitableList(item: Visitable<*>) {
        privateVisitableItems.add(item)
    }

    fun clearVisitableItems() {
        privateVisitableItems.clear()
    }
}
