package com.tokopedia.flight.orderdetail.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider

/**
 * @author by furqan on 19/10/2020
 */
class FlightOrderDetailViewModel(dispatcherProvider: TravelDispatcherProvider)
    : BaseViewModel(dispatcherProvider.io()) {
}