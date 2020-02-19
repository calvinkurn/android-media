package com.tokopedia.flight.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.resultstatistics.FlightSearchStatisticModel
import javax.inject.Inject

/**
 * @author by furqan on 19/02/2020
 */
class FlightFilterViewModel @Inject constructor(private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

    private val mutableFilterModel = MutableLiveData<FlightFilterModel>()
    val filterModel: LiveData<FlightFilterModel>
        get() = mutableFilterModel

    private val mutableStatisticModel = MutableLiveData<FlightSearchStatisticModel>()
    val statisticModel: LiveData<FlightSearchStatisticModel>
        get() = mutableStatisticModel

    fun init(filterModel: FlightFilterModel) {
        mutableFilterModel.value = filterModel
    }

    fun resetFilter() {
        // TODO: Reset Filter Value
    }
}