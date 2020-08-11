package com.tokopedia.flight.detail.view.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.OrderStopDetailEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.RouteEntity;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightStopOverViewModel;
import com.tokopedia.flight.searchV4.data.cloud.single.Route;
import com.tokopedia.flight.searchV4.data.cloud.single.StopDetailEntity;
import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 12/8/17.
 */

public class FlightDetailRouteModelMapper {
    private FlightDetailRouteInfoModelMapper flightDetailRouteInfoViewModelMapper;

    @Inject
    public FlightDetailRouteModelMapper(FlightDetailRouteInfoModelMapper flightDetailRouteInfoViewModelMapper) {
        this.flightDetailRouteInfoViewModelMapper = flightDetailRouteInfoViewModelMapper;
    }

    public FlightDetailRouteModel transform(Route route) {
        FlightDetailRouteModel flightDetailRouteViewModel = null;
        if (route != null) {
            flightDetailRouteViewModel = new FlightDetailRouteModel();
            flightDetailRouteViewModel.setAirlineCode(route.getAirline());
            flightDetailRouteViewModel.setAirlineName(route.getAirlineName());
            flightDetailRouteViewModel.setAirlineLogo(route.getAirlineLogo());
            flightDetailRouteViewModel.setArrivalAirportCity(route.getArrivalAirportCity());
            flightDetailRouteViewModel.setArrivalAirportName(route.getArrivalAirportName());
            flightDetailRouteViewModel.setArrivalTimestamp(route.getArrivalTimestamp());
            flightDetailRouteViewModel.setArrivalAirportCode(route.getArrivalAirport());
            flightDetailRouteViewModel.setDepartureAirportCity(route.getDepartureAirportCity());
            flightDetailRouteViewModel.setDepartureAirportName(route.getDepartureAirportName());
            flightDetailRouteViewModel.setDepartureTimestamp(route.getDepartureTimestamp());
            flightDetailRouteViewModel.setDepartureAirportCode(route.getDepartureAirport());
            flightDetailRouteViewModel.setDuration(route.getDuration());
            flightDetailRouteViewModel.setFlightNumber(route.getFlightNumber());
            flightDetailRouteViewModel.setLayover(route.getLayover());
            flightDetailRouteViewModel.setRefundable(route.getRefundable());
            flightDetailRouteViewModel.setInfos(flightDetailRouteInfoViewModelMapper.transform(route.getInfos()));
            flightDetailRouteViewModel.setAmenities(route.getAmenities());
            flightDetailRouteViewModel.setStopOver(route.getStops());
            flightDetailRouteViewModel.setStopOverDetail(transform(route.getStopDetails(), null));
            flightDetailRouteViewModel.setOperatingAirline(route.getOperatingAirline());
        }
        return flightDetailRouteViewModel;
    }

    private List<FlightStopOverViewModel> transform(List<StopDetailEntity> stopDetails, List<OrderStopDetailEntity> orderStopDetails) {
        List<FlightStopOverViewModel> details = new ArrayList<>();
        FlightStopOverViewModel viewModel = null;

        if (stopDetails != null) {
            for (StopDetailEntity entity : stopDetails) {
                viewModel = transform(entity);
                if (viewModel != null)
                    details.add(viewModel);
            }
        }
        if (orderStopDetails != null) {
            for (OrderStopDetailEntity entity : orderStopDetails) {
                viewModel = transform(entity);
                if (viewModel != null)
                    details.add(viewModel);
            }
        }

        return details;
    }

