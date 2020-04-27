package com.tokopedia.vouchergame.list.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class DigitalHomePageSearchViewModel @Inject constructor(private val searchCategoryHomePageUseCase: SearchCategoryHomePageUseCase,
                                                   dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val searchCategoryList = MutableLiveData<Result<List<DigitalHomePageSearchCategoryModel>>>()

    fun searchCategoryList(rawQuery: String, searchQuery: String, isLoadFromCloud: Boolean = false) {
        launch {
            searchCategoryList.value = searchCategoryHomePageUseCase.searchCategoryList(rawQuery, isLoadFromCloud, searchQuery)
        }
    }

}