package com.tokopedia.flight.bookingV2.presentation.presenter

import android.util.Patterns
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.ticker.TravelTickerFlightPage
import com.tokopedia.common.travel.ticker.TravelTickerInstanceId
import com.tokopedia.common.travel.ticker.domain.TravelTickerUseCase
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.flight.bookingV2.constant.FlightBookingPassenger
import com.tokopedia.flight.bookingV2.data.entity.AddToCartEntity
import com.tokopedia.flight.bookingV2.domain.FlightAddToCartV11UseCase
import com.tokopedia.flight.bookingV2.domain.FlightGetCartDataUseCase
import com.tokopedia.flight.bookingV2.presentation.contract.FlightBookingContract
import com.tokopedia.flight.bookingV2.presentation.viewmodel.*
import com.tokopedia.flight.bookingV2.presentation.viewmodel.mapper.FlightBookingCartDataMapper
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.data.model.FlightException
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel
import com.tokopedia.flight.search.data.api.single.response.Fare
import com.tokopedia.flight.search.domain.FlightSearchJourneyByIdUseCase
import com.tokopedia.flight.search.presentation.model.FlightJourneyViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchPassDataViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sessioncommon.data.profile.ProfilePojo
import com.tokopedia.sessioncommon.domain.usecase.GetProfileUseCase
import com.tokopedia.travel.country_code.data.TravelPhoneCodeEntity
import com.tokopedia.travel.country_code.domain.TravelCountryCodeUseCase
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * @author by furqan on 04/03/19
 */