    private FlightStopOverViewModel transform(StopDetailEntity entity) {
        FlightStopOverViewModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightStopOverViewModel();
            viewModel.setAirportCode(entity.getCode());
            viewModel.setCityName(entity.getCity());
        }
        return viewModel;
    }

    private FlightStopOverViewModel transform(OrderStopDetailEntity entity) {
        FlightStopOverViewModel viewModel = null;
        if (entity != null) {
            viewModel = new FlightStopOverViewModel();
            viewModel.setAirportCode(entity.getCode());
            viewModel.setCityName(entity.getCity());
        }
        return viewModel;
    }

    public List<FlightDetailRouteModel> transform(List<Route> routes, int totalTransit) {
        List<FlightDetailRouteModel> flightDetailRouteViewModels = new ArrayList<>();
        FlightDetailRouteModel flightDetailRouteViewModel;
        if (routes != null) {
            for (Route route : routes) {
                flightDetailRouteViewModel = transform(route);
                if (flightDetailRouteViewModel != null && flightDetailRouteViewModels.size() <= totalTransit) {
                    flightDetailRouteViewModels.add(flightDetailRouteViewModel);
                }
            }
        }
        return flightDetailRouteViewModels;
    }

    public FlightDetailRouteModel transform(RouteEntity route) {
        FlightDetailRouteModel flightDetailRouteViewModel = null;
        if (route != null) {
            flightDetailRouteViewModel = new FlightDetailRouteModel();
            flightDetailRouteViewModel.setAirlineCode(route.getAirlineId());
            flightDetailRouteViewModel.setArrivalTimestamp(route.getArrivalTime());
            flightDetailRouteViewModel.setArrivalAirportCode(route.getArrivalAirportCode());
            flightDetailRouteViewModel.setArrivalAirportName(route.getArrivalAirportName());
            flightDetailRouteViewModel.setArrivalAirportCity(route.getArrivalCityName());
            flightDetailRouteViewModel.setDepartureTimestamp(route.getDepartureTime());
            flightDetailRouteViewModel.setDepartureAirportCode(route.getDepartureAirportCode());
            flightDetailRouteViewModel.setDepartureAirportName(route.getDepartureAirportName());
            flightDetailRouteViewModel.setDepartureAirportCity(route.getDepartureCityName());
            flightDetailRouteViewModel.setAirlineName(route.getAirlineName());
            flightDetailRouteViewModel.setAirlineLogo(route.getAirlineLogo());
            flightDetailRouteViewModel.setDuration(route.getDuration());
            flightDetailRouteViewModel.setLayover(route.getLayover());
            flightDetailRouteViewModel.setPnr(route.getPnr());
            flightDetailRouteViewModel.setFlightNumber(route.getFlightNumber());
            flightDetailRouteViewModel.setRefundable(route.isRefundable());
            flightDetailRouteViewModel.setStopOver(route.getStops());
            flightDetailRouteViewModel.setInfos(flightDetailRouteInfoViewModelMapper.transform(route.getFreeAmenities()));
            flightDetailRouteViewModel.setStopOverDetail(transform(null, route.getStopDetailEntities()));
            flightDetailRouteViewModel.setOperatingAirline(route.getOperatingAirline());

            if (route.getDepartureTerminal() != null && route.getDepartureTerminal().length() > 0) {
                flightDetailRouteViewModel.setDepartureTerminal(route.getDepartureTerminal());
            }

            if (route.getArrivalTerminal() != null && route.getArrivalTerminal().length() > 0) {
                flightDetailRouteViewModel.setArrivalTerminal(route.getArrivalTerminal());
            }
        }
        return flightDetailRouteViewModel;
    }

    public List<FlightDetailRouteModel> transformList(List<RouteEntity> routeEntities) {
        List<FlightDetailRouteModel> flightDetailRouteViewModels = new ArrayList<>();
        FlightDetailRouteModel flightDetailRouteViewModel;
        if (routeEntities != null) {
            for (RouteEntity route : routeEntities) {
                flightDetailRouteViewModel = transform(route);
                if (flightDetailRouteViewModel != null) {
                    flightDetailRouteViewModels.add(flightDetailRouteViewModel);
                }
            }
        }
        return flightDetailRouteViewModels;
    }

    private int getIndexFromId(List<FlightAirlineModel> airlineDBList, String id) {
        int index = -1;
        for (FlightAirlineModel airlineDB : airlineDBList) {
            index++;
            if (airlineDB.getId().equals(id)) {
                break;
            }
        }
        return index;
    }
}
