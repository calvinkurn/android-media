package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.category.utils.CATEGORY_FIRST_PAGE_USE_CASE
import com.tokopedia.tokomart.category.utils.CATEGORY_ID
import com.tokopedia.tokomart.category.utils.CATEGORY_LOAD_MORE_PAGE_USE_CASE
import com.tokopedia.tokomart.category.utils.CATEGORY_QUERY_PARAM_MAP
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class CategoryViewModel @Inject constructor (
        baseDispatcher: CoroutineDispatchers,
        @param:Named(CATEGORY_ID)
        val categoryId: Int,
        @Named(CATEGORY_QUERY_PARAM_MAP)
        queryParamMap: Map<String, String>,
        @param:Named(CATEGORY_FIRST_PAGE_USE_CASE)
        private val getCategoryFirstPageUseCase: UseCase<CategoryModel>,
        @param:Named(CATEGORY_LOAD_MORE_PAGE_USE_CASE)
        private val getCategoryLoadMorePageUseCase: UseCase<CategoryModel>,
        getFilterUseCase: UseCase<DynamicFilterModel>,
        chooseAddressWrapper: ChooseAddressWrapper,
): BaseSearchCategoryViewModel(
        baseDispatcher,
        queryParamMap,
        getFilterUseCase,
        chooseAddressWrapper
) {

    override fun onViewCreated() {
        getCategoryFirstPageUseCase.cancelJobs()
        getCategoryFirstPageUseCase.execute(
                this::onGetCategoryFirstPageSuccess,
                this::onGetCategoryFirstPageError,
                createRequestParams(),
        )
    }

    private fun onGetCategoryFirstPageSuccess(categoryModel: CategoryModel) {
        val headerDataView = HeaderDataView(
                title = "Category_Title",
                hasSeeAllCategoryButton = true,
                aceSearchProductHeader = categoryModel.searchProduct.header,
                categoryFilterDataValue = categoryModel.categoryFilter,
                quickFilterDataValue = categoryModel.quickFilter,
        )

        val contentDataView = ContentDataView(
                productList = categoryModel.searchProduct.data.productList,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    private fun onGetCategoryFirstPageError(throwable: Throwable) {

    }

    override fun createFooterVisitableList() = listOf(
            CategoryIsleDataView(),
    )

    override fun executeLoadMore() {
        getCategoryLoadMorePageUseCase.execute(
                this::onGetCategoryLoadMorePageSuccess,
                this::onGetCategoryLoadMorePageError,
                createRequestParams(),
        )
    }

    private fun onGetCategoryLoadMorePageSuccess(categoryModel: CategoryModel) {
        val contentDataView = ContentDataView(productList = categoryModel.searchProduct.data.productList)
        onGetLoadMorePageSuccess(contentDataView)
    }

    private fun onGetCategoryLoadMorePageError(throwable: Throwable) {

    }
}
