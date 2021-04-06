package com.tokopedia.deals.search.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.DealsSearchConstants
import com.tokopedia.deals.search.domain.DealsSearchGqlQueries
import com.tokopedia.deals.search.model.response.*
import com.tokopedia.deals.search.domain.usecase.DealsSearchInitialLoadUseCase
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.model.response.EventSearch
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DealsSearchViewModel @Inject constructor (
        private val dealsLoadInitialData: DealsSearchInitialLoadUseCase,
        private val dealsSearchUseCase: DealsSearchUseCase,
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _dealsSearchResponse = MutableLiveData<Result<EventSearch>>()
    val dealsSearchResponse: LiveData<Result<EventSearch>>
        get() = _dealsSearchResponse

    private val _dealsSearchInitialLoad = MutableLiveData<Result<InitialLoadData>>()
    val dealsInitialResponse: LiveData<Result<InitialLoadData>>
        get() = _dealsSearchInitialLoad

    private val _dealsSearchLoadMore = MutableLiveData<Result<EventSearch>>()
    val dealsLoadMoreResponse: LiveData<Result<EventSearch>>
        get() = _dealsSearchLoadMore

    fun getInitialData(location: Location?,
                       childCategoryIds: String?) {
        val currentLocation = getLocationOrDefault(location)
        dealsLoadInitialData.getDealsInitialLoadResult(onSuccessLoadInitialData(), onErrorLoadInitialData(),
                currentLocation.coordinates,
                currentLocation.locType.name,
                childCategoryIds)
    }

    fun loadMoreData(searchQuery: String,
                     location: Location?,
                     childCategoryIds: String?,
                     page: Int){
        val currentLocation = getLocationOrDefault(location)
        val rawQuery = DealsSearchGqlQueries.getSearchLoadModeQuery()
        dealsSearchUseCase.getDealsSearchResult(onSuccessLoadMoreData(), onErrorLoadMoreData(),
                searchQuery,
                currentLocation.coordinates,
                currentLocation.locType.name,
                childCategoryIds,
                page.toString(),
                rawQuery,
                DealsSearchConstants.TREE_PRODUCT)
    }

    fun searchDeals(searchQuery: String,
                    location: Location?,
                    childCategoryIds: String?,
                    page: Int){
        val currentLocation = getLocationOrDefault(location)
        val rawQuery = DealsSearchGqlQueries.getEventSearchQuery()
        dealsSearchUseCase.getDealsSearchResult(onSuccessSearch(), onErrorSearch(),
                searchQuery,
                currentLocation.coordinates,
                currentLocation.locType.name,
                childCategoryIds,
                page.toString(),
                rawQuery,
                DealsSearchConstants.TREE_BRAND_PRODUCT)
    }

    private fun getLocationOrDefault(location: Location?): Location {
        var currentLocation = Location()
        if (location == null || location.coordinates.isEmpty()) {
            currentLocation.id = DealsLocationUtils.DEFAULT_LOCATION_ID
            currentLocation.cityName = DealsLocationUtils.DEFAULT_LOCATION_NAME
            currentLocation.coordinates = DealsLocationUtils.DEFAULT_LOCATION_COORDINATES
            currentLocation.locType.name = DEFAULT_LOCATION_TYPE
        } else {
            currentLocation = location
        }
        return currentLocation
    }

    private fun onErrorLoadInitialData(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsSearchInitialLoad.value = Fail(it)
        }
    }

    private fun onSuccessLoadInitialData(): (InitialLoadData) -> Unit {
        return {
            _dealsSearchInitialLoad.value = Success(it)
        }
    }

    private fun onErrorLoadMoreData(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsSearchLoadMore.value = Fail(it)
        }
    }

    private fun onSuccessLoadMoreData(): (SearchData) -> Unit {
        return {
            _dealsSearchLoadMore.value = Success(it.eventSearch)
        }
    }

    private fun onErrorSearch(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsSearchResponse.value = Fail(it)
        }
    }

    private fun onSuccessSearch(): (SearchData) -> Unit {
        return {
            _dealsSearchResponse.value = Success(it.eventSearch)
        }
    }

    override fun onCleared() {
        super.onCleared()
        dealsLoadInitialData.cancelJobs()
        dealsSearchUseCase.cancelJobs()
    }

    companion object {
        const val DEFAULT_LOCATION_TYPE = "city"
    }
}