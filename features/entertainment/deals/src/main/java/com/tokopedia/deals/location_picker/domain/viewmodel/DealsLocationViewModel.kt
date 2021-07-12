package com.tokopedia.deals.location_picker.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.location_picker.domain.usecase.*
import com.tokopedia.deals.location_picker.model.response.EventLocationSearch
import com.tokopedia.deals.location_picker.model.response.LocationData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class DealsLocationViewModel @Inject constructor (
        private val dealsSearchLocationUseCase: DealsSearchLocationUseCase,
        private val dealsPopularCitiesUseCase: DealsPopularCitiesUseCase,
        private val dealsPopularLocationUseCase: DealsPopularLocationUseCase,
        private val dealsLandmarkLocationUseCase: DealsLandmarkLocationUseCase,
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _dealsSearchedLocationResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsSearchedLocationResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsSearchedLocationResponse

    private val _dealsLoadMoreSearchedLocationResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsLoadMoreSearchedLocationResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsLoadMoreSearchedLocationResponse

    private val _dealsPopularCitiesResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsPopularCitiesResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsPopularCitiesResponse

    private val _dealsPopularLocationResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsPopularLocationResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsPopularLocationResponse

    private val _dealsLoadMorePopularLocationResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsLoadMorePopularLocationResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsLoadMorePopularLocationResponse

    private val _dealsDataLandmarkLocationResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsDataLandmarkLocationResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsDataLandmarkLocationResponse

    private val _dealsLoadMoreDataLandmarkLocationResponse = MutableLiveData<Result<EventLocationSearch>>()
    val dealsLoadMoreDataLandmarkLocationResponse: LiveData<Result<EventLocationSearch>>
        get() = _dealsLoadMoreDataLandmarkLocationResponse

    fun getSearchedLocation(keyword: String) {
        dealsSearchLocationUseCase.getSearchedLocation(onSuccessGetSearchedLocation(), onErrorGetSearchedLocation(), keyword, "1")
    }

    fun getLoadMoreSearchedLocation(keyword: String, page: String) {
        dealsSearchLocationUseCase.getSearchedLocation(onSuccessLoadMoreSearchedLocation(), onErrorLoadMoreSearchedLocation(), keyword, page)
    }

    fun getInitialPopularCities() {
        dealsPopularCitiesUseCase.getPopularCities(onSuccessGetPopularCities(), onErrorGetPopularCities())
    }

    fun getInitialPopularLocation(coordinates: String) {
        dealsPopularLocationUseCase.getPopularLocations(onSuccessGetPopularLocation(), onErrorGetPopularLocation(), coordinates, "1")
    }

    fun getLoadMoreDataLocation(coordinates: String, page: String) {
        dealsPopularLocationUseCase.getPopularLocations(onSuccessLoadMorePopularLocation(), onErrorLoadMorePopularLocation(), coordinates, page)
    }

    fun getInitialDataLandmarkLocation(coordinates: String) {
        dealsLandmarkLocationUseCase.getLandmarkLocation(onSuccessGetLandmarkLocation(), onErrorGetLandmarkLocation(), coordinates, "1")
    }

    fun getLoadMoreDataLandmarkLocation(coordinates: String, page: String) {
        dealsLandmarkLocationUseCase.getLandmarkLocation(onSuccessLoadMoreLandmarkLocation(), onErrorLoadMoreLandmarkLocation(), coordinates, page)
    }

    private fun onErrorGetSearchedLocation(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsSearchedLocationResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetSearchedLocation(): (LocationData) -> Unit {
        return {
            _dealsSearchedLocationResponse.value = Success(it.eventLocationSearch)
        }
    }

    private fun onErrorLoadMoreSearchedLocation(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsLoadMoreSearchedLocationResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoadMoreSearchedLocation(): (LocationData) -> Unit {
        return {
            _dealsLoadMoreSearchedLocationResponse.value = Success(it.eventLocationSearch)
        }
    }

    private fun onErrorGetPopularCities(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsPopularCitiesResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetPopularCities(): (LocationData) -> Unit {
        return {
            _dealsPopularCitiesResponse.value = Success(it.eventLocationSearch)
        }
    }

    private fun onErrorGetPopularLocation(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsPopularLocationResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetPopularLocation(): (LocationData) -> Unit {
        return {
            _dealsPopularLocationResponse.value = Success(it.eventLocationSearch)
        }
    }

    private fun onErrorLoadMorePopularLocation(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsLoadMorePopularLocationResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoadMorePopularLocation(): (LocationData) -> Unit {
        return {
            _dealsLoadMorePopularLocationResponse.value = Success(it.eventLocationSearch)
        }
    }

    private fun onErrorGetLandmarkLocation(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsDataLandmarkLocationResponse.value = Fail(it)
        }
    }

    private fun onSuccessGetLandmarkLocation(): (LocationData) -> Unit {
        return {
            _dealsDataLandmarkLocationResponse.value = Success(it.eventLocationSearch)
        }
    }

    private fun onErrorLoadMoreLandmarkLocation(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            _dealsLoadMoreDataLandmarkLocationResponse.value = Fail(it)
        }
    }

    private fun onSuccessLoadMoreLandmarkLocation(): (LocationData) -> Unit {
        return {
            _dealsLoadMoreDataLandmarkLocationResponse.value = Success(it.eventLocationSearch)
        }
    }

    override fun onCleared() {
        super.onCleared()
        dealsSearchLocationUseCase.cancelJobs()
        dealsPopularCitiesUseCase.cancelJobs()
        dealsPopularLocationUseCase.cancelJobs()
        dealsLandmarkLocationUseCase.cancelJobs()
    }
}