class FlightBookingPresenter @Inject constructor(val flightAddToCartUseCase: FlightAddToCartV11UseCase,
                                                 private val getPhoneCodeUseCase: TravelCountryCodeUseCase,
                                                 val flightAnalytics: FlightAnalytics,
                                                 val userSession: UserSessionInterface,
                                                 val flightSearchJourneyByIdUseCase: FlightSearchJourneyByIdUseCase,
                                                 private val travelTickerUseCase: TravelTickerUseCase,
                                                 private val getProfileUseCase: GetProfileUseCase,
                                                 flightGetCartDataUseCase: FlightGetCartDataUseCase,
                                                 flightBookingCartDataMapper: FlightBookingCartDataMapper)
    : FlightBaseBookingPresenter<FlightBookingContract.View>(flightGetCartDataUseCase, flightBookingCartDataMapper), FlightBookingContract.Presenter {

    private val compositeSubscription = CompositeSubscription()

    override fun initialize() {
        if (userSession.isMsisdnVerified) {
            processGetCartData()
            onGetProfileData()
        } else if (userSession.isLoggedIn) {
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
                FlightCurrencyFormatUtil.convertToIdrPrice(totalPrice)
        view.renderTotalPrices(FlightCurrencyFormatUtil.convertToIdrPrice(totalPrice))
    }

    override fun onCountDownTimestampChanged(timestamp: String) {
        view.getCurrentBookingParamViewModel().orderDueTimestamp = timestamp
    }

    override fun getInsurances(): List<FlightInsuranceViewModel> =
            view.getCurrentBookingParamViewModel().insurances

    override fun getComboKey(): String = view.getPriceViewModel().comboKey ?: ""

    override fun getRequestParams(): RequestParams {
        return getRequestParams(calculateTotalPassengerFare())
    }

    override fun onFinishTransactionTimeReached() {
        if (isViewAttached) {
            onGetCart(true, view.getCurrentCartPassData())
        }
    }

    override fun onGetProfileData() {
        view.showContactDataProgressBar()
        getProfileUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(graphqlResponse: GraphqlResponse) {
                val data = graphqlResponse.getData<ProfilePojo>(ProfilePojo::class.java)
                val profileInfo = data.profileInfo
                if (isViewAttached) {
                    view.hideContactDataProgressBar()
                    if (view.getContactName().isEmpty()) {
                        view.setContactName(profileInfo.fullName)
                    }
                    if (view.getContactEmail().isEmpty()) {
                        view.setContactEmail(profileInfo.email)
                    }
                    if (view.getContactPhoneNumber().isEmpty()) {
                        view.setContactPhoneNumber(transform(profileInfo.phone))
                    }
                    if (profileInfo.birthday.isNotEmpty()) {
                        view.setContactBirthdate(
                                FlightDateUtil.dateToString(
                                        FlightDateUtil.stringToDate(profileInfo.birthday),
                                        FlightDateUtil.DEFAULT_FORMAT
                                )
                        )
                    }
                    if (profileInfo.gender.isNotEmpty()) {
                        view.setContactGender(profileInfo.gender.toInt())
                    }
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                view.hideContactDataProgressBar()
                e?.printStackTrace()
            }
        })
    }

    override fun onButtonSubmitClicked() {
        if (validateFields()) {
            flightAnalytics.eventBookingNextClick(view.getCurrentCartPassData(),
                    view.getCurrentBookingParamViewModel().searchParam, view.getPriceViewModel().comboKey)

            view.getCurrentBookingParamViewModel().contactName = view.getContactName()
            view.getCurrentBookingParamViewModel().contactEmail = view.getContactEmail()
            view.getCurrentBookingParamViewModel().contactPhone = view.getContactPhoneNumber()

            view.navigateToReview(FlightBookingReviewModel(
                    view.getCurrentBookingParamViewModel(),
                    view.getCurrentCartPassData(),
                    view.getDepartureTripId(),
                    view.getReturnTripId(),
                    view.getString(com.tokopedia.flight.R.string.flight_luggage_prefix),
                    view.getString(com.tokopedia.flight.R.string.flight_meal_prefix),
                    view.getString(com.tokopedia.flight.R.string.flight_birthdate_prefix),
                    view.getString(com.tokopedia.flight.R.string.flight_passenger_passport_number_hint)
            ))
        }
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
            } else {
                view.renderPassengersList(view.getCurrentBookingParamViewModel().passengerViewModels)
            }

            if (isFromSavedInstance) {
                view.renderPassengersList(view.getCurrentBookingParamViewModel().passengerViewModels)
                view.setContactName(view.getCurrentBookingParamViewModel().contactName)
                view.setContactEmail(view.getCurrentBookingParamViewModel().contactEmail)
                view.setContactPhoneNumber(view.getCurrentBookingParamViewModel().contactPhone, view.getCurrentBookingParamViewModel().phoneCode.countryPhoneCode.toInt())
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
            view.getCurrentBookingParamViewModel().phoneCode = flightBookingCartData.defaultPhoneCode

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
                    view.showPriceChangesDialog(FlightCurrencyFormatUtil.convertToIdrPrice(resultTotalPrice), FlightCurrencyFormatUtil.convertToIdrPrice(oldTotalPrice))
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
                        flightBookingCartData.returnTrip, view.getPriceViewModel().comboKey)
            }
        } else {
            initialize()
        }
    }

    override fun fetchTickerData() {
        travelTickerUseCase.execute(travelTickerUseCase.createRequestParams(
                TravelTickerInstanceId.FLIGHT, TravelTickerFlightPage.BOOK),
                object : Subscriber<TravelTickerViewModel>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(travelTickerViewModel: TravelTickerViewModel) {
                        if (travelTickerViewModel.message.isNotEmpty()) {
                            view.renderTickerView(travelTickerViewModel)
                        }
                    }
                })
    }

    override fun onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe()
        }
        flightAddToCartUseCase.unsubscribe()
        flightSearchJourneyByIdUseCase.unsubscribe()
        getProfileUseCase.unsubscribe()
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
                        .map { viewModel ->
                            val flightBookingCartData = FlightBookingCartData()
                            val flightDetailViewModel = FlightDetailViewModel().build(viewModel)
                            flightDetailViewModel.build(view.getCurrentBookingParamViewModel().searchParam)
                            val priceViewModel = view.getPriceViewModel()

                            if (priceViewModel.departurePrice.adultNumericCombo > 0) {
                                flightDetailViewModel.adultNumericPrice = priceViewModel.departurePrice.adultNumericCombo
                                flightDetailViewModel.childNumericPrice = priceViewModel.departurePrice.childNumericCombo
                                flightDetailViewModel.infantNumericPrice = priceViewModel.departurePrice.infantNumericCombo
                            } else {
                                flightDetailViewModel.adultNumericPrice = priceViewModel.departurePrice.adultNumeric
                                flightDetailViewModel.childNumericPrice = priceViewModel.departurePrice.childNumeric
                                flightDetailViewModel.infantNumericPrice = priceViewModel.departurePrice.infantNumeric
                            }
                            flightBookingCartData.departureTrip = flightDetailViewModel
                            flightBookingCartData
                        }.onBackpressureDrop()
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
                            .flatMap { t ->
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

                                val newTotalPrice = actionCalculateCurrentTotalPrice(
                                        t.departureTrip, t.returnTrip)
                                updateTotalPrice(newTotalPrice)

                                view.setCartData(t)

                                flightAddToCartUseCase.createObservable(getRequestParams(price))
                            }
                            .zipWith(getDefaultPhoneDataObservable()
                            ) { t1, t2 ->
                                view.setCartId(t1.id)
                                view.getCurrentCartPassData().id = t1.id
                                view.getCurrentCartPassData().defaultPhoneCode = t2
                                t1
                            }
                            .onBackpressureDrop()
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : Subscriber<AddToCartEntity>() {
                                override fun onNext(t: AddToCartEntity?) {
                                    if (isViewAttached && t != null) {
                                        if (view.getCurrentBookingParamViewModel().passengerViewModels == null
                                                || view.getCurrentBookingParamViewModel().passengerViewModels.size == 0) {
                                            val passengerViewModels = buildPassengerViewModel(view.getCurrentBookingParamViewModel().searchParam)
                                            view.getCurrentBookingParamViewModel().passengerViewModels = passengerViewModels
                                        }
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
                                flightSearchJourneyByIdUseCase.createRequestParams(view.getReturnTripId()))
                        ) { t1, t2 ->
                            val flightDetailViewModel = FlightDetailViewModel().build(t2)
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
                            it
                        }
                    } else {
                        return Observable.just(it)
                    }
                }
            }

    private fun getDefaultPhoneDataObservable(): Observable<TravelCountryPhoneCode> =
            getPhoneCodeUseCase.createObservable(GraphqlHelper.loadRawString(view.getViewContext().resources, com.tokopedia.travel.country_code.R.raw.query_travel_get_all_country))
                    .flatMap(Func1 { graphqlResponse ->
                        val data = graphqlResponse.getData<TravelPhoneCodeEntity.Response>(TravelPhoneCodeEntity.Response::class.java)
                        var observableData: Observable<TravelCountryPhoneCode> = Observable.just(TravelCountryPhoneCode())
                        if (data != null) {
                            val phoneData = data.travelGetAllCountries.countries.find { it.id == "ID" || it.attributes.name == "Indonesia" }
                            phoneData?.let {
                                observableData = Observable.just(TravelCountryPhoneCode(phoneData.id, phoneData.attributes.name, phoneData.attributes.phoneCode))
                            }
                        }

                        observableData
                    })

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
                FlightCurrencyFormatUtil.convertToIdrPrice(departureFlightDetailViewModel.adultNumericPrice),
                FlightCurrencyFormatUtil.convertToIdrPrice(departureFlightDetailViewModel.childNumericPrice),
                FlightCurrencyFormatUtil.convertToIdrPrice(departureFlightDetailViewModel.infantNumericPrice),
                departureFlightDetailViewModel.adultNumericPrice,
                departureFlightDetailViewModel.childNumericPrice,
                departureFlightDetailViewModel.infantNumericPrice
        ))

        if (returnFlightDetailViewModel != null) {
            fares.add(
                    Fare(
                            FlightCurrencyFormatUtil.convertToIdrPrice(returnFlightDetailViewModel.adultNumericPrice),
                            FlightCurrencyFormatUtil.convertToIdrPrice(returnFlightDetailViewModel.childNumericPrice),
                            FlightCurrencyFormatUtil.convertToIdrPrice(returnFlightDetailViewModel.infantNumericPrice),
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
                    view.getString(com.tokopedia.flight.R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    view.getString(com.tokopedia.flight.R.string.flight_booking_postfix_adult_passenger)
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
                        view.getString(com.tokopedia.flight.R.string.flight_booking_prefix_passenger),
                        passengerNumber,
                        view.getString(com.tokopedia.flight.R.string.flight_booking_postfix_children_passenger))
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
                        view.getString(com.tokopedia.flight.R.string.flight_booking_prefix_passenger),
                        passengerNumber,
                        view.getString(com.tokopedia.flight.R.string.flight_booking_postfix_infant_passenger))

                viewModel.flightBookingLuggageMetaViewModels = ArrayList()
                viewModel.flightBookingMealMetaViewModels = ArrayList()
                viewModels.add(viewModel)
                passengerNumber++
            }
        }

        return viewModels
    }

    private fun formatPassengerHeader(prefix: String, number: Int, postix: String): String {
        return String.format(view.getString(com.tokopedia.flight.R.string.flight_booking_header_passenger_format),
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
            view.showContactNameEmptyError(com.tokopedia.flight.R.string.flight_booking_contact_name_empty_error)
        } else if (view.getContactName().isNotEmpty() && !isAlphabetAndSpaceOnly(view.getContactName())) {
            isValid = false
            view.showContactNameInvalidError(com.tokopedia.flight.R.string.flight_booking_contact_name_alpha_space_error)
        } else if (view.getContactEmail().isEmpty()) {
            isValid = false
            view.showContactEmailEmptyError(com.tokopedia.flight.R.string.flight_booking_contact_email_empty_error)
        } else if (!isValidEmail(view.getContactEmail())) {
            isValid = false
            view.showContactEmailInvalidError(com.tokopedia.flight.R.string.flight_booking_contact_email_invalid_error)
        } else if (!isEmailWithoutProhibitSymbol(view.getContactEmail())) {
            isValid = false
            view.showContactEmailInvalidSymbolError(com.tokopedia.flight.R.string.flight_booking_contact_email_invalid_symbol_error)
        } else if (view.getContactPhoneNumber().isEmpty()) {
            isValid = false
            view.showContactPhoneNumberEmptyError(com.tokopedia.flight.R.string.flight_booking_contact_phone_empty_error)
        } else if (view.getContactPhoneNumber().isNotEmpty() && !isNumericOnly(view.getContactPhoneNumber())) {
            isValid = false
            view.showContactPhoneNumberInvalidError(com.tokopedia.flight.R.string.flight_booking_contact_phone_invalid_error)
        } else if (view.getContactPhoneNumber().length > 13) {
            isValid = false
            view.showContactPhoneNumberInvalidError(com.tokopedia.flight.R.string.flight_booking_contact_phone_max_length_error)
        } else if (view.getContactPhoneNumber().length < 9) {
            isValid = false
            view.showContactPhoneNumberInvalidError(com.tokopedia.flight.R.string.flight_booking_contact_phone_min_length_error)
        } else if (!isAllPassengerFilled(view.getCurrentBookingParamViewModel().passengerViewModels)) {
            isValid = false
            view.showPassengerInfoNotFullfilled(com.tokopedia.flight.R.string.flight_booking_passenger_not_fullfilled_error)
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

    override fun onContactDataResultRecieved(contactData: TravelContactData) {
        view.setContactName(contactData.name)
        view.setContactEmail(contactData.email)
        view.setContactPhoneNumber(contactData.phone, contactData.phoneCode)
    }

    companion object {

        private val TWELVE_YEARS_AGO = -12
        private val MAX_CONTACT_NAME_LENGTH = 20
        private val GENDER_MAN = 1
        private val GENDER_WOMAN = 2

    }

}