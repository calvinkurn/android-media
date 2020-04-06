package com.tokopedia.flight.bookingV2.presentation.contract

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingCartData
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingPassengerModel
import com.tokopedia.flight.bookingV2.presentation.model.SimpleModel
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import java.util.*

/**
 * @author by furqan on 04/03/19
 */
interface FlightBaseBookingContract {

    interface View: CustomerView {

        fun showPriceChangesDialog(newTotalPrice: String, oldTotalPrice: String)

        fun hideUpdatePriceDialog() // show update data loading

        fun showUpdatePriceLoading()

        fun getDepartureFlightDetailViewModel(): FlightDetailModel

        fun getReturnFlightDetailViewModel(): FlightDetailModel?

        fun getFlightBookingPassengers(): List<FlightBookingPassengerModel>

        fun renderPriceListDetails(simpleModels: List<SimpleModel>)

        fun renderFinishTimeCountDown(date: Date)

        fun showUpdateDataErrorStateLayout(t: Throwable)

        fun showExpireTransactionDialog(message: String)

        fun getCartId(): String

        fun showSoldOutDialog()

        fun getString(@StringRes idRes: Int): String

    }

    interface Presenter<T : View>: CustomerPresenter<T> {

        fun onGetCart(shouldReRenderUi: Boolean, flightBookingCartData: FlightBookingCartData?)

        fun renderUi()

    }

}