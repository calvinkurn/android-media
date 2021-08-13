package com.tokopedia.deals.brand.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.brand.mapper.DealsBrandMapper.mapBrandToBaseItemViewModel
import com.tokopedia.deals.common.domain.DealsSearchUseCase
import com.tokopedia.deals.common.model.response.SearchData
import com.tokopedia.deals.common.ui.dataview.DealsBrandsDataView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.search.domain.DealsSearchGqlQueries
import com.tokopedia.deals.search.domain.viewmodel.DealsSearchViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DealsBrandViewModel @Inject constructor (
        private val dealsSearchUseCase: DealsSearchUseCase,
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _dealsSearchResponse = MutableLiveData<Result<DealsBrandsDataView>>()
    val dealsSearchResponse: LiveData<Result<DealsBrandsDataView>>
        get() = _dealsSearchResponse

    private val _dealsShimmerData = MutableLiveData<Result<List<DealsBrandsDataView>>>()
    val dealsShimmerData: LiveData<Result<List<DealsBrandsDataView>>>
        get() = _dealsShimmerData

    init {
        getInitialData()
    }

    fun getBrandList(searchQuery: String,
                     location: Location?,
                     childCategoryIds: String?,
                     page: Int) {
        val currentLocation = getLocationOrDefault(location)
        val rawQuery = DealsSearchGqlQueries.getEventSearchQuery()
        dealsSearchUseCase.getDealsSearchResult(onSuccessSearch(page), onErrorSearch(),
                searchQuery,
                currentLocation.coordinates,
                currentLocation.locType.name,
                childCategoryIds,
                page.toString(),
                rawQuery,
                TREE_BRAND)
    }

    private fun onErrorSearch(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsSearchResponse.value = Fail(it)
        }
    }

    private fun onSuccessSearch(page: Int): (SearchData) -> Unit {
        return {
            _dealsSearchResponse.value = Success(mapBrandToBaseItemViewModel(it.eventSearch.brands, page))
        }
    }

    private fun getLocationOrDefault(location: Location?): Location {
        var currentLocation = Location()
        if (location == null || location.coordinates.isEmpty()) {
            currentLocation.id = DealsLocationUtils.DEFAULT_LOCATION_ID
            currentLocation.cityName = DealsLocationUtils.DEFAULT_LOCATION_NAME
            currentLocation.coordinates = DealsLocationUtils.DEFAULT_LOCATION_COORDINATES
            currentLocation.locType.name = DealsSearchViewModel.DEFAULT_LOCATION_TYPE
        } else {
            currentLocation = location
        }
        return currentLocation
    }

    fun getInitialData() {
        _dealsShimmerData.value = Success(listOf(DealsBrandsDataView()))
    }

    override fun onCleared() {
        super.onCleared()
        dealsSearchUseCase.cancelJobs()
    }

    companion object {
        const val TREE_BRAND = "brand"
    }
}