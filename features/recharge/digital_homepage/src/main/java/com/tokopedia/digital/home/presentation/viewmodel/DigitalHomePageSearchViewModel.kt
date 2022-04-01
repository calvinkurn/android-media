package com.tokopedia.digital.home.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.digital.home.old.domain.DigitalHomepageSearchByDynamicIconUseCase
import com.tokopedia.digital.home.old.domain.SearchCategoryHomePageUseCase
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital.home.model.Tracking
import com.tokopedia.digital.home.old.domain.SearchAutoCompleteHomePageUseCase
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchNewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by resakemal on 12/08/19.
 */
class DigitalHomePageSearchViewModel @Inject constructor(
        private val searchCategoryHomePageUseCase: SearchCategoryHomePageUseCase,
        private val searchByDynamicIconUseCase: DigitalHomepageSearchByDynamicIconUseCase,
        private val searchAutoCompleteHomePageUseCase: SearchAutoCompleteHomePageUseCase,
        private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    lateinit var job: Job

    private val mutableSearchCategoryList = MutableLiveData<Result<DigitalHomePageSearchNewModel>>()
    val searchCategoryList: LiveData<Result<DigitalHomePageSearchNewModel>>
        get() = mutableSearchCategoryList

    fun searchCategoryList(rawQuery: String, searchQuery: String, isLoadFromCloud: Boolean = false) {
        job = launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                searchCategoryHomePageUseCase.searchCategoryList(rawQuery, isLoadFromCloud, searchQuery)
            }
            mutableSearchCategoryList.postValue(Success(data))
        }) {
            mutableSearchCategoryList.postValue(Fail(it))
        }
    }

    fun searchByDynamicIconsCategory(query: String, platformId: Int, sectionIDs: List<Int>, enablePersonalize: Boolean = false) {
        job = launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                searchByDynamicIconUseCase.searchCategoryList(
                        DigitalHomepageSearchByDynamicIconUseCase.createRechargeHomepageSectionsParams(platformId,
                                sectionIDs, enablePersonalize), query)
            }
            mutableSearchCategoryList.postValue(Success(data))
        }) {
            mutableSearchCategoryList.postValue(Fail(it))
        }
    }

    fun searchAutoComplete(map: Map<String, Any>, searchQuery: String){
        job = launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                searchAutoCompleteHomePageUseCase.searchAutoCompleteList(map, searchQuery)
            }
            mutableSearchCategoryList.postValue(Success(data))
        }) {
            mutableSearchCategoryList.postValue(Fail(it))
        }
    }

    fun cancelAutoComplete(){
        viewModelScope.launch{
            if (::job.isInitialized && job.isActive){
                job.cancelAndJoin()
            }
            mutableSearchCategoryList.postValue(Success(DigitalHomePageSearchNewModel(false, Tracking(), "", emptyList())))
        }
    }

    fun mapAutoCompleteParams(query: String): Map<String, Any>{
        return mapOf(
            PARAM to String.format(PARAM_FORMAT, query)
        )
    }

    companion object {
        const val PARAM = "param"
        const val PARAM_FORMAT = "navsource=tnb&q=%s&source=search&categoryid="
    }

}