package com.tokopedia.flight.bookingV2.presentation.contract

import android.support.annotation.StringRes
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo
import com.tokopedia.flight.booking.view.viewmodel.*
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import rx.Observable
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

        fun showContactPhoneNumberEmptyError(@StringRes resId: Int)

        fun showContactPhoneNumberInvalidError(@StringRes resId: Int)

        fun getCurrentBookingParamViewModel(): FlightBookingParamViewModel

        fun getPriceViewModel(): FlightPriceViewModel

        fun showAndRenderReturnTripCardDetail(searchParam: FlightSearchPassDataViewModel, returnTrip: FlightDetailViewModel)

        fun showAndRenderDepartureTripCardDetail(searchParam: FlightSearchPassDataViewModel, departureTrip: FlightDetailViewModel)

        fun renderPassengersList(passengerViewModels: List<FlightBookingPassengerViewModel>)

        fun renderPhoneCodeView(countryPhoneCode: String)

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

        fun getProfileObservable(): Observable<ProfileInfo>

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

    }

    interface Presenter : FlightBaseBookingContract.Presenter<View> {

        fun initialize()

        fun onGetProfileData()

        fun onButtonSubmitClicked()

        fun onPhoneCodeResultReceived(phoneCodeViewModel: FlightBookingPhoneCodeViewModel)

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

    }

}