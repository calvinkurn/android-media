package com.tokopedia.flight.booking.data.mapper

import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.flight.booking.data.FlightCart
import com.tokopedia.flight.booking.data.FlightCartViewEntity
import com.tokopedia.flight.booking.data.FlightPromoViewEntity
import com.tokopedia.flight.common.util.FlightCurrencyFormatUtil
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoModel
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightStopOverViewModel
import com.tokopedia.flight.passenger.constant.FlightBookingPassenger
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityMetaModel
import com.tokopedia.flight.passenger.view.model.FlightBookingAmenityModel
import com.tokopedia.flight.passenger.view.model.FlightBookingPassengerModel
import com.tokopedia.flight.search.presentation.model.FlightPriceModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import java.util.*

/**
 * @author by jessica on 2019-10-29
 */

class FlightBookingMapper {

    companion object {
        private const val DEFAULT_IS_COUPON_ZERO = 0
        private const val DEFAULT_IS_COUPON_ONE = 1

        const val AMENITY_LUGGAGE = 1
        const val AMENITY_MEAL = 2

        fun mapToFlightCartView(flightCart: FlightCart, isRefreshCart: Boolean): FlightCartViewEntity {
            val journies: MutableList<FlightCartViewEntity.JourneySummary> = arrayListOf()
            for (item in flightCart.cartData.flight.journeys) {

                val newJourney = FlightCartViewEntity.JourneySummary()
                var airlineName = ""
                var departureCityName = ""
                var arrivalCityName = ""

                for ((position, route) in item.routes.withIndex()) {
                    for (includedItem in flightCart.included) {
                        if (route.airlineId.equals(includedItem.id, true)) {
                            if (airlineName.equals(includedItem.attributes.name, true)) break
                            if (position == 0) newJourney.airlineLogo = includedItem.attributes.logo else {
                                airlineName += " + "
                                newJourney.isMultipleAirline = true
                            }
                            airlineName += includedItem.attributes.name
                            break
                        }
                    }
                }

                for (includedItem in flightCart.included) {
                    if (item.departureAirportId.equals(includedItem.id, true)) {
                        departureCityName = includedItem.attributes.city
                    } else if (item.arrivalAirportId.equals(includedItem.id, true)) {
                        arrivalCityName = includedItem.attributes.city
                    }
                }

                var departureDateString = TravelDateUtil.dateToString(TravelDateUtil.EEE_DD_MMM_YY, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, item.departureTime))
                departureDateString += String.format(" • %s-%s", TravelDateUtil.dateToString(TravelDateUtil.HH_MM, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, item.departureTime)),
                        TravelDateUtil.dateToString(TravelDateUtil.HH_MM, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, item.arrivalTime)))
                if (item.addDayArrival > 0) departureDateString += String.format(" (+%d hari)", item.addDayArrival)

                newJourney.journeyId = item.id
                newJourney.airline = airlineName
                newJourney.routeName = String.format("%s (%s) → %s (%s)", departureCityName, item.departureAirportId,
                        arrivalCityName, item.arrivalAirportId)
                newJourney.isRefundable = item.routes[0].refundable
                newJourney.transit = item.routes.size - 1
                newJourney.date = departureDateString

