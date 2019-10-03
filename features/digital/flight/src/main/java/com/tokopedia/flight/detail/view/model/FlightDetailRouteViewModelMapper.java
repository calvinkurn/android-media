package com.tokopedia.flight.detail.view.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.Amenity;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderStopDetailEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.RouteEntity;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightStopOverViewModel;
import com.tokopedia.flight.searchV3.data.api.single.response.Route;
import com.tokopedia.flight.searchV3.data.api.single.response.StopDetailEntity;
import com.tokopedia.flight.searchV3.presentation.model.FlightAirlineViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 12/8/17.
 */

public class FlightDetailRouteViewModelMapper {
    private FlightDetailRouteInfoViewModelMapper flightDetailRouteInfoViewModelMapper;

    @Inject
    public FlightDetailRouteViewModelMapper(FlightDetailRouteInfoViewModelMapper flightDetailRouteInfoViewModelMapper) {
        this.flightDetailRouteInfoViewModelMapper = flightDetailRouteInfoViewModelMapper;
    }

    public FlightDetailRouteViewModel transform(Route route) {
        FlightDetailRouteViewModel flightDetailRouteViewModel = null;
        if (route != null) {
            flightDetailRouteViewModel = new FlightDetailRouteViewModel();
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
            flightDetailRouteViewModel.setAmenities(transform(route.getAmenities()));
            flightDetailRouteViewModel.setStopOver(route.getStops());
            flightDetailRouteViewModel.setStopOverDetail(transform(route.getStopDetails(), null));
        }
        return flightDetailRouteViewModel;
    }

    private List<Amenity> transform(List<com.tokopedia.flight.searchV3.data.api.single.response.Amenity> amenities) {
        List<Amenity> amenityList = new ArrayList<>();

        for (com.tokopedia.flight.searchV3.data.api.single.response.Amenity item : amenities) {
            Amenity model = new Amenity();
            model.setDefault(item.isDefault());
            model.setIcon(item.getIcon());
            model.setLabel(item.getLabel());

            amenityList.add(model);
        }

        return amenityList;
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

    public List<FlightDetailRouteViewModel> transform(List<Route> routes, int totalTransit) {
        List<FlightDetailRouteViewModel> flightDetailRouteViewModels = new ArrayList<>();
        FlightDetailRouteViewModel flightDetailRouteViewModel;
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

    public FlightDetailRouteViewModel transform(RouteEntity route) {
        FlightDetailRouteViewModel flightDetailRouteViewModel = null;
        if (route != null) {
            flightDetailRouteViewModel = new FlightDetailRouteViewModel();
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

            if (route.getDepartureTerminal() != null && route.getDepartureTerminal().length() > 0) {
                flightDetailRouteViewModel.setDepartureTerminal(route.getDepartureTerminal());
            }

            if (route.getArrivalTerminal() != null && route.getArrivalTerminal().length() > 0) {
                flightDetailRouteViewModel.setArrivalTerminal(route.getArrivalTerminal());
            }
        }
        return flightDetailRouteViewModel;
    }

    public List<FlightDetailRouteViewModel> transformList(List<RouteEntity> routeEntities) {
        List<FlightDetailRouteViewModel> flightDetailRouteViewModels = new ArrayList<>();
        FlightDetailRouteViewModel flightDetailRouteViewModel;
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

    private int getIndexFromId(List<FlightAirlineViewModel> airlineDBList, String id) {
        int index = -1;
        for (FlightAirlineViewModel airlineDB : airlineDBList) {
            index++;
            if (airlineDB.getId().equals(id)) {
                break;
            }
        }
        return index;
    }
}
