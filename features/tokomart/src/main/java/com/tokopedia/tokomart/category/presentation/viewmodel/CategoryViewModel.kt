package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.category.presentation.model.CategoryIsleDataView
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CategoryViewModel @Inject constructor (
        baseDispatcher: CoroutineDispatchers,
        private val getCategoryFirstPageUseCase: UseCase<CategoryModel>,
): BaseSearchCategoryViewModel(baseDispatcher) {

    override fun onViewCreated() {
        getCategoryFirstPageUseCase.cancelJobs()
        getCategoryFirstPageUseCase.execute(
                this::onGetCategoryFirstPageSuccess,
                this::onGetCategoryFirstPageError,
                RequestParams.create(),
        )
    }

    private fun onGetCategoryFirstPageSuccess(categoryModel: CategoryModel) {
        val headerDataView = HeaderDataView(
                title = "Category_Title",
                hasSeeAllCategoryButton = true,
                totalData = categoryModel.searchProduct.header.totalData,
        )

        val contentDataView = ContentDataView(
                productList = categoryModel.searchProduct.data.productList,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    private fun onGetCategoryFirstPageError(throwable: Throwable) {
        TODO("Not yet implemented")
    }

    override fun createFooterVisitableList() = listOf(
            CategoryIsleDataView(),
    )
}
