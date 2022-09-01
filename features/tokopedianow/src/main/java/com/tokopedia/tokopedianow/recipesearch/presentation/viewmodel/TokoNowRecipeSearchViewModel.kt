package com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.recipelist.base.viewmodel.BaseTokoNowRecipeListViewModel
import com.tokopedia.tokopedianow.recipelist.domain.usecase.GetRecipeListUseCase
import javax.inject.Inject

class TokoNowRecipeSearchViewModel @Inject constructor(
    getRecipeListUseCase: GetRecipeListUseCase,
    addressData: TokoNowLocalAddress,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowRecipeListViewModel(getRecipeListUseCase, addressData, dispatchers) {

    companion object {
        private const val SOURCE_PAGE_NAME = "Search"
    }

    override val sourcePage: String = SOURCE_PAGE_NAME

    fun setQueryParams(encodedQuery: String?) {
        val queryParams = if (encodedQuery.isNullOrEmpty()) "" else encodedQuery
        getRecipeListParam.queryParams = queryParams
    }
}