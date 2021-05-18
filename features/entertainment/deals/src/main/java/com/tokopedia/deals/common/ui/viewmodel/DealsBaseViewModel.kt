package com.tokopedia.deals.common.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.common.domain.GetNearestLocationUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.common.utils.DealsLocationUtils
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.deals.location_picker.model.response.LocationType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 15/06/20
 */

class DealsBaseViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                             private val getNearestLocationUseCase: GetNearestLocationUseCase)
    : BaseViewModel(dispatcher.main) {

    // fragments may also observe location to determined whether the location changes.
    private val _observableCurrentLocation = MutableLiveData<Location>()
    val observableCurrentLocation: LiveData<Location>
        get() = _observableCurrentLocation

    fun getCurrentLocation(coordinates: String) {
        launch {
            try {
                getNearestLocationUseCase.useParams(GetNearestLocationUseCase.createParams(coordinates, "1"))
                val data = getNearestLocationUseCase.executeOnBackground()

                if (data.eventLocationSearch.locations.isNotEmpty()) {
                    _observableCurrentLocation.postValue(data.eventLocationSearch.locations.first())
                }
            } catch (t: Throwable) {
                _observableCurrentLocation.postValue(Location(
                        id = DealsLocationUtils.DEFAULT_LOCATION_ID,
                        cityId = DealsLocationUtils.DEFAULT_LOCATION_ID,
                        name = DealsLocationUtils.DEFAULT_LOCATION_NAME,
                        cityName = DealsLocationUtils.DEFAULT_LOCATION_NAME,
                        coordinates = DealsLocationUtils.DEFAULT_LOCATION_COORDINATES,
                        locType = LocationType(name = DealsLocationUtils.DEFAULT_LOCATION_CITY)))
            }
        }
    }

    fun setCurrentLocation(location: Location) {
        _observableCurrentLocation.postValue(location)
    }
}