package com.tokopedia.flight.searchV3.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.flight.searchV3.presentation.contract.FlightSearchReturnContract

/**
 * @author by furqan on 14/01/19
 */
class FlightSearchReturnPresenter : BaseDaggerPresenter<FlightSearchReturnContract.View>(),
        FlightSearchReturnContract.Presenter {
}