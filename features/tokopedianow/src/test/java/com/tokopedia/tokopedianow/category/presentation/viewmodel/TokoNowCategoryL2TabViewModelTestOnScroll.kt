package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.mapper.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2TabData
import com.tokopedia.unit.test.ext.verifyValueEquals
import org.junit.Test

class TokoNowCategoryL2TabViewModelTestOnScroll: TokoNowCategoryL2TabViewModelTestFixture() {

    @Test
    fun `given atTheBottomOfThePage true when onScroll should load more product items`() {
        val atTheBottomOfThePage = true

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        val queryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)
        onGetProductList(withQueryParams = queryParams, thenReturn = getProductResponse)

        viewModel.onScroll(atTheBottomOfThePage)

        val expectedVisitableList = mutableListOf<Visitable<*>>()

        expectedVisitableList.addProductCardItems(
            response = getProductResponse,
            miniCartData = null,
            hasBlockedAddToCart = false
        )

        verifyGetProductUseCaseCalled(queryParams)

        viewModel.visitableListLiveData
            .verifyValueEquals(expectedVisitableList)
    }

    @Test
    fun `given atTheBottomOfThePage false when onScroll should not call get product use case`() {
        val atTheBottomOfThePage = false

        viewModel.onScroll(atTheBottomOfThePage)

        verifyGetProductUseCaseNotCalled()
    }

    @Test
    fun `given get product throws error when loadMore should do nothing`() {
        val queryParams = createGetProductQueryParams(categoryIdL1, categoryIdL2)

        val atTheBottomOfThePage = true

        val data = CategoryL2TabData(
            title = categoryTitle,
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2
        )

        viewModel.onViewCreated(data)

        onGetProductList_throwsError()

        viewModel.onScroll(atTheBottomOfThePage)

        verifyGetProductUseCaseCalled(queryParams)
    }
}
