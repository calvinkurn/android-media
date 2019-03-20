package com.tokopedia.flight.bookingV2.presentation.presenter

import android.util.Patterns
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.constant.FlightBookingPassenger
import com.tokopedia.flight.booking.domain.FlightBookingGetPhoneCodeUseCase
import com.tokopedia.flight.booking.domain.subscriber.model.ProfileInfo
import com.tokopedia.flight.booking.view.viewmodel.*
import com.tokopedia.flight.bookingV2.data.entity.AddToCartEntity
import com.tokopedia.flight.bookingV2.domain.FlightAddToCartV11UseCase
import com.tokopedia.flight.bookingV2.domain.FlightGetCartDataUseCase
import com.tokopedia.flight.bookingV2.presentation.contract.FlightBookingContract
import com.tokopedia.flight.bookingV2.presentation.viewmodel.mapper.FlightBookingCartDataMapper
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.data.model.FlightException
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel
import com.tokopedia.flight.search.data.api.single.response.Fare
import com.tokopedia.flight.search.domain.usecase.FlightSearchJourneyByIdUseCase
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.functions.Func2
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * @author by furqan on 04/03/19
 */
class FlightBookingPresenter @Inject constructor(val flightAddToCartUseCase: FlightAddToCartV11UseCase,
                                                 private val flightBookingGetPhoneCodeUseCase: FlightBookingGetPhoneCodeUseCase,
                                                 val flightAnalytics: FlightAnalytics,
                                                 val userSession: UserSessionInterface,
                                                 val flightSearchJourneyByIdUseCase: FlightSearchJourneyByIdUseCase,
                                                 flightGetCartDataUseCase: FlightGetCartDataUseCase,
                                                 flightBookingCartDataMapper: FlightBookingCartDataMapper)
    : FlightBaseBookingPresenter<FlightBookingContract.View>(flightGetCartDataUseCase, flightBookingCartDataMapper), FlightBookingContract.Presenter {

    private val compositeSubscription = CompositeSubscription()

    override fun initialize() {
        if (userSession.isMsisdnVerified) {
            processGetCartData()
            onGetProfileData()
        } else {
            view.navigateToOtpPage()
        }
    }

    override fun getCurrentCartData(): BaseCartData {
        val baseCartData = BaseCartData()
        baseCartData.id = view.getCurrentCartPassData().id
        baseCartData.newFarePrices = view.getCurrentCartPassData().newFarePrices
        val amenityViewModels = arrayListOf<FlightBookingAmenityViewModel>()
        if (view.getCurrentBookingParamViewModel().passengerViewModels != null) {
            for (passenger in view.getCurrentBookingParamViewModel().passengerViewModels) {
                for (luggageAmenities in passenger.flightBookingLuggageMetaViewModels) {
                    amenityViewModels.addAll(luggageAmenities.amenities)
                }
                for (mealAmenities in passenger.flightBookingMealMetaViewModels) {
                    amenityViewModels.addAll(mealAmenities.amenities)
                }
            }
        }
        baseCartData.amenities = amenityViewModels
        baseCartData.total = view.getCurrentBookingParamViewModel().totalPriceNumeric
        baseCartData.adult = view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.adult
        baseCartData.child = view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.children
        baseCartData.infant = view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.infant
        return baseCartData
    }

    override fun updateTotalPrice(totalPrice: Int) {
        view.getCurrentBookingParamViewModel().totalPriceNumeric = totalPrice
        view.getCurrentBookingParamViewModel().totalPriceFmt =
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(totalPrice)
        view.renderTotalPrices(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(totalPrice))
    }

    override fun onCountDownTimestampChanged(timestamp: String) {
        view.getCurrentBookingParamViewModel().orderDueTimestamp = timestamp
    }

    override fun getInsurances(): List<FlightInsuranceViewModel> =
            view.getCurrentBookingParamViewModel().insurances

    override fun getComboKey(): String = view.getPriceViewModel().comboKey

    override fun getRequestParams(): RequestParams {
        return getRequestParams(calculateTotalPassengerFare())
    }

    override fun onFinishTransactionTimeReached() {
        if (isViewAttached) {
            onGetCart(false, view.getCurrentCartPassData())
        }
    }

    override fun onGetProfileData() {
        compositeSubscription.add(view.getProfileObservable()
                .onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ProfileInfo>() {
                    override fun onNext(profileInfo: ProfileInfo?) {
                        if (profileInfo != null && isViewAttached) {
                            if (view.getContactName().isEmpty()) {
                                view.setContactName(profileInfo.fullname)
                            }
                            if (view.getContactEmail().isEmpty()) {
                                view.setContactEmail(profileInfo.email)
                            }
                            if (view.getContactPhoneNumber().isEmpty()) {
                                view.setContactPhoneNumber(transform(profileInfo.phoneNumber))
                            }
                            view.setContactBirthdate(
                                    FlightDateUtil.dateToString(
                                            FlightDateUtil.stringToDate(profileInfo.bday),
                                            FlightDateUtil.DEFAULT_FORMAT
                                    )
                            )
                            view.setContactGender(profileInfo.gender)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        e?.printStackTrace()
                    }
                }))
    }

    override fun onButtonSubmitClicked() {
        if (validateFields()) {
            flightAnalytics.eventBookingNextClick(view.getString(R.string.flight_booking_analytics_customer_page))
            view.getCurrentBookingParamViewModel().contactName = view.getContactName()
            view.getCurrentBookingParamViewModel().contactEmail = view.getContactEmail()
            view.getCurrentBookingParamViewModel().contactPhone = view.getContactPhoneNumber()

            view.navigateToReview(FlightBookingReviewModel(
                    view.getCurrentBookingParamViewModel(),
                    view.getCurrentCartPassData(),
                    view.getDepartureTripId(),
                    view.getReturnTripId(),
                    view.getString(R.string.flight_luggage_prefix),
                    view.getString(R.string.flight_meal_prefix),
                    view.getString(R.string.flight_birthdate_prefix),
                    view.getString(R.string.flight_passenger_passport_number_hint)
            ))
        }
    }

    override fun onPhoneCodeResultReceived(phoneCodeViewModel: FlightBookingPhoneCodeViewModel) {
        view.getCurrentBookingParamViewModel().phoneCodeViewModel = phoneCodeViewModel
        view.renderPhoneCodeView(String.format("+%s", phoneCodeViewModel.countryPhoneCode))
    }

    override fun onPassengerResultReceived(passengerViewModel: FlightBookingPassengerViewModel) {
        val passengerViewModels = view.getCurrentBookingParamViewModel().passengerViewModels
        val indexPassenger = passengerViewModels.indexOf(passengerViewModel)
        if (indexPassenger != -1) {
            passengerViewModels[indexPassenger] = passengerViewModel
        }
        view.renderPassengersList(passengerViewModels)

        actionCalculatePriceAndRender(
                view.getCurrentCartPassData().newFarePrices,
                view.getCurrentCartPassData().departureTrip,
                view.getCurrentCartPassData().returnTrip,
                view.getCurrentBookingParamViewModel().passengerViewModels,
                view.getCurrentBookingParamViewModel().insurances
        )

        val newTotalPrice = actionCalculateCurrentTotalPrice(
                view.getDepartureFlightDetailViewModel(),
                view.getReturnFlightDetailViewModel())

        updateTotalPrice(newTotalPrice)
    }

    override fun onDepartureInfoClicked() {
        flightAnalytics.eventDetailClick(view.getCurrentCartPassData().departureTrip)
        view.navigateToDetailTrip(view.getCurrentCartPassData().departureTrip)
    }

    override fun onReturnInfoClicked() {
        flightAnalytics.eventDetailClick(view.getCurrentCartPassData().returnTrip)
        view.navigateToDetailTrip(view.getCurrentCartPassData().returnTrip)
    }

    override fun onChangePassengerButtonClicked(viewModel: FlightBookingPassengerViewModel, departureDate: String) {
        val requestId = if (view.getReturnTripId().isNotEmpty()) {
            view.getIdEmpotencyKey("${view.getDepartureTripId()}_${view.getReturnTripId()}")
        } else {
            view.getIdEmpotencyKey(view.getDepartureTripId())
        }
        view.navigateToPassengerInfoDetail(viewModel, isMandatoryDoB(), departureDate, requestId)
    }

    override fun onReceiveOtpSuccessResult() {
        initialize()
    }

    override fun onReceiveOtpCancelResult() {
        view.closePage()
    }

    override fun onRetryGetCartData() {
        onGetCart(true, view.getCurrentCartPassData())
    }

    override fun onInsuranceChanges(insurance: FlightInsuranceViewModel, checked: Boolean) {
        val insurances = if (view.getCurrentBookingParamViewModel().insurances != null) {
            view.getCurrentBookingParamViewModel().insurances
        } else {
            arrayListOf()
        }

        if (checked) {
            val index = insurances.indexOf(insurance)
            if (index == -1) {
                insurances.add(insurance)
            }
        } else {
            val index = insurances.indexOf(insurance)
            if (index != -1) {
                insurances.removeAt(index)
            }
        }
        view.getCurrentBookingParamViewModel().insurances = insurances

        actionCalculatePriceAndRender(view.getCurrentCartPassData().newFarePrices,
                view.getCurrentCartPassData().departureTrip,
                view.getCurrentCartPassData().returnTrip,
                view.getCurrentBookingParamViewModel().passengerViewModels,
                view.getCurrentBookingParamViewModel().insurances)

        val newTotalPrice = actionCalculateCurrentTotalPrice(
                view.getDepartureFlightDetailViewModel(),
                view.getReturnFlightDetailViewModel())
        updateTotalPrice(newTotalPrice)

        flightAnalytics.eventInsuranceChecked(checked,
                view.getDepartureFlightDetailViewModel(),
                view.getReturnFlightDetailViewModel())
    }

    override fun onMoreInsuranceInfoClicked() {
        flightAnalytics.eventInsuranceClickMore()
    }

    override fun onInsuranceBenefitExpanded() {
        flightAnalytics.eventInsuranceAnotherBenefit()
    }

    override fun renderUi() {
        renderUi(view.getCurrentCartPassData(), false)
    }

    override fun renderUi(flightBookingCartData: FlightBookingCartData?, isFromSavedInstance: Boolean) {
        if (flightBookingCartData != null && flightBookingCartData.id != null) {
            view.hideFullPageLoading()
            view.getCurrentBookingParamViewModel().id = flightBookingCartData.id
            view.setCartData(flightBookingCartData)
            view.showAndRenderDepartureTripCardDetail(view.getCurrentBookingParamViewModel().searchParam,
                    flightBookingCartData.departureTrip)
            if (isRoundTrip()) {
                view.showAndRenderReturnTripCardDetail(view.getCurrentBookingParamViewModel().searchParam,
                        flightBookingCartData.returnTrip)
            }
            if (view.getCurrentBookingParamViewModel().passengerViewModels == null ||
                    view.getCurrentBookingParamViewModel().passengerViewModels.size == 0) {
                val passengerViewModels = buildPassengerViewModel(view.getCurrentBookingParamViewModel().searchParam)
                view.getCurrentBookingParamViewModel().passengerViewModels = passengerViewModels
                view.renderPassengersList(passengerViewModels)
            }
            if (isFromSavedInstance) {
                view.renderPassengersList(view.getCurrentBookingParamViewModel().passengerViewModels)
                view.setContactName(view.getCurrentBookingParamViewModel().contactName)
                view.setContactEmail(view.getCurrentBookingParamViewModel().contactEmail)
                view.setContactPhoneNumber(view.getCurrentBookingParamViewModel().contactPhone)
                val expiredDate = view.getExpiredTransactionDate()
                if (expiredDate != null) {
                    view.getCurrentBookingParamViewModel().orderDueTimestamp = FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT)
                    view.renderFinishTimeCountDown(expiredDate)
                }
            } else {
                val expiredDate = FlightDateUtil.addTimeToCurrentDate(Calendar.SECOND, flightBookingCartData.refreshTime)
                view.getCurrentBookingParamViewModel().orderDueTimestamp = FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT)
                view.renderFinishTimeCountDown(expiredDate)
            }
            view.getCurrentBookingParamViewModel().phoneCodeViewModel = flightBookingCartData.defaultPhoneCode
            view.renderPhoneCodeView(String.format("+%s", view.getCurrentBookingParamViewModel().phoneCodeViewModel.countryPhoneCode))

            val oldTotalPrice = actionCalculateCurrentTotalPrice(flightBookingCartData.departureTrip, flightBookingCartData.returnTrip)
            var resultTotalPrice = oldTotalPrice

            if (flightBookingCartData.newFarePrices != null && flightBookingCartData.newFarePrices.isNotEmpty()) {
                for (newFarePrice in flightBookingCartData.newFarePrices) {
                    if (newFarePrice.id.equals(flightBookingCartData.departureTrip.id, true)) {
                        flightBookingCartData.departureTrip.adultNumericPrice = newFarePrice.fare.adultNumeric
                        flightBookingCartData.departureTrip.childNumericPrice = newFarePrice.fare.childNumeric
                        flightBookingCartData.departureTrip.infantNumericPrice = newFarePrice.fare.infantNumeric
                    } else if (isRoundTrip() && newFarePrice.id.equals(flightBookingCartData.returnTrip.id, true)) {
                        flightBookingCartData.returnTrip.adultNumericPrice = newFarePrice.fare.adultNumeric
                        flightBookingCartData.returnTrip.childNumericPrice = newFarePrice.fare.childNumeric
                        flightBookingCartData.returnTrip.infantNumericPrice = newFarePrice.fare.infantNumeric
                    } else if (view.getPriceViewModel().comboKey != null &&
                            view.getPriceViewModel().comboKey.isNotEmpty() &&
                            newFarePrice.id.equals(view.getPriceViewModel().comboKey, true)) {
                        val newAdultPrice = newFarePrice.fare.adultNumeric / 2
                        val newChildPrice = newFarePrice.fare.childNumeric / 2
                        val newInfantPrice = newFarePrice.fare.infantNumeric / 2

                        flightBookingCartData.departureTrip.adultNumericPrice = newAdultPrice
                        flightBookingCartData.departureTrip.childNumericPrice = newChildPrice
                        flightBookingCartData.departureTrip.infantNumericPrice = newInfantPrice

                        flightBookingCartData.returnTrip.adultNumericPrice = newFarePrice.fare.adultNumeric - newAdultPrice
                        flightBookingCartData.returnTrip.childNumericPrice = newFarePrice.fare.childNumeric - newChildPrice
                        flightBookingCartData.returnTrip.infantNumericPrice = newFarePrice.fare.infantNumeric - newInfantPrice
                    }
                }
                val newTotalPrice = actionCalculateCurrentTotalPrice(flightBookingCartData.departureTrip, flightBookingCartData.returnTrip)
                if (newTotalPrice != oldTotalPrice && getCurrentCartData().total > 0) {
                    resultTotalPrice = newTotalPrice
                    view.showPriceChangesDialog(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(resultTotalPrice), CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(oldTotalPrice))
                }
            }
            updateTotalPrice(resultTotalPrice)

            actionCalculatePriceAndRender(flightBookingCartData.newFarePrices,
                    flightBookingCartData.departureTrip,
                    flightBookingCartData.returnTrip,
                    view.getCurrentBookingParamViewModel().passengerViewModels,
                    view.getCurrentBookingParamViewModel().insurances
            )

            renderInsurance(flightBookingCartData, isFromSavedInstance)

            if (!isFromSavedInstance) {
                flightAnalytics.eventAddToCart(view.getCurrentBookingParamViewModel().searchParam.flightClass,
                        flightBookingCartData, resultTotalPrice, flightBookingCartData.departureTrip,
                        flightBookingCartData.returnTrip)
            }
        } else {
            initialize()
        }
    }

    override fun onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }
        flightAddToCartUseCase.unsubscribe()
        flightBookingGetPhoneCodeUseCase.unsubscribe()
        flightSearchJourneyByIdUseCase.unsubscribe()
        detachView()
    }

    private fun getRequestParams(price: Int) = if (isRoundTrip()) {
        flightAddToCartUseCase.createRequestParam(
                view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.adult,
                view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.children,
                view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.infant,
                view.getCurrentBookingParamViewModel().searchParam.flightClass.id,
                view.getDepartureTripId(),
                view.getReturnTripId(),
                view.getIdEmpotencyKey("${view.getDepartureTripId()}_${view.getReturnTripId()}"),
                price,
                view.getPriceViewModel().comboKey
        )
    } else {
        flightAddToCartUseCase.createRequestParam(
                view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.adult,
                view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.children,
                view.getCurrentBookingParamViewModel().searchParam.flightPassengerViewModel.infant,
                view.getCurrentBookingParamViewModel().searchParam.flightClass.id,
                view.getDepartureTripId(),
                view.getIdEmpotencyKey(view.getDepartureTripId()),
                price
        )
    }

    private fun processGetCartData() {
        view.showFullPageLoading()
        compositeSubscription.add(
                getDepartureDataObservable()
                        .map(object : Func1<FlightJourneyViewModel, FlightBookingCartData> {
                            override fun call(viewModel: FlightJourneyViewModel?): FlightBookingCartData {
                                val flightBookingCartData = FlightBookingCartData()
                                val flightDetailViewModel = FlightDetailViewModel().build(viewModel)
                                flightDetailViewModel.build(view.getCurrentBookingParamViewModel().getSearchParam())
                                val priceViewModel = view.getPriceViewModel()

                                if (priceViewModel.comboKey != null && priceViewModel.comboKey.isNotEmpty()) {
                                    flightDetailViewModel.adultNumericPrice = priceViewModel.departurePrice.adultNumericCombo
                                    flightDetailViewModel.childNumericPrice = priceViewModel.departurePrice.childNumericCombo
                                    flightDetailViewModel.infantNumericPrice = priceViewModel.departurePrice.infantNumericCombo
                                } else {
                                    flightDetailViewModel.adultNumericPrice = priceViewModel.departurePrice.adultNumeric
                                    flightDetailViewModel.childNumericPrice = priceViewModel.departurePrice.childNumeric
                                    flightDetailViewModel.infantNumericPrice = priceViewModel.departurePrice.infantNumeric
                                }
                                flightBookingCartData.departureTrip = flightDetailViewModel
                                return flightBookingCartData
                            }
                        }).onBackpressureDrop()
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<FlightBookingCartData>() {
                            override fun onNext(t: FlightBookingCartData) {
                                getReturnTripDataAndCart(t)
                            }

                            override fun onCompleted() {
                            }

                            override fun onError(e: Throwable?) {
                                e?.printStackTrace()
                                if (isViewAttached) {
                                    view.hideFullPageLoading()
                                    if (e != null) {
                                        view.showGetCartDataErrorStateLayout(e)
                                    }
                                }
                            }
                        })
        )
    }

    private fun getReturnTripDataAndCart(flightBookingCartData: FlightBookingCartData?) {
        if (flightBookingCartData != null) {
            compositeSubscription.add(
                    Observable.just(flightBookingCartData)
                            .flatMap(getRoundTripDataObservable())
                            .flatMap(object : Func1<FlightBookingCartData, Observable<AddToCartEntity>> {
                                override fun call(t: FlightBookingCartData): Observable<AddToCartEntity> {
                                    val fares = arrayListOf<Fare>()
                                    val departureFare = Fare("", "", "",
                                            t.departureTrip.adultNumericPrice,
                                            t.departureTrip.childNumericPrice,
                                            t.departureTrip.infantNumericPrice)
                                    fares.add(departureFare)

                                    if (t.returnTrip != null) {
                                        val returnFare = Fare("", "", "",
                                                t.returnTrip.adultNumericPrice,
                                                t.returnTrip.childNumericPrice,
                                                t.returnTrip.infantNumericPrice)
                                        fares.add(returnFare)
                                    }

                                    val searchPassDataViewModel = view.getCurrentBookingParamViewModel().searchParam
                                    val price = calculateTotalPassengerFare(
                                            fares,
                                            searchPassDataViewModel.flightPassengerViewModel.adult,
                                            searchPassDataViewModel.flightPassengerViewModel.children,
                                            searchPassDataViewModel.flightPassengerViewModel.infant
                                    )

                                    view.setCartData(t)

                                    return flightAddToCartUseCase.createObservable(getRequestParams(price))
                                }
                            })
                            .zipWith(getDefaultPhoneDataObservable(),
                                    object : Func2<AddToCartEntity, FlightBookingPhoneCodeViewModel, AddToCartEntity> {
                                        override fun call(t1: AddToCartEntity, t2: FlightBookingPhoneCodeViewModel?): AddToCartEntity {
                                            view.setCartId(t1.id)
                                            view.getCurrentCartPassData().id = t1.id
                                            view.getCurrentCartPassData().defaultPhoneCode = t2
                                            return t1
                                        }

                                    })
                            .onBackpressureDrop()
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Subscriber<AddToCartEntity>() {
                                override fun onNext(t: AddToCartEntity?) {
                                    if (isViewAttached && t != null) {
                                        onGetCart(true, view.getCurrentCartPassData())
                                    }
                                }

                                override fun onCompleted() {
                                }

                                override fun onError(e: Throwable?) {
                                    e?.printStackTrace()
                                    if (e != null) {
                                        if (isViewAttached && e is FlightException) {
                                            val errors = e.errorList
                                            when {
                                                errors.contains(FlightError(FlightErrorConstant.ADD_TO_CART)) -> view.showExpireTransactionDialog(e.message!!)
                                                errors.contains(FlightError(FlightErrorConstant.FLIGHT_SOLD_OUT)) -> view.showSoldOutDialog()
                                                errors.contains(FlightError(FlightErrorConstant.FLIGHT_EXPIRED)) -> view.showExpireTransactionDialog(e.message!!)
                                                else -> view.showGetCartDataErrorStateLayout(e)
                                            }
                                        } else {
                                            view.showGetCartDataErrorStateLayout(e)
                                        }
                                    }
                                }

                            })
            )
        }
    }

    private fun getDepartureDataObservable(): Observable<FlightJourneyViewModel> =
            flightSearchJourneyByIdUseCase.createObservable(flightSearchJourneyByIdUseCase
                    .createRequestParams(view.getDepartureTripId()))

    private fun getRoundTripDataObservable():
            Func1<FlightBookingCartData, Observable<FlightBookingCartData>> =
            object : Func1<FlightBookingCartData, Observable<FlightBookingCartData>> {
                override fun call(it: FlightBookingCartData?): Observable<FlightBookingCartData> {
                    if (isRoundTrip() && it != null) {
                        return Observable.just(it).zipWith(flightSearchJourneyByIdUseCase.createObservable(
                                flightSearchJourneyByIdUseCase.createRequestParams(view.getReturnTripId())),
                                object : Func2<FlightBookingCartData, FlightJourneyViewModel, FlightBookingCartData> {
                                    override fun call(t1: FlightBookingCartData?, t2: FlightJourneyViewModel?): FlightBookingCartData {
                                        val flightDetailViewModel = FlightDetailViewModel()
                                        flightDetailViewModel.build(view.getCurrentBookingParamViewModel().searchParam)
                                        val priceViewModel = view.getPriceViewModel()
                                        if (priceViewModel.comboKey != null && priceViewModel.comboKey.isNotEmpty()) {
                                            flightDetailViewModel.adultNumericPrice = priceViewModel.returnPrice.adultNumericCombo
                                            flightDetailViewModel.childNumericPrice = priceViewModel.returnPrice.childNumericCombo
                                            flightDetailViewModel.infantNumericPrice = priceViewModel.returnPrice.infantNumericCombo
                                        } else {
                                            flightDetailViewModel.adultNumericPrice = priceViewModel.returnPrice.adultNumeric
                                            flightDetailViewModel.childNumericPrice = priceViewModel.returnPrice.childNumeric
                                            flightDetailViewModel.infantNumericPrice = priceViewModel.returnPrice.infantNumeric
                                        }
                                        it.returnTrip = flightDetailViewModel
                                        return it
                                    }
                                })
                    } else {
                        return Observable.just(it)
                    }
                }
            }

    private fun getDefaultPhoneDataObservable(): Observable<FlightBookingPhoneCodeViewModel> =
            flightBookingGetPhoneCodeUseCase.createObservable(flightBookingGetPhoneCodeUseCase.createRequest("Indonesia"))
                    .flatMap {
                        Observable.from(it)
                    }
                    .first()

    private fun transform(phoneRawString: String): String {
        var phoneString: String = checkStart(phoneRawString)
        phoneString = phoneString.replace("-", "")
        val phoneNumArr = StringBuilder(phoneString)
        if (phoneNumArr.isNotEmpty()) {
            phoneNumArr.replace(0, 1, "")
        }
        return phoneNumArr.toString()
    }

    private fun checkStart(phoneNumber: String): String {
        var phoneRawString = phoneNumber
        if (phoneRawString.startsWith("62")) {
            phoneRawString = phoneRawString.replaceFirst("62".toRegex(), "0")
        } else if (phoneRawString.startsWith("+62")) {
            phoneRawString = phoneRawString.replaceFirst("\\+62".toRegex(), "0")
        }
        return phoneRawString
    }

    private fun isRoundTrip(): Boolean = view.getReturnTripId().isNotEmpty()

    private fun actionCalculateCurrentTotalPrice(departureFlightDetailViewModel: FlightDetailViewModel,
                                                 returnFlightDetailViewModel: FlightDetailViewModel?): Int {
        val baseCartData = getCurrentCartData()
        val fares = arrayListOf<Fare>()
        fares.add(Fare(
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(departureFlightDetailViewModel.adultNumericPrice),
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(departureFlightDetailViewModel.childNumericPrice),
                CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(departureFlightDetailViewModel.infantNumericPrice),
                departureFlightDetailViewModel.adultNumericPrice,
                departureFlightDetailViewModel.childNumericPrice,
                departureFlightDetailViewModel.infantNumericPrice
        ))

        if (returnFlightDetailViewModel != null) {
            fares.add(
                    Fare(
                            CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(returnFlightDetailViewModel.adultNumericPrice),
                            CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(returnFlightDetailViewModel.childNumericPrice),
                            CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(returnFlightDetailViewModel.infantNumericPrice),
                            returnFlightDetailViewModel.adultNumericPrice,
                            returnFlightDetailViewModel.childNumericPrice,
                            returnFlightDetailViewModel.infantNumericPrice
                    )
            )
        }

        return calculateTotalFareAndAmenities(
                fares,
                baseCartData.adult,
                baseCartData.child,
                baseCartData.infant,
                baseCartData.amenities
        )
    }

    private fun buildPassengerViewModel(passData: FlightSearchPassDataViewModel): List<FlightBookingPassengerViewModel> {
        var passengerNumber = 1
        val viewModels = arrayListOf<FlightBookingPassengerViewModel>()
        for (i in 1..passData.flightPassengerViewModel.adult) {
            val viewModel = FlightBookingPassengerViewModel()
            viewModel.passengerLocalId = passengerNumber
            viewModel.type = FlightBookingPassenger.ADULT
            viewModel.headerTitle = formatPassengerHeader(
                    view.getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    view.getString(R.string.flight_booking_postfix_adult_passenger)
            )
            viewModel.flightBookingLuggageMetaViewModels = arrayListOf()
            viewModel.flightBookingMealMetaViewModels = arrayListOf()
            viewModels.add(viewModel)
            passengerNumber++
        }
        if (passData.flightPassengerViewModel.children > 0) {
            for (i in 1..passData.flightPassengerViewModel.children) {
                val viewModel = FlightBookingPassengerViewModel()
                viewModel.passengerLocalId = passengerNumber
                viewModel.type = FlightBookingPassenger.CHILDREN
                viewModel.headerTitle = formatPassengerHeader(
                        view.getString(R.string.flight_booking_prefix_passenger),
                        passengerNumber,
                        view.getString(R.string.flight_booking_postfix_children_passenger))
                viewModel.flightBookingMealMetaViewModels = ArrayList()
                viewModel.flightBookingLuggageMetaViewModels = ArrayList()
                viewModels.add(viewModel)
                passengerNumber++
            }
        }
        if (passData.flightPassengerViewModel.infant > 0) {
            for (i in 1..passData.flightPassengerViewModel.infant) {
                val viewModel = FlightBookingPassengerViewModel()
                viewModel.passengerLocalId = passengerNumber
                viewModel.type = FlightBookingPassenger.INFANT
                viewModel.headerTitle = formatPassengerHeader(
                        view.getString(R.string.flight_booking_prefix_passenger),
                        passengerNumber,
                        view.getString(R.string.flight_booking_postfix_infant_passenger))

                viewModel.flightBookingLuggageMetaViewModels = ArrayList()
                viewModel.flightBookingMealMetaViewModels = ArrayList()
                viewModels.add(viewModel)
                passengerNumber++
            }
        }

        return viewModels
    }

    private fun formatPassengerHeader(prefix: String, number: Int, postix: String): String {
        return String.format(view.getString(R.string.flight_booking_header_passenger_format),
                prefix,
                number,
                postix
        )
    }

    private fun renderInsurance(flightBookingCartData: FlightBookingCartData, isFromSavedInstance: Boolean) {
        if (!isFromSavedInstance) {
            if (flightBookingCartData.insurances != null && flightBookingCartData.insurances.size > 0) {
                view.showInsuranceLayout()
                view.renderInsurance(flightBookingCartData.insurances)
            } else {
                view.hideInsuranceLayout()
            }
        } else {
            if (flightBookingCartData.insurances != null && flightBookingCartData.insurances.size > 0) {
                for (insuranceViewModel in flightBookingCartData.insurances) {
                    insuranceViewModel.isDefaultChecked = false
                    if (view.getCurrentBookingParamViewModel() != null && view.getCurrentBookingParamViewModel().insurances != null)
                        for (selected in view.getCurrentBookingParamViewModel().insurances) {
                            if (selected.id.equals(insuranceViewModel.id, ignoreCase = true)) {
                                insuranceViewModel.isDefaultChecked = true
                                break
                            }
                        }
                }
                view.showInsuranceLayout()
                view.renderInsurance(flightBookingCartData.insurances)
            } else {
                view.hideInsuranceLayout()
            }
        }
    }

    private fun isMandatoryDoB(): Boolean = view.getCurrentCartPassData().isMandatoryDob

    private fun validateFields(): Boolean {
        var isValid = true
        if (view.getContactName().isEmpty()) {
            isValid = false
            view.showContactNameEmptyError(R.string.flight_booking_contact_name_empty_error)
        } else if (view.getContactName().isNotEmpty() && !isAlphabetAndSpaceOnly(view.getContactName())) {
            isValid = false
            view.showContactNameInvalidError(R.string.flight_booking_contact_name_alpha_space_error)
        } else if (view.getContactEmail().isEmpty()) {
            isValid = false
            view.showContactEmailEmptyError(R.string.flight_booking_contact_email_empty_error)
        } else if (!isValidEmail(view.getContactEmail())) {
            isValid = false
            view.showContactEmailInvalidError(R.string.flight_booking_contact_email_invalid_error)
        } else if (!isEmailWithoutProhibitSymbol(view.getContactEmail())) {
            isValid = false
            view.showContactEmailInvalidSymbolError(R.string.flight_booking_contact_email_invalid_symbol_error)
        } else if (view.getContactPhoneNumber().isEmpty()) {
            isValid = false
            view.showContactPhoneNumberEmptyError(R.string.flight_booking_contact_phone_empty_error)
        } else if (view.getContactPhoneNumber().isNotEmpty() && !isNumericOnly(view.getContactPhoneNumber())) {
            isValid = false
            view.showContactPhoneNumberInvalidError(R.string.flight_booking_contact_phone_invalid_error)
        } else if (view.getContactPhoneNumber().length > 13) {
            isValid = false
            view.showContactPhoneNumberInvalidError(R.string.flight_booking_contact_phone_max_length_error)
        } else if (view.getContactPhoneNumber().length < 9) {
            isValid = false
            view.showContactPhoneNumberInvalidError(R.string.flight_booking_contact_phone_min_length_error)
        } else if (!isAllPassengerFilled(view.getCurrentBookingParamViewModel().passengerViewModels)) {
            isValid = false
            view.showPassengerInfoNotFullfilled(R.string.flight_booking_passenger_not_fullfilled_error)
        }
        return isValid
    }

    private fun isEmailWithoutProhibitSymbol(contactEmail: String): Boolean =
            !contactEmail.contains("+")

    private fun isAllPassengerFilled(passengerViewModels: List<FlightBookingPassengerViewModel>): Boolean {
        var isvalid = true
        for (flightBookingPassengerViewModel in passengerViewModels) {
            if (flightBookingPassengerViewModel.passengerFirstName == null) {
                isvalid = false
                break
            }
        }
        return isvalid
    }

    private fun isNumericOnly(expression: String): Boolean {
        val pattern = Pattern.compile("^[0-9\\s]*$")
        val matcher = pattern.matcher(expression)
        return matcher.matches()
    }

    private fun isAlphabetAndSpaceOnly(expression: String): Boolean {
        val pattern = Pattern.compile("^[a-zA-Z\\s]*$")
        val matcher = pattern.matcher(expression)
        return matcher.matches()
    }

    private fun isValidEmail(contactEmail: String): Boolean =
            Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() &&
                    !contactEmail.contains(".@") && !contactEmail.contains("@.")


    companion object {

        private val TWELVE_YEARS_AGO = -12
        private val MAX_CONTACT_NAME_LENGTH = 20
        private val GENDER_MAN = 1
        private val GENDER_WOMAN = 2

    }

}