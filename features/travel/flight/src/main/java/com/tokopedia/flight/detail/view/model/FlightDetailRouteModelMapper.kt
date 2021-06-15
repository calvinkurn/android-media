package com.tokopedia.flight.detail.view.model

import com.tokopedia.flight.orderlist.data.cloud.entity.OrderStopDetailEntity
import com.tokopedia.flight.orderlist.data.cloud.entity.RouteEntity
import com.tokopedia.flight.orderlist.view.viewmodel.FlightStopOverViewModel
import com.tokopedia.flight.search.data.cloud.single.Route
import com.tokopedia.flight.search.data.cloud.single.StopDetailEntity
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel
import java.util.*
import javax.inject.Inject

/**
 * @author by alvarisi on 12/8/17.
 */
class FlightDetailRouteModelMapper @Inject constructor(
        private val flightDetailRouteInfoViewModelMapper: FlightDetailRouteInfoModelMapper) {

    private fun transform(route: Route?): FlightDetailRouteModel? {
        var flightDetailRouteViewModel: FlightDetailRouteModel? = null
        if (route != null) {
            flightDetailRouteViewModel = FlightDetailRouteModel()
            flightDetailRouteViewModel.airlineCode = route.airline
            flightDetailRouteViewModel.airlineName = route.airlineName
            flightDetailRouteViewModel.airlineLogo = route.airlineLogo
            flightDetailRouteViewModel.arrivalAirportCity = route.arrivalAirportCity
            flightDetailRouteViewModel.arrivalAirportName = route.arrivalAirportName
            flightDetailRouteViewModel.arrivalTimestamp = route.arrivalTimestamp
            flightDetailRouteViewModel.arrivalAirportCode = route.arrivalAirport
            flightDetailRouteViewModel.departureAirportCity = route.departureAirportCity
            flightDetailRouteViewModel.departureAirportName = route.departureAirportName
            flightDetailRouteViewModel.departureTimestamp = route.departureTimestamp
            flightDetailRouteViewModel.departureAirportCode = route.departureAirport
            flightDetailRouteViewModel.duration = route.duration
            flightDetailRouteViewModel.flightNumber = route.flightNumber
            flightDetailRouteViewModel.layover = route.layover
            flightDetailRouteViewModel.isRefundable = route.isRefundable
            flightDetailRouteViewModel.infos = flightDetailRouteInfoViewModelMapper.transform(route.infos)
            flightDetailRouteViewModel.amenities = route.amenities
            flightDetailRouteViewModel.stopOver = route.stops
            flightDetailRouteViewModel.stopOverDetail = transform(route.stopDetails, null)
            flightDetailRouteViewModel.operatingAirline = route.operatingAirline
        }
        return flightDetailRouteViewModel
    }

    private fun transform(stopDetails: List<StopDetailEntity>?, orderStopDetails: List<OrderStopDetailEntity>?): List<FlightStopOverViewModel> {
        val details: MutableList<FlightStopOverViewModel> = ArrayList()
        var viewModel: FlightStopOverViewModel? = null
        if (stopDetails != null) {
            for (entity in stopDetails) {
                viewModel = transform(entity)
                if (viewModel != null) details.add(viewModel)
            }
        }
        if (orderStopDetails != null) {
            for (entity in orderStopDetails) {
                viewModel = transform(entity)
                if (viewModel != null) details.add(viewModel)
            }
        }
        return details
    }

    private fun transform(entity: StopDetailEntity?): FlightStopOverViewModel? {
        var viewModel: FlightStopOverViewModel? = null
        if (entity != null) {
            viewModel = FlightStopOverViewModel()
            viewModel.airportCode = entity.code
            viewModel.cityName = entity.city
        }
        return viewModel
    }

    private fun transform(entity: OrderStopDetailEntity?): FlightStopOverViewModel? {
        var viewModel: FlightStopOverViewModel? = null
        if (entity != null) {
            viewModel = FlightStopOverViewModel()
            viewModel.airportCode = entity.code
            viewModel.cityName = entity.city
        }
        return viewModel
    }

    fun transform(routes: List<Route?>?, totalTransit: Int): List<FlightDetailRouteModel> {
        val flightDetailRouteViewModels: MutableList<FlightDetailRouteModel> = ArrayList()
        var flightDetailRouteViewModel: FlightDetailRouteModel?
        if (routes != null) {
            for (route in routes) {
                flightDetailRouteViewModel = transform(route)
                if (flightDetailRouteViewModel != null && flightDetailRouteViewModels.size <= totalTransit) {
                    flightDetailRouteViewModels.add(flightDetailRouteViewModel)
                }
            }
        }
        return flightDetailRouteViewModels
    }

    private fun transform(route: RouteEntity?): FlightDetailRouteModel? {
        var flightDetailRouteViewModel: FlightDetailRouteModel? = null
        if (route != null) {
            flightDetailRouteViewModel = FlightDetailRouteModel()
            flightDetailRouteViewModel.airlineCode = route.airlineId
            flightDetailRouteViewModel.arrivalTimestamp = route.arrivalTime
            flightDetailRouteViewModel.arrivalAirportCode = route.arrivalAirportCode
            flightDetailRouteViewModel.arrivalAirportName = route.arrivalAirportName
            flightDetailRouteViewModel.arrivalAirportCity = route.arrivalCityName
            flightDetailRouteViewModel.departureTimestamp = route.departureTime
            flightDetailRouteViewModel.departureAirportCode = route.departureAirportCode
            flightDetailRouteViewModel.departureAirportName = route.departureAirportName
            flightDetailRouteViewModel.departureAirportCity = route.departureCityName
            flightDetailRouteViewModel.airlineName = route.airlineName
            flightDetailRouteViewModel.airlineLogo = route.airlineLogo
            flightDetailRouteViewModel.duration = route.duration
            flightDetailRouteViewModel.layover = route.layover
            flightDetailRouteViewModel.pnr = route.pnr
            flightDetailRouteViewModel.flightNumber = route.flightNumber
            flightDetailRouteViewModel.isRefundable = route.isRefundable
            flightDetailRouteViewModel.stopOver = route.stops
            flightDetailRouteViewModel.infos = flightDetailRouteInfoViewModelMapper.transform(route.freeAmenities)
            flightDetailRouteViewModel.stopOverDetail = transform(null, route.stopDetailEntities)
            flightDetailRouteViewModel.operatingAirline = route.operatingAirline
            if (route.departureTerminal.isNotEmpty()) {
                flightDetailRouteViewModel.departureTerminal = route.departureTerminal
            }
            if (route.arrivalTerminal.isNotEmpty()) {
                flightDetailRouteViewModel.arrivalTerminal = route.arrivalTerminal
            }
        }
        return flightDetailRouteViewModel
    }

    fun transformList(routeEntities: List<RouteEntity?>?): List<FlightDetailRouteModel> {
        val flightDetailRouteViewModels: MutableList<FlightDetailRouteModel> = ArrayList()
        var flightDetailRouteViewModel: FlightDetailRouteModel?
        if (routeEntities != null) {
            for (route in routeEntities) {
                flightDetailRouteViewModel = transform(route)
                if (flightDetailRouteViewModel != null) {
                    flightDetailRouteViewModels.add(flightDetailRouteViewModel)
                }
            }
        }
        return flightDetailRouteViewModels
    }

    private fun getIndexFromId(airlineDBList: List<FlightAirlineModel>, id: String): Int {
        var index = -1
        for ((id1) in airlineDBList) {
            index++
            if (id1 == id) {
                break
            }
        }
        return index
    }

}