package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import org.junit.Test

class CategoryOssTest: BaseCategoryPageLoadTest() {

    @Test
    fun `test oos state product card`() {
        val categoryModel = "category/first-page-8-products-oos.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!

        `Then assert first page visitables`(visitableList, categoryModel)
    }

    private fun `Then assert first page visitables`(
        visitableList: List<Visitable<*>>,
        categoryModel: CategoryModel,
    ) {
        `Then assert visitable list contents`(visitableList, categoryModel)
    }

    private fun `Then assert visitable list contents`(
        visitableList: List<Visitable<*>>,
        categoryModel: CategoryModel,
    ) {
        val expectedProductList = categoryModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(
            expectedProductList = expectedProductList,
            actualProductItemDataViewList = actualProductItemDataViewList,
            startPosition = 1,
            needToVerifyAtc = false
        )
    }

}
