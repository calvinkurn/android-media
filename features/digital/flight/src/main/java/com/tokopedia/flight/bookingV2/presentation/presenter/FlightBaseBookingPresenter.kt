package com.tokopedia.flight.bookingV2.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice
import com.tokopedia.flight.booking.view.viewmodel.*
import com.tokopedia.flight.bookingV2.data.entity.GetCartEntity
import com.tokopedia.flight.bookingV2.domain.FlightGetCartDataUseCase
import com.tokopedia.flight.bookingV2.presentation.contract.FlightBaseBookingContract
import com.tokopedia.flight.bookingV2.presentation.viewmodel.mapper.FlightBookingCartDataMapper
import com.tokopedia.flight.common.constant.FlightErrorConstant
import com.tokopedia.flight.common.data.model.FlightError
import com.tokopedia.flight.common.data.model.FlightException
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel
import com.tokopedia.flight.search.data.api.single.response.Fare
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.*

/**
 * @author by furqan on 04/03/19
 */
abstract class FlightBaseBookingPresenter<T : FlightBaseBookingContract.View>(
        private val getCartDataUseCase: FlightGetCartDataUseCase,
        val flightBookingCartDataMapper: FlightBookingCartDataMapper)
    : BaseDaggerPresenter<T>(), FlightBaseBookingContract.Presenter<T> {

    abstract fun getRequestParams(): RequestParams

    abstract fun getCurrentCartData(): BaseCartData

    abstract fun updateTotalPrice(totalPrice: Int)

    abstract fun onCountDownTimestampChanged(timestamp: String)

    abstract fun getInsurances(): List<FlightInsuranceViewModel>

    abstract fun getComboKey(): String

    override fun onGetCart(shouldReRenderUi: Boolean, flightBookingCartData: FlightBookingCartData?) {
        view.showUpdatePriceLoading()
        var flightCartData: FlightBookingCartData? = flightBookingCartData
        getCartDataUseCase.createObservable(getCartDataUseCase.createRequestParam(view.getCartId()))
                .map(object : Func1<GetCartEntity, FlightBookingCartData> {
                    override fun call(t: GetCartEntity?): FlightBookingCartData {
                        flightCartData = flightBookingCartDataMapper.transform(flightCartData, t)
                        return flightCartData as FlightBookingCartData
                    }
                })
                .map(object : Func1<FlightBookingCartData, BaseCartData> {
                    override fun call(t: FlightBookingCartData?): BaseCartData {
                        val baseCartData = cloneViewModel(getCurrentCartData())

                        if (t != null) {
                            baseCartData.id = t.id
                            baseCartData.refreshTime = t.refreshTime

                            val paxFares = arrayListOf<Fare>()
                            paxFares.add(Fare(
                                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(t.departureTrip.adultNumericPrice),
                                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(t.departureTrip.childNumericPrice),
                                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(t.departureTrip.infantNumericPrice),
                                    t.departureTrip.adultNumericPrice,
                                    t.departureTrip.childNumericPrice,
                                    t.departureTrip.infantNumericPrice
                            ))
                            if (t.returnTrip != null) {
                                paxFares.add(Fare(
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(t.returnTrip.adultNumericPrice),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(t.returnTrip.childNumericPrice),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(t.returnTrip.infantNumericPrice),
                                        t.returnTrip.adultNumericPrice,
                                        t.returnTrip.childNumericPrice,
                                        t.returnTrip.infantNumericPrice
                                ))
                            }

                            val newTotalPrice = calculateTotalFareAndAmenities(paxFares,
                                    baseCartData.adult,
                                    baseCartData.child,
                                    baseCartData.infant,
                                    baseCartData.amenities)
                            baseCartData.total = newTotalPrice
                        }

                        if (t != null && t.newFarePrices.isNotEmpty()) {
                            val fares = arrayListOf<Fare>()
                            val journeyAffected = arrayListOf<String>()
                            for (newFare in t.newFarePrices) {
                                if (newFare.id.equals(view.getDepartureFlightDetailViewModel().id, true)) {
                                    journeyAffected.add(newFare.id)
                                    fares.add(newFare.fare)
                                }
                                if (view.getReturnFlightDetailViewModel() != null &&
                                        newFare.id.equals(view.getReturnFlightDetailViewModel()!!.id, true)) {
                                    journeyAffected.add(newFare.id)
                                    fares.add(newFare.fare)
                                }
                            }

                            if (!journeyAffected.contains(view.getDepartureFlightDetailViewModel().id)) {
                                fares.add(Fare(
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(view.getDepartureFlightDetailViewModel().adultNumericPrice),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(view.getDepartureFlightDetailViewModel().childNumericPrice),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(view.getDepartureFlightDetailViewModel().infantNumericPrice),
                                        view.getDepartureFlightDetailViewModel().adultNumericPrice,
                                        view.getDepartureFlightDetailViewModel().childNumericPrice,
                                        view.getDepartureFlightDetailViewModel().infantNumericPrice
                                ))
                            }

                            if (view.getReturnFlightDetailViewModel() != null &&
                                    !journeyAffected.contains(view.getReturnFlightDetailViewModel()!!.id)) {
                                fares.add(Fare(
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(view.getReturnFlightDetailViewModel()!!.adultNumericPrice),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(view.getReturnFlightDetailViewModel()!!.childNumericPrice),
                                        CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(view.getReturnFlightDetailViewModel()!!.infantNumericPrice),
                                        view.getReturnFlightDetailViewModel()!!.adultNumericPrice,
                                        view.getReturnFlightDetailViewModel()!!.childNumericPrice,
                                        view.getReturnFlightDetailViewModel()!!.infantNumericPrice
                                ))
                            }

                            if (journeyAffected.isEmpty() && getComboKey().isNotEmpty()) {
                                val newFarePrice = t.newFarePrices[0]
                                fares.add(newFarePrice.fare)
                            }

                            val newTotalPrice = calculateTotalFareAndAmenities(fares,
                                    baseCartData.adult,
                                    baseCartData.child,
                                    baseCartData.infant,
                                    baseCartData.amenities)
                            baseCartData.newFarePrices = t.newFarePrices
                            baseCartData.total = newTotalPrice
                        } else {
                            baseCartData.newFarePrices = arrayListOf()
                        }

                        return baseCartData
                    }
                }).onBackpressureDrop()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<BaseCartData>() {
                    override fun onNext(baseCartData: BaseCartData) {
                        view.hideUpdatePriceDialog()
                        val expiredDate = FlightDateUtil.addTimeToCurrentDate(Calendar.SECOND, baseCartData.refreshTime)
                        view.renderFinishTimeCountDown(expiredDate)
                        onCountDownTimestampChanged(FlightDateUtil.dateToString(expiredDate, FlightDateUtil.DEFAULT_TIMESTAMP_FORMAT))

                        if (baseCartData.total != getCurrentCartData().total && getCurrentCartData().total > 0) {
                            view.showPriceChangesDialog(CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(baseCartData.total),
                                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(getCurrentCartData().total))
                            updateTotalPrice(baseCartData.total)
                            actionCalculatePriceAndRender(
                                    baseCartData.newFarePrices,
                                    view.getDepartureFlightDetailViewModel(),
                                    view.getReturnFlightDetailViewModel(),
                                    view.getFlightBookingPassengers(),
                                    getInsurances())
                        }

                        renderUi(flightCartData, false)
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        if (e != null) {
                            e.printStackTrace()
                            if (isViewAttached) {
                                view.hideUpdatePriceDialog()
                                view.showUpdateDataErrorStateLayout(e)
                                if (e is FlightException) {
                                    val errors = e.errorList
                                    when {
                                        errors.contains(FlightError(FlightErrorConstant.ADD_TO_CART)) -> view.showExpireTransactionDialog(e.message!!)
                                        errors.contains(FlightError(FlightErrorConstant.FLIGHT_SOLD_OUT)) -> view.showSoldOutDialog()
                                        errors.contains(FlightError(FlightErrorConstant.FLIGHT_EXPIRED)) -> view.showExpireTransactionDialog(e.message!!)
                                    }
                                }
                            }
                        }
                    }

                })
    }

    protected fun calculateTotalFareAndAmenities(newFares: List<Fare>,
                                                 adult: Int, child: Int, infant: Int,
                                                 amenities: List<FlightBookingAmenityViewModel>): Int {
        var newTotalPrice = calculateTotalPassengerFare(newFares, adult, child, infant)

        for (item in amenities) {
            newTotalPrice += item.priceNumeric
        }

        for (insurance in getInsurances()) {
            newTotalPrice += insurance.totalPrice.toInt()
        }

        return newTotalPrice
    }

    protected fun calculateTotalPassengerFare(): Int {
        val cartData = getCurrentCartData()
        val fares = arrayListOf<Fare>()
        for (item in cartData.newFarePrices) {
            fares.add(item.fare)
        }
        return calculateTotalPassengerFare(fares, cartData.adult, cartData.child, cartData.infant)
    }

    protected fun calculateTotalPassengerFare(newFares: List<Fare>, adult: Int, child: Int, infant: Int): Int {
        var newTotalPrice = 0
        for (item in newFares) {
            newTotalPrice += item.adultNumeric * adult
            newTotalPrice += item.childNumeric * child
            newTotalPrice += item.infantNumeric * infant
        }
        return newTotalPrice
    }

    protected fun actionCalculatePriceAndRender(newFarePrices: List<NewFarePrice>,
                                                departureDetailViewModel: FlightDetailViewModel,
                                                returnDetailViewModel: FlightDetailViewModel?,
                                                flightBookingPassengers: List<FlightBookingPassengerViewModel>,
                                                insurances: List<FlightInsuranceViewModel>) {
        for (newPrice in newFarePrices) {
            if (newPrice.id.equals(departureDetailViewModel.id, true)) {
                departureDetailViewModel.adultNumericPrice = newPrice.fare.adultNumeric
                departureDetailViewModel.childNumericPrice = newPrice.fare.childNumeric
                departureDetailViewModel.infantNumericPrice = newPrice.fare.infantNumeric
            }
            if (returnDetailViewModel != null && newPrice.id.equals(returnDetailViewModel.id, true)) {
                returnDetailViewModel.adultNumericPrice = newPrice.fare.adultNumeric
                returnDetailViewModel.childNumericPrice = newPrice.fare.childNumeric
                returnDetailViewModel.infantNumericPrice = newPrice.fare.infantNumeric
            }

            if (getComboKey().isNotEmpty() && newPrice.id.equals(getComboKey(), true)) {
                val newAdultPrice = newPrice.fare.adultNumeric / 2
                val newChildPrice = newPrice.fare.childNumeric / 2
                val newInfantPrice = newPrice.fare.infantNumeric / 2

                departureDetailViewModel.adultNumericPrice = newAdultPrice
                departureDetailViewModel.childNumericPrice = newChildPrice
                departureDetailViewModel.infantNumericPrice = newInfantPrice

                if (returnDetailViewModel != null) {
                    returnDetailViewModel.adultNumericPrice = newPrice.fare.adultNumeric - newAdultPrice
                    returnDetailViewModel.childNumericPrice = newPrice.fare.childNumeric - newChildPrice
                    returnDetailViewModel.infantNumericPrice = newPrice.fare.infantNumeric - newInfantPrice
                }
            }
        }

        val simpleViewModels = arrayListOf<SimpleViewModel>()

        // adult price
        if (departureDetailViewModel.adultNumericPrice > 0) {
            simpleViewModels.add(
                    formatPassengerFarePriceDetail(
                            departureDetailViewModel.departureAirport,
                            departureDetailViewModel.arrivalAirport,
                            view.getString(R.string.flightbooking_price_adult_label),
                            departureDetailViewModel.countAdult,
                            departureDetailViewModel.adultNumericPrice * departureDetailViewModel.countAdult
                    )
            )
        }

        // child price
        if (departureDetailViewModel.countChild > 0 &&
                departureDetailViewModel.childNumericPrice > 0) {
            simpleViewModels.add(
                    formatPassengerFarePriceDetail(
                            departureDetailViewModel.departureAirport,
                            departureDetailViewModel.arrivalAirport,
                            view.getString(R.string.flightbooking_price_child_label),
                            departureDetailViewModel.countChild,
                            departureDetailViewModel.childNumericPrice * departureDetailViewModel.countChild
                    )
            )
        }

        // infant price
        if (departureDetailViewModel.countInfant > 0 &&
                departureDetailViewModel.infantNumericPrice > 0) {
            simpleViewModels.add(
                    formatPassengerFarePriceDetail(
                            departureDetailViewModel.departureAirport,
                            departureDetailViewModel.arrivalAirport,
                            view.getString(R.string.flightbooking_price_infant_label),
                            departureDetailViewModel.countInfant,
                            departureDetailViewModel.infantNumericPrice * departureDetailViewModel.countInfant
                    )
            )
        }

        // return price
        if (returnDetailViewModel != null) {
            // adult price
            if (returnDetailViewModel.adultNumericPrice > 0) {
                simpleViewModels.add(
                        formatPassengerFarePriceDetail(
                                returnDetailViewModel.departureAirport,
                                returnDetailViewModel.arrivalAirport,
                                view.getString(R.string.flightbooking_price_adult_label),
                                returnDetailViewModel.countAdult,
                                returnDetailViewModel.adultNumericPrice * returnDetailViewModel.countAdult
                        )
                )
            }
            // child price
            if (returnDetailViewModel.countChild > 0 && returnDetailViewModel.childNumericPrice > 0) {
                simpleViewModels.add(
                        formatPassengerFarePriceDetail(
                                returnDetailViewModel.departureAirport,
                                returnDetailViewModel.arrivalAirport,
                                view.getString(R.string.flightbooking_price_child_label),
                                returnDetailViewModel.countChild,
                                returnDetailViewModel.childNumericPrice * returnDetailViewModel.countChild
                        )
                )
            }
            //infant price
            if (returnDetailViewModel.countInfant > 0 && returnDetailViewModel.infantNumericPrice > 0) {
                simpleViewModels.add(
                        formatPassengerFarePriceDetail(
                                returnDetailViewModel.departureAirport,
                                returnDetailViewModel.arrivalAirport,
                                view.getString(R.string.flightbooking_price_infant_label),
                                returnDetailViewModel.countInfant,
                                returnDetailViewModel.infantNumericPrice * returnDetailViewModel.countInfant
                        )
                )
            }
        }

        // amenities
        val meals = hashMapOf<String, Int>()
        val luggages = hashMapOf<String, Int>()

        for (flightPassengerViewModel in flightBookingPassengers) {
            for (flightBookingAmenityMetaViewModel in flightPassengerViewModel.flightBookingMealMetaViewModels) {
                for (flightBookingAmenityViewModel in flightBookingAmenityMetaViewModel.amenities) {
                    if (meals[flightBookingAmenityMetaViewModel.description] != null) {
                        var total = meals[flightBookingAmenityMetaViewModel.description]!!
                        total += flightBookingAmenityViewModel.priceNumeric
                        meals[flightBookingAmenityMetaViewModel.description] = total
                    } else {
                        meals[flightBookingAmenityMetaViewModel.description] = flightBookingAmenityViewModel.priceNumeric
                    }
                }
            }
            for (flightBookingLuggageMetaViewModel in flightPassengerViewModel.flightBookingLuggageMetaViewModels) {
                for (flightBookingLuggageViewModel in flightBookingLuggageMetaViewModel.amenities) {
                    if (luggages[flightBookingLuggageMetaViewModel.description] != null) {
                        var total = luggages[flightBookingLuggageMetaViewModel.description]!!
                        total += flightBookingLuggageViewModel.priceNumeric
                        luggages[flightBookingLuggageMetaViewModel.description] = total
                    } else {
                        luggages[flightBookingLuggageMetaViewModel.description] = flightBookingLuggageViewModel.priceNumeric
                    }
                }
            }
        }

        for ((key, value) in meals) {
            simpleViewModels.add(SimpleViewModel(
                    String.format("%s %s", view.getString(R.string.flight_price_detail_prefixl_meal_label),
                            key),
                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(value)))

        }
        for ((key, value) in luggages) {
            simpleViewModels.add(SimpleViewModel(
                    String.format("%s %s", view.getString(R.string.flight_price_detail_prefix_luggage_label),
                            key),
                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(value)))

        }
        val totalPassenger = departureDetailViewModel.countAdult + departureDetailViewModel.countChild + departureDetailViewModel.countInfant

        for (insuranceViewModel in insurances) {
            simpleViewModels.add(SimpleViewModel(
                    String.format("%s x%d", insuranceViewModel.name, totalPassenger),
                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(insuranceViewModel.totalPrice.toInt())))
        }

        view.renderPriceListDetails(simpleViewModels)
    }

    private fun cloneViewModel(currentDashboardViewModel: BaseCartData): BaseCartData {
        var viewModel: BaseCartData? = null
        try {
            viewModel = currentDashboardViewModel.clone() as BaseCartData
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
            throw RuntimeException("Failed to Clone FlightDashboardViewModel")
        }
        return viewModel
    }

    private fun formatPassengerFarePriceDetail(departureAirport: String, arrivalAirport: String, label: String,
                                               passengerCount: Int, price: Int): SimpleViewModel =
            SimpleViewModel(
                    String.format(view.getString(R.string.flight_booking_passenger_price_format),
                            departureAirport,
                            arrivalAirport,
                            label,
                            passengerCount),
                    CurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(price))

}