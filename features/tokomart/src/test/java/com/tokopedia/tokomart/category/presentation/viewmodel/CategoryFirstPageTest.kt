package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.searchcategory.assertBannerDataView
import com.tokopedia.tokomart.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.assertProductCountDataView
import com.tokopedia.tokomart.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokomart.searchcategory.assertTitleDataView
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.verifyProductItemDataViewList
import io.mockk.coEvery
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

class CategoryFirstPageTest: CategoryTestFixtures() {

    @Test
    fun `test first page is last page`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = categoryViewModel.visitableListLiveData.value!!

        `Then assert visitable list header`(visitableList, categoryModel)
        `Then assert visitable list contents`(visitableList, categoryModel)
        `Then assert visitable list footer`(visitableList)
    }

    private fun `Given get category first page use case will be successful`(categoryModel: CategoryModel) {
        coEvery {
            getCategoryFirstPageUseCase.execute(any(), any(), any())
        } coAnswers {
            firstArg<(CategoryModel) -> Unit>().invoke(categoryModel)
        }
    }

    private fun `When view created`() {
        categoryViewModel.onViewCreated()
    }

    private fun `Then assert visitable list header`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView(title = "Category_Title", hasSeeAllCategoryButton = true)
        visitableList[3].assertQuickFilterDataView()
        visitableList[4].assertProductCountDataView(categoryModel.searchProduct.header.totalData)
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        val expectedProductList = categoryModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList)
    }

    private fun `Then assert visitable list footer`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(CategoryIsleDataView::class.java))
    }

    @Test
    fun `test first page has next page`() {
        val categoryModel = "category/first-page-16-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel)

        `When view created`()

        val visitableList = categoryViewModel.visitableListLiveData.value!!

        `Then assert visitable list header`(visitableList, categoryModel)
        `Then assert visitable list contents`(visitableList, categoryModel)
        `Then assert visitable list end with loading more model`(visitableList)
    }

    private fun `Then assert visitable list end with loading more model`(visitableList: List<Visitable<*>>) {
        assertThat(visitableList.last(), instanceOf(LoadingMoreModel::class.java))
    }
}