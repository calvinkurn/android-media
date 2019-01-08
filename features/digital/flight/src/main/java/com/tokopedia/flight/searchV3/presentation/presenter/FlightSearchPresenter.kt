package com.tokopedia.flight.searchV3.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchContract

/**
 * @author by furqan on 07/01/19
 */
class FlightSearchPresenter: BaseDaggerPresenter<FlightSearchContract.View>(),
    FlightSearchContract.Presenter {

}