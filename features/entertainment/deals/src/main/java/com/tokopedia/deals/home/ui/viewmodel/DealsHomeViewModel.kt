package com.tokopedia.deals.home.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.deals.common.data.DealsNearestLocationParam
import com.tokopedia.deals.common.domain.GetNearestLocationUseCase
import com.tokopedia.deals.common.model.response.Brand
import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.deals.home.data.DealsEventHome
import com.tokopedia.deals.home.data.EventHomeLayout
import com.tokopedia.deals.home.domain.GetEventHomeBrandPopularUseCase
import com.tokopedia.deals.home.domain.GetEventHomeLayoutUseCase
import com.tokopedia.deals.home.domain.GetInitialHomeLayoutModelUseCase
import com.tokopedia.deals.home.util.DealsHomeMapper
import com.tokopedia.deals.location_picker.DealsLocationConstants.LANDMARK
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 19/06/20
 */

class DealsHomeViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                             private val dealsHomeMapper: DealsHomeMapper,
                                             private val getEventHomeLayoutUseCase: GetEventHomeLayoutUseCase,
                                             private val getEventHomeBrandPopularUseCase: GetEventHomeBrandPopularUseCase,
                                             private val getNearestLocationUseCase: GetNearestLocationUseCase)
    : BaseViewModel(dispatcher.main) {

    private val _observableEventHomeLayout = MutableLiveData<Result<List<DealsBaseItemDataView>>>()
    val observableEventHomeLayout: LiveData<Result<List<DealsBaseItemDataView>>>
        get() = _observableEventHomeLayout

    var mutableTickerData = DealsEventHome.TickerHome()

    init {
        _observableEventHomeLayout.postValue(Success(GetInitialHomeLayoutModelUseCase.requestEmptyViewModels()))
    }

    fun getLayout(location: Location) {
        _observableEventHomeLayout.postValue(Success(GetInitialHomeLayoutModelUseCase.requestEmptyViewModels()))
        launch {
            try {
                val eventHomeLayouts = getEventHomeLayout(location)
                val brands = getBrandPopular(location)
                val location = getNearestLocation(location)

                val homeLayout = dealsHomeMapper.mapLayoutToBaseItemViewModel(eventHomeLayouts, brands, location, mutableTickerData)
                _observableEventHomeLayout.postValue(Success(homeLayout))
            } catch (t: Throwable) {
                _observableEventHomeLayout.postValue(Fail(t))
            }
        }
    }

    private suspend fun getEventHomeLayout(location: Location): List<EventHomeLayout> {
        try {
            val data = getEventHomeLayoutUseCase.apply {
                useParams(GetEventHomeLayoutUseCase.createParams(location.coordinates, location.locType.name))
            }.executeOnBackground()
            setTickerData(data.response.ticker)
            return data.response.layout
        } catch (t: Throwable) {
            throw t
        }
    }

    private suspend fun getBrandPopular(location: Location): List<Brand> {
        return try {
            val data = getEventHomeBrandPopularUseCase.apply {
                useParams(GetEventHomeBrandPopularUseCase.createParams(location.coordinates, location.locType.name, POPULAR_BRAND_SIZE))
            }.executeOnBackground()
            data.eventSearch.brands
        } catch (t: Throwable) {
            emptyList()
        }
    }

    private suspend fun getNearestLocation(location: Location): List<Location> {
        return try {
            if (location.locType.name != LANDMARK) {
                val data = getNearestLocationUseCase.apply {
                    useParams(GetNearestLocationUseCase.createParams(
                            DealsNearestLocationParam.VALUE_LOCATION_TYPE_LANDMARK,
                            location.coordinates, NEAREST_LOCATION_SIZE_NUM_VALUE,
                            NEAREST_LOCATION_PAGE_NUM_VALUE,
                            DealsNearestLocationParam.VALUE_CATEGORY_ID_DEFAULT,
                            DealsNearestLocationParam.VALUE_DISTANCE_20KM))
                }.executeOnBackground()
                data.eventLocationSearch.locations
            } else emptyList()
        } catch (t: Throwable) {
            emptyList()
        }
    }

    private fun setTickerData(tickerData: DealsEventHome.TickerHome){
        mutableTickerData = tickerData
    }

    companion object {
        const val NEAREST_LOCATION_SIZE_NUM_VALUE = "4"
        const val NEAREST_LOCATION_PAGE_NUM_VALUE = "1"
        const val NEAREST_LOCATION_FIXED_VALUE = "true"

        const val POPULAR_BRAND_SIZE = "16"
    }

}