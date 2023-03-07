package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.assertBannerDataView
import com.tokopedia.tokopedianow.searchcategory.assertCategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.assertChooseAddressDataView
import com.tokopedia.tokopedianow.searchcategory.assertProductCountDataView
import com.tokopedia.tokopedianow.searchcategory.assertQuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryTitle
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryFirstPageTest: BaseCategoryPageLoadTest() {

    @Test
    fun `test first page is last page`() {
        val categoryModel = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, categoryModel)
        `Then assert visitable list footer`(visitableList, categoryModel.categoryDetail.data.navigation)
        `Then assert has next page value`(false)
        `Then assert get first page success interactions`(categoryModel)
    }

    private fun `Then assert first page visitables`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        `Then assert visitable list header`(visitableList, categoryModel)
        `Then assert visitable list contents`(visitableList, categoryModel)
    }

    private fun `Then assert visitable list header`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        val categoryTitle = categoryModel.categoryDetail.data.name
        val mapParameter = tokoNowCategoryViewModel.queryParam

        visitableList[0].assertChooseAddressDataView()
        visitableList[1].assertBannerDataView()
        visitableList[2].assertTitleDataView(categoryTitle)
        visitableList[3].assertCategoryFilterDataView(categoryModel.categoryFilter)
        visitableList[4].assertQuickFilterDataView(categoryModel.quickFilter, mapParameter)
        visitableList[5].assertProductCountDataView(categoryModel.searchProduct.header.totalDataText)
    }

    private fun Visitable<*>.assertTitleDataView(title: String) {
        assertThat(this, instanceOf(TitleDataView::class.java))

        val titleDataView = this as TitleDataView
        assertThat(titleDataView.hasSeeAllCategoryButton, shouldBe(true))
        assertThat(titleDataView.titleType, instanceOf(CategoryTitle::class.java))

        val categoryTitle = titleDataView.titleType as CategoryTitle
        assertThat(categoryTitle.categoryName, shouldBe(title))
    }

    private fun `Then assert visitable list contents`(
            visitableList: List<Visitable<*>>,
            categoryModel: CategoryModel,
    ) {
        val expectedProductList = categoryModel.searchProduct.data.productList
        val actualProductItemDataViewList = visitableList.filterIsInstance<ProductItemDataView>()

        verifyProductItemDataViewList(expectedProductList, actualProductItemDataViewList, 1)
    }

    private fun `Then assert get first page success interactions`(categoryModel: CategoryModel) {
        `Then assert auto complete applink from API`(categoryModel)
        `Then assert header background is shown`()
        `Then assert content is not loading`()
    }

    private fun `Then assert auto complete applink from API`(categoryModel: CategoryModel) {
        val expectedApplink = categoryModel.searchProduct.data.autocompleteApplink

        assertThat(tokoNowCategoryViewModel.autoCompleteApplink, shouldBe(expectedApplink))
    }

    private fun `Then assert header background is shown`() {
        assertThat(tokoNowCategoryViewModel.isHeaderBackgroundVisibleLiveData.value, shouldBe(true))
    }

    private fun `Then assert content is not loading`() {
        assertThat(tokoNowCategoryViewModel.isContentLoadingLiveData.value, shouldBe(false))
    }

    private fun `Then assert product feedback loop not visible`(){
        Assert.assertEquals(tokoNowCategoryViewModel.isProductFeedbackLoopVisible(),false)
    }

    @Test
    fun `test first page has next page`() {
        val categoryModel = "category/first-page-16-products.json".jsonToObject<CategoryModel>()
        `Given get category first page use case will be successful`(categoryModel, requestParamsSlot)

        `When view created`()

        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!

        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(1))
        `Then assert first page visitables`(visitableList, categoryModel)
        `Then assert visitable list end with loading more model`(visitableList)
        `Then assert has next page value`(true)
        `Then assert get first page success interactions`(categoryModel)
        `Then assert product feedback loop not visible`()
    }
}
