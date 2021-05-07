package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SearchViewModel @Inject constructor (
        baseDispatcher: CoroutineDispatchers,
        private val getSearchFirstPageUseCase: UseCase<SearchModel>,
): BaseSearchCategoryViewModel(baseDispatcher) {

    override fun onViewCreated() {
        getSearchFirstPageUseCase.cancelJobs()
        getSearchFirstPageUseCase.execute(
                this::onGetSearchFirstPageSuccess,
                this::onGetSearchFirstPageError,
                RequestParams.create()
        )
    }

    private fun onGetSearchFirstPageSuccess(searchModel: SearchModel) {
        val headerDataView = HeaderDataView(
                title = "",
                hasSeeAllCategoryButton = false,
                totalData = searchModel.searchProduct.header.totalData
        )

        val contentDataView = ContentDataView(
                productList = searchModel.searchProduct.data.productList,
        )

        onGetFirstPageSuccess(headerDataView, contentDataView)
    }

    private fun onGetSearchFirstPageError(throwable: Throwable) {

    }
}
