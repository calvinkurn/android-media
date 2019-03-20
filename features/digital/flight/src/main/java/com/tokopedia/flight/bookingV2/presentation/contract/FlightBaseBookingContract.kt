package com.tokopedia.flight.bookingV2.presentation.contract

import android.support.annotation.StringRes
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import java.util.*

/**
 * @author by furqan on 04/03/19
 */
interface FlightBaseBookingContract {

    interface View: CustomerView {

        fun showPriceChangesDialog(newTotalPrice: String, oldTotalPrice: String)

        fun hideUpdatePriceDialog() // show update data loading

        fun showUpdatePriceLoading()

        fun getDepartureFlightDetailViewModel(): FlightDetailViewModel

        fun getReturnFlightDetailViewModel(): FlightDetailViewModel?

        fun getFlightBookingPassengers(): List<FlightBookingPassengerViewModel>

        fun renderPriceListDetails(simpleViewModels: List<SimpleViewModel>)

        fun renderFinishTimeCountDown(date: Date)

        fun showUpdateDataErrorStateLayout(t: Throwable)

        fun showExpireTransactionDialog(message: String)

        fun getCartId(): String

        fun showSoldOutDialog()

        fun getString(@StringRes idRes: Int): String

    }

    interface Presenter<T : View>: CustomerPresenter<T> {

        fun onGetCart(shouldReRenderUi: Boolean)

        fun renderUi()

    }

}