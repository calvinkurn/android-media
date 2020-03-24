package com.tokopedia.flight.bookingV2.presentation.contract

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.flight.bookingV2.presentation.model.*
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataModel
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import java.util.*

/**
 * @author by furqan on 06/03/19
 */
interface FlightBookingContract {

    interface View : FlightBaseBookingContract.View {

        fun getViewContext(): Context

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

        fun getCurrentBookingParamViewModel(): FlightBookingParamModel

        fun getPriceViewModel(): FlightPriceModel

        fun showAndRenderReturnTripCardDetail(searchParam: FlightSearchPassDataModel, returnTrip: FlightDetailModel)

        fun showAndRenderDepartureTripCardDetail(searchParam: FlightSearchPassDataModel, departureTrip: FlightDetailModel)

        fun renderPassengersList(passengerModels: List<FlightBookingPassengerModel>)

        fun getDepartureTripId(): String

        fun getReturnTripId(): String

        fun navigateToDetailTrip(departureTrip: FlightDetailModel)

        fun getIdEmpotencyKey(tokenId: String): String

        fun showFullPageLoading()

        fun hideFullPageLoading()

        fun setCartData(flightBookingCartData: FlightBookingCartData)

        fun getCurrentCartPassData(): FlightBookingCartData

        override fun renderPriceListDetails(prices: List<SimpleModel>)

        fun renderTotalPrices(totalPrice: String)

        fun showGetCartDataErrorStateLayout(t: Throwable)

        override fun renderFinishTimeCountDown(date: Date)

        override fun showExpireTransactionDialog(message: String)

        override fun showPriceChangesDialog(newTotalPrice: String, oldTotalPrice: String)

        fun navigateToReview(flightBookingReviewModel: FlightBookingReviewModel)

        override fun showUpdatePriceLoading()

        override fun showUpdateDataErrorStateLayout(t: Throwable)

        fun showPassengerInfoNotFullfilled(@StringRes resId: Int)

        fun navigateToPassengerInfoDetail(model: FlightBookingPassengerModel,
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

        fun renderInsurance(insurances: List<FlightInsuranceModel>)

        fun setCartId(id: String)

        fun renderTickerView(travelTickerViewModel: TravelTickerViewModel)

        fun showContactDataProgressBar()

        fun hideContactDataProgressBar()

    }

    interface Presenter : FlightBaseBookingContract.Presenter<View> {

        fun initialize()

        fun onGetProfileData()

        fun onButtonSubmitClicked()

        fun onPassengerResultReceived(passengerModel: FlightBookingPassengerModel)

        fun onDepartureInfoClicked()

        fun onReturnInfoClicked()

        fun onRetryGetCartData()

        fun onDestroyView()

        fun onFinishTransactionTimeReached()

        fun onChangePassengerButtonClicked(model: FlightBookingPassengerModel, departureDate: String)

        fun onReceiveOtpSuccessResult()

        fun onReceiveOtpCancelResult()

        fun onInsuranceChanges(insurance: FlightInsuranceModel, checked: Boolean)

        fun onMoreInsuranceInfoClicked()

        fun onInsuranceBenefitExpanded()

        fun renderUi(flightBookingCartData: FlightBookingCartData?, isFromSavedInstance: Boolean)

        fun fetchTickerData()

        fun onContactDataResultRecieved(contactData: TravelContactData)

    }

}