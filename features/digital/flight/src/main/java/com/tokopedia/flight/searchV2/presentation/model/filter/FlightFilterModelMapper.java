package com.tokopedia.flight.searchV2.presentation.model.filter;

import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;

/**
 * Created by Rizky on 17/10/18.
 */
public class FlightFilterModelMapper {

    public FlightFilterModel map(com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel flightFilterModel) {
        FlightFilterModel result = new FlightFilterModel();
        result.setTransitTypeList(flightFilterModel.getTransitTypeList());
        result.setAirlineList(flightFilterModel.getAirlineList());
        result.setDepartureTimeList(flightFilterModel.getDepartureTimeList());
        result.setRefundableTypeList(flightFilterModel.getRefundableTypeList());
        result.setDurationMax(flightFilterModel.getDurationMax());
        result.setDurationMin(flightFilterModel.getDurationMin());
        result.setPriceMax(flightFilterModel.getPriceMax());
        result.setPriceMin(flightFilterModel.getPriceMin());
        result.setSpecialPrice(flightFilterModel.isSpecialPrice());
        result.setHasFilter(flightFilterModel.hasFilter());
        return result;
    }

    public com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel map2(FlightFilterModel flightFilterModel) {
        com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel result =
                new com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel();
        result.setTransitTypeList(flightFilterModel.getTransitTypeList());
        result.setAirlineList(flightFilterModel.getAirlineList());
        result.setDepartureTimeList(flightFilterModel.getDepartureTimeList());
        result.setRefundableTypeList(flightFilterModel.getRefundableTypeList());
        result.setDurationMax(flightFilterModel.getDurationMax());
        result.setDurationMin(flightFilterModel.getDurationMin());
        result.setPriceMax(flightFilterModel.getPriceMax());
        result.setPriceMin(flightFilterModel.getPriceMin());
        result.setSpecialPrice(flightFilterModel.isSpecialPrice());
        result.setHasFilter(flightFilterModel.hasFilter());
        return result;
    }

}
