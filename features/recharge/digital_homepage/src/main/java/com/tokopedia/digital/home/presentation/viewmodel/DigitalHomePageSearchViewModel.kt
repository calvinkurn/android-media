package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.old.domain.DigitalHomepageSearchByDynamicIconUseCase
import com.tokopedia.digital.home.old.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.util.RechargeHomepageDispatchersProvider
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class DigitalHomePageSearchViewModel @Inject constructor(
        private val searchCategoryHomePageUseCase: SearchCategoryHomePageUseCase,
        private val searchByDynamicIconUseCase: DigitalHomepageSearchByDynamicIconUseCase,
        private val dispatcher: RechargeHomepageDispatchersProvider
): BaseViewModel(dispatcher.Main) {

    private val mutableSearchCategoryList = MutableLiveData<Result<List<DigitalHomePageSearchCategoryModel>>>()
    val searchCategoryList: LiveData<Result<List<DigitalHomePageSearchCategoryModel>>>
        get() = mutableSearchCategoryList

    fun searchCategoryList(rawQuery: String, searchQuery: String, isLoadFromCloud: Boolean = false) {
        launchCatchError(block = {
            val data = withContext(dispatcher.IO) {
                searchCategoryHomePageUseCase.searchCategoryList(rawQuery, isLoadFromCloud, searchQuery)
            }
            mutableSearchCategoryList.postValue(Success(data))
        }) {
            mutableSearchCategoryList.postValue(Fail(it))
        }
    }

    fun searchByDynamicIconsCategory(query: String, platformId: Int, sectionIDs: List<Int>, enablePersonalize: Boolean = false) {
        launchCatchError(block = {
            val data = withContext(dispatcher.IO) {
                searchByDynamicIconUseCase.searchCategoryList(
                        DigitalHomepageSearchByDynamicIconUseCase.createRechargeHomepageSectionsParams(platformId,
                                sectionIDs, enablePersonalize), query)
            }
            mutableSearchCategoryList.postValue(Success(data))
        }) {
            mutableSearchCategoryList.postValue(Fail(it))
        }
    }

}