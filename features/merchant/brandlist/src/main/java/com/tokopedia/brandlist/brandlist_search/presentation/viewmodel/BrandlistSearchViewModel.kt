package com.tokopedia.brandlist.brandlist_search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_search.domain.SearchBrandUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class BrandlistSearchViewModel @Inject constructor(
        private val searchBrandUseCase: SearchBrandUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

}