                journies.add(newJourney)
            }

            val luggageMetaModels = arrayListOf<FlightBookingAmenityMetaModel>()
            val mealMetaModels = arrayListOf<FlightBookingAmenityMetaModel>()
            for (amenity in flightCart.cartData.flight.amenityOptions) {
                val amenityMetaViewModel = FlightBookingAmenityMetaModel()
                amenityMetaViewModel.arrivalId = amenity.arrivalAirportId
                amenityMetaViewModel.departureId = amenity.departureAirportId
                amenityMetaViewModel.key = amenity.key
                amenityMetaViewModel.journeyId = amenity.journeyId
                amenityMetaViewModel.description = amenity.detail
                amenityMetaViewModel.amenities = mapToFlightBookingAmenityViewModels(amenity)
                if (amenity.type == AMENITY_MEAL) mealMetaModels.add(amenityMetaViewModel)
                else if (amenity.type == AMENITY_LUGGAGE) luggageMetaModels.add(amenityMetaViewModel)
            }

            val now = Calendar.getInstance()
            now.add(Calendar.MINUTE, flightCart.meta.refreshTime)

            return FlightCartViewEntity(journeySummaries = journies, insurances = flightCart.cartData.flight.insuranceOptions,
                    luggageModels = luggageMetaModels, mealModels = mealMetaModels, orderDueTimeStamp = now.time, isRefreshCart = isRefreshCart)
        }

        fun mapToFlightPromoViewEntity(voucher: FlightCart.Voucher): FlightPromoViewEntity {
            val promoData = PromoData()
            voucher.let {
                if (it.autoApply.success) {
                    if (!(it.isCouponActive == DEFAULT_IS_COUPON_ZERO &&
                                    it.autoApply.isCoupon == DEFAULT_IS_COUPON_ONE)) {
                        promoData.typePromo = it.autoApply.isCoupon
                        promoData.promoCode = it.autoApply.code
                        promoData.description = it.autoApply.messageSuccess
                        promoData.title = it.autoApply.titleDescription
                        promoData.amount = it.autoApply.discountedAmount
                        promoData.state = TickerCheckoutView.State.ACTIVE
                    }
                }
            }
            return FlightPromoViewEntity(voucher.enableVoucher, voucher.isCouponActive, promoData)
        }

        fun mapToFlightPassengerEntity(adult: Int, child: Int, infant: Int): List<FlightBookingPassengerModel> {

            var passengerNumber = 1
            val viewModels = arrayListOf<FlightBookingPassengerModel>()
            for (i in 1..adult) {
                val viewModel = FlightBookingPassengerModel()
                viewModel.passengerLocalId = passengerNumber
                viewModel.type = FlightBookingPassenger.ADULT
                viewModel.headerTitle = String.format("Penumpang dewasa")
                viewModel.flightBookingLuggageMetaViewModels = arrayListOf()
                viewModel.flightBookingMealMetaViewModels = arrayListOf()
                viewModels.add(viewModel)
                passengerNumber++
            }

            if (child > 0) {
                for (i in 1..child) {
                    val viewModel = FlightBookingPassengerModel()
                    viewModel.passengerLocalId = passengerNumber
                    viewModel.type = FlightBookingPassenger.CHILDREN
                    viewModel.headerTitle = String.format("Penumpang anak")
                    viewModel.flightBookingLuggageMetaViewModels = arrayListOf()
                    viewModel.flightBookingMealMetaViewModels = arrayListOf()
                    viewModels.add(viewModel)
                    passengerNumber++
                }
            }

            if (infant > 0) {
                for (i in 1..infant) {
                    val viewModel = FlightBookingPassengerModel()
                    viewModel.passengerLocalId = passengerNumber
                    viewModel.type = FlightBookingPassenger.INFANT
                    viewModel.headerTitle = String.format("Penumpang bayi")
                    viewModel.flightBookingLuggageMetaViewModels = arrayListOf()
                    viewModel.flightBookingMealMetaViewModels = arrayListOf()
                    viewModels.add(viewModel)
                    passengerNumber++
                }
            }

            return viewModels
        }

        private fun mapToFlightBookingAmenityViewModels(entity: FlightCart.Amenity): List<FlightBookingAmenityModel> {
            val viewModels = ArrayList<FlightBookingAmenityModel>()
            var data: FlightBookingAmenityModel? = null
            if (entity != null) {
                for (item in entity.items) {
                    data = mapToFlightBookingAmenityViewModel(entity.type, item)
                    if (data != null) {
                        viewModels.add(data)
                    }
                }
            }
            return viewModels
        }

        private fun mapToFlightBookingAmenityViewModel(type: Int, item: FlightCart.AmenityItem?): FlightBookingAmenityModel? {
            var model: FlightBookingAmenityModel? = null
            if (item != null) {
                model = FlightBookingAmenityModel()
                model.id = item.id
                model.price = item.price
                model.priceNumeric = item.priceNumeric
                model.title = item.description
                model.amenityType = type
            }
            return model
        }

        fun mapToFlightDetail(flight: FlightCart.Flight, included: List<FlightCart.Included>,
                              flightPriceModel: FlightPriceModel): List<FlightDetailModel> {

            val list = listOf<FlightDetailModel>().toMutableList()
            for ((index, journey) in flight.journeys.withIndex()) {
                val flightDetailViewModel = FlightDetailModel()

                flightDetailViewModel.beforeTotal = ""
                flightDetailViewModel.id = journey.id

                for (includedItem in included) {
                    if (journey.departureAirportId.equals(includedItem.id, true)) {
                        flightDetailViewModel.departureAirport = includedItem.id
                        flightDetailViewModel.departureAirportCity = includedItem.attributes.city
                    } else if (journey.arrivalAirportId.equals(includedItem.id, true)) {
                        flightDetailViewModel.arrivalAirport = includedItem.id
                        flightDetailViewModel.arrivalAirportCity = includedItem.attributes.city
                    }
                }

                flightDetailViewModel.totalTransit = journey.routes.size - 1
                flightDetailViewModel.isRefundable = if (journey.routes[0].refundable) RefundableEnum.REFUNDABLE else RefundableEnum.NOT_REFUNDABLE

                if (index == 0) {
                    flightPriceModel.departurePrice?.let {
                        if (flightPriceModel.comboKey.isNullOrEmpty()) {
                            flightDetailViewModel.adultNumericPrice = it.adultNumeric
                            flightDetailViewModel.childNumericPrice = it.childNumeric
                            flightDetailViewModel.infantNumericPrice = it.infantNumeric
                        } else {
                            flightDetailViewModel.adultNumericPrice = it.adultNumericCombo
                            flightDetailViewModel.childNumericPrice = it.childNumericCombo
                            flightDetailViewModel.infantNumericPrice = it.infantNumericCombo
                        }
                    }

                } else if (index == 1) {
                    flightPriceModel.returnPrice?.let {
                        if (flightPriceModel.comboKey.isNullOrEmpty()) {
                            flightDetailViewModel.adultNumericPrice = it.adultNumeric
                            flightDetailViewModel.childNumericPrice = it.childNumeric
                            flightDetailViewModel.infantNumericPrice = it.infantNumeric
                        } else {
                            flightDetailViewModel.adultNumericPrice = it.adultNumericCombo
                            flightDetailViewModel.childNumericPrice = it.childNumericCombo
                            flightDetailViewModel.infantNumericPrice = it.infantNumericCombo
                        }
                    }
                }

                flightDetailViewModel.totalNumeric = flightDetailViewModel.adultNumericPrice + flightDetailViewModel.childNumericPrice + flightDetailViewModel.infantNumericPrice
                flightDetailViewModel.countAdult = flight.adult
                flightDetailViewModel.countChild = flight.child
                flightDetailViewModel.countInfant = flight.infant
                flightDetailViewModel.total = FlightCurrencyFormatUtil.convertPriceValueToIdrFormatNoSpace(flightDetailViewModel.totalNumeric)
                flightDetailViewModel.departureTime = journey.departureTime
                flightDetailViewModel.arrivalTime = journey.arrivalTime
                flightDetailViewModel.airlineDataList = listOf()

                val routeList = listOf<FlightDetailRouteModel>().toMutableList()
                for (route in journey.routes) {
                    val routeDetail = FlightDetailRouteModel()
                    routeDetail.airlineCode = route.airlineId

                    routeDetail.airlineName = ""
                    routeDetail.airlineLogo = ""
                    routeDetail.departureAirportCode = ""
                    routeDetail.departureAirportCity = ""
                    routeDetail.departureAirportName = ""

                    for (includedItem in included) {
                        if (route.airlineId.equals(includedItem.id, true)) {
                            routeDetail.airlineName = includedItem.attributes.name
                            routeDetail.airlineLogo = includedItem.attributes.logo
                        } else if (route.departureAirportId.equals(includedItem.id, true)) {
                            routeDetail.departureAirportCode = includedItem.id
                            routeDetail.departureAirportCity = includedItem.attributes.city
                            routeDetail.departureAirportName = includedItem.attributes.name
                        } else if (route.arrivalAirportId.equals(includedItem.id, true)) {
                            routeDetail.arrivalAirportCode = includedItem.id
                            routeDetail.arrivalAirportCity = includedItem.attributes.city
                            routeDetail.arrivalAirportName = includedItem.attributes.name
                        }
                    }
                    routeDetail.departureTimestamp = route.departureTime
                    routeDetail.arrivalTimestamp = route.arrivalTime
                    routeDetail.duration = route.duration
                    routeDetail.flightNumber = route.flightNumber
                    routeDetail.layover = route.layover
                    routeDetail.isRefundable = route.refundable
                    routeDetail.infos = transformToInfoDetail(route.infos)
                    routeDetail.amenities = route.amenities
                    routeDetail.stopOver = route.stop
                    routeDetail.stopOverDetail = transformToStopDetail(route.stopDetail)
                    routeDetail.operatingAirline = route.operatingAirlineId

                    routeList.add(routeDetail)
                }
                flightDetailViewModel.routeList = routeList


                list.add(flightDetailViewModel)
            }
            return list
        }

        private fun transformToStopDetail(stopDetails: List<FlightCart.StopDetail>): List<FlightStopOverViewModel> {
            val list = listOf<FlightStopOverViewModel>().toMutableList()
            for (stop in stopDetails) {
                val stopDetail = FlightStopOverViewModel()
                stopDetail.airportCode = stop.code
                stopDetail.cityName = stop.city
                list.add(stopDetail)
            }
            return list
        }

        private fun transformToInfoDetail(infos: List<FlightCart.Info>): List<FlightDetailRouteInfoModel> {
            val list = listOf<FlightDetailRouteInfoModel>().toMutableList()
            for (item in infos) {
                val infoDetail = FlightDetailRouteInfoModel()
                infoDetail.label = item.label
                infoDetail.value = item.value
                list.add(infoDetail)
            }
            return list
        }
    }
}