package com.tokopedia.flight.bookingV2.presentation.contract

import androidx.annotation.StringRes
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.flight.bookingV2.presentation.viewmodel.*
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import java.util.*

/**
 * @author by furqan on 06/03/19
 */
interface FlightBookingContract {

    interface View : FlightBaseBookingContract.View {

        fun getContactName(): String

        fun setContactName(fullname: String)

        fun showContactNameEmptyError(@StringRes resId: Int)

        fun showContactNameInvalidError(@StringRes resId: Int)

        fun getContactEmail(): String

        fun setContactEmail(email: String)

        fun showContactEmailEmptyError(@StringRes resId: Int)

        fun showContactEmailInvalidError(@StringRes resId: Int)

        fun getContactPhoneNumber(): String

        fun setContactPhoneNumber(phone: String)

        fun setContactPhoneNumber(phone: String, phoneCode: Int)

        fun showContactPhoneNumberEmptyError(@StringRes resId: Int)

        fun showContactPhoneNumberInvalidError(@StringRes resId: Int)

        fun getCurrentBookingParamViewModel(): FlightBookingParamViewModel

        fun getPriceViewModel(): FlightPriceViewModel

        fun showAndRenderReturnTripCardDetail(searchParam: FlightSearchPassDataViewModel, returnTrip: FlightDetailViewModel)

        fun showAndRenderDepartureTripCardDetail(searchParam: FlightSearchPassDataViewModel, departureTrip: FlightDetailViewModel)

        fun renderPassengersList(passengerViewModels: List<FlightBookingPassengerViewModel>)

        fun getDepartureTripId(): String

        fun getReturnTripId(): String

        fun navigateToDetailTrip(departureTrip: FlightDetailViewModel)

        fun getIdEmpotencyKey(tokenId: String): String

        fun showFullPageLoading()

        fun hideFullPageLoading()

        fun setCartData(flightBookingCartData: FlightBookingCartData)

        fun getCurrentCartPassData(): FlightBookingCartData

        override fun renderPriceListDetails(prices: List<SimpleViewModel>)

        fun renderTotalPrices(totalPrice: String)

        fun showGetCartDataErrorStateLayout(t: Throwable)

        override fun renderFinishTimeCountDown(date: Date)

        override fun showExpireTransactionDialog(message: String)

        override fun showPriceChangesDialog(newTotalPrice: String, oldTotalPrice: String)

        fun navigateToReview(flightBookingReviewModel: FlightBookingReviewModel)

        override fun showUpdatePriceLoading()

        override fun showUpdateDataErrorStateLayout(t: Throwable)

        fun showPassengerInfoNotFullfilled(@StringRes resId: Int)

        fun navigateToPassengerInfoDetail(viewModel: FlightBookingPassengerViewModel,
                                          isMandatoryDoB: Boolean, departureDate: String,
                                          requestId: String)

        fun setContactBirthdate(birthdate: String)

        fun getContactBirthdate(): String

        fun setContactGender(gender: Int)

        fun getContactGender(): Int

        fun showContactEmailInvalidSymbolError(@StringRes resId: Int)

        fun navigateToOtpPage()

        fun closePage()

        fun getExpiredTransactionDate(): Date

        fun showInsuranceLayout()

        fun hideInsuranceLayout()

        fun renderInsurance(insurances: List<FlightInsuranceViewModel>)

        fun setCartId(id: String)

        fun renderTickerView(travelTickerViewModel: TravelTickerViewModel)

        fun showContactDataProgressBar()

        fun hideContactDataProgressBar()

    }

    interface Presenter : FlightBaseBookingContract.Presenter<View> {

        fun initialize()

        fun onGetProfileData()

        fun onButtonSubmitClicked()

        fun onPassengerResultReceived(passengerViewModel: FlightBookingPassengerViewModel)

        fun onDepartureInfoClicked()

        fun onReturnInfoClicked()

        fun onRetryGetCartData()

        fun onDestroyView()

        fun onFinishTransactionTimeReached()

        fun onChangePassengerButtonClicked(viewModel: FlightBookingPassengerViewModel, departureDate: String)

        fun onReceiveOtpSuccessResult()

        fun onReceiveOtpCancelResult()

        fun onInsuranceChanges(insurance: FlightInsuranceViewModel, checked: Boolean)

        fun onMoreInsuranceInfoClicked()

        fun onInsuranceBenefitExpanded()

        fun renderUi(flightBookingCartData: FlightBookingCartData?, isFromSavedInstance: Boolean)

        fun fetchTickerData()

        fun onContactDataResultRecieved(contactData: TravelContactData)

    }

}