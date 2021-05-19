package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.assertBannerDataView
import com.tokopedia.tokomart.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.assertProductCountDataView
import com.tokopedia.tokomart.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokomart.searchcategory.assertTitleDataView
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.verifyProductItemDataViewList
import org.junit.Test

class CategoryFirstPageTest: BaseCategoryPageLoadTest() {

    @Test
    fun `test first page is last page`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = categoryViewModel.visitableListLiveData.value!!

        `Then assert first page visitables`(visitableList, categoryModel)
        `Then assert visitable list footer`(visitableList)
        `Then assert has next page value`(false)
    }

    private fun `When view created`() {
        categoryViewModel.onViewCreated()
    }

    private fun `Then assert first page visitables`(visitableList: List<Visitable<*>>, categoryModel: CategoryModel) {
        `Then assert visitable list header`(visitableList, categoryModel)
        `Then assert visitable list contents`(visitableList, categoryModel)
    }

    private fun `Then assert visitable list header`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView(title = "Category_Title", hasSeeAllCategoryButton = true)
        visitableList[3].assertQuickFilterDataView(categoryModel.quickFilter)
        visitableList[4].assertProductCountDataView(categoryModel.searchProduct.header.totalDataText)
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        val expectedProductList = categoryModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList)
    }

    @Test
    fun `test first page has next page`() {
        val categoryModel = "category/first-page-16-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = categoryViewModel.visitableListLiveData.value!!

        `Then assert first page visitables`(visitableList, categoryModel)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
    }
}