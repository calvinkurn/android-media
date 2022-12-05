//package com.tokopedia.tokopedianow.category.presentation.viewmodel
//
//import com.tokopedia.abstraction.base.view.adapter.Visitable
//import com.tokopedia.tokopedianow.category.domain.model.CategoryModel
//import com.tokopedia.tokopedianow.searchcategory.jsonToObject
//import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
//import com.tokopedia.tokopedianow.searchcategory.verifyProductItemDataViewList
//import io.mockk.every
//import io.mockk.verify
//import org.junit.Test
//
//class CategoryLoadMoreTest: BaseCategoryPageLoadTest() {
//
//    @Test
//    fun `test load more page as last page`() {
//        val categoryModelPage1 = "category/first-page-16-products.json".jsonToObject<CategoryModel>()
//        val categoryModelPage2 = "category/load-more-products.json".jsonToObject<CategoryModel>()
//
//        `Given get category first page use case will be successful`(categoryModelPage1)
//        `Given get category load more page use case will be successful`(categoryModelPage2)
//        `Given view already created`()
//
//        `When view load more`()
//
//        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
//        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(2))
//        `Then assert load more page data`(categoryModelPage1, categoryModelPage2, visitableList)
//        `Then assert visitable list footer`(visitableList, categoryModelPage1.categoryDetail.data.navigation)
//        `Then assert has next page value`(false)
//    }
//
//    private fun `Given get category load more page use case will be successful`(categoryModel: CategoryModel) {
//        every {
//            getCategoryLoadMorePageUseCase.execute(any(), any(), capture(requestParamsSlot))
//        } answers {
//            firstArg<(CategoryModel) -> Unit>().invoke(categoryModel)
//        }
//    }
//
//    private fun `When view load more`() {
//        tokoNowCategoryViewModel.onLoadMore()
//    }
//
//    private fun `Then assert load more page data`(
//            categoryModelPage1: CategoryModel,
//            categoryModelPage2: CategoryModel,
//            visitableList: List<Visitable<*>>
//    ) {
//        val firstProductIndex = visitableList.indexOfFirst { it is ProductItemDataView }
//        val page1ProductSize = categoryModelPage1.searchProduct.data.productList.size
//        val nextPageProductIndex = firstProductIndex + page1ProductSize
//        val page2ProductList = categoryModelPage2.searchProduct.data.productList
//        val nextPageVisitableList = visitableList.subList(
//                nextPageProductIndex,
//                nextPageProductIndex + page2ProductList.size
//        )
//
//        verifyProductItemDataViewList(
//                page2ProductList,
//                nextPageVisitableList.filterIsInstance<ProductItemDataView>(),
//                page1ProductSize + 1
//        )
//    }
//
//    @Test
//    fun `test load more page has next page`() {
//        val categoryModelPage1 = "category/first-page-24-products.json".jsonToObject<CategoryModel>()
//        val categoryModelPage2 = "category/load-more-products.json".jsonToObject<CategoryModel>()
//
//        `Given get category first page use case will be successful`(categoryModelPage1)
//        `Given get category load more page use case will be successful`(categoryModelPage2)
//        `Given view already created`()
//
//        `When view load more`()
//
//        val visitableList = tokoNowCategoryViewModel.visitableListLiveData.value!!
//        `Then assert request params map`(createExpectedMandatoryTokonowQueryParams(2))
//        `Then assert load more page data`(categoryModelPage1, categoryModelPage2, visitableList)
//        `Then assert visitable list end with loading more model`(visitableList)
//        `Then assert has next page value`(true)
//    }
//
//    @Test
//    fun `test do not load more page when already reached last page`() {
//        val categoryModelPage1 = "category/first-page-8-products.json".jsonToObject<CategoryModel>()
//
//        `Given get category first page use case will be successful`(categoryModelPage1)
//        `Given view already created`()
//
//        `When view load more`()
//
//        `Then verify get category load more use case is not called`()
//    }
//
//    private fun `Then verify get category load more use case is not called`() {
//        verify(exactly = 0) {
//            getCategoryLoadMorePageUseCase.execute(any(), any(), any())
//        }
//    }
//}
