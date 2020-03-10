package com.tokopedia.flight.search_universal.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import javax.inject.Inject

/**
 * @author by furqan on 10/03/2020
 */
class FlightSearchUniversalViewModel @Inject constructor(
        private val dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {

}