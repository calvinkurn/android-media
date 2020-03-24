package com.tokopedia.flight.bookingV2.presentation.model.mapper;

import com.tokopedia.flight.bookingV2.data.cloud.entity.Amenity;
import com.tokopedia.flight.bookingV2.data.cloud.entity.AmenityItem;
import com.tokopedia.flight.bookingV2.presentation.model.FlightBookingAmenityModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingAmenityModelMapper {
    @Inject
    public FlightBookingAmenityModelMapper() {
    }

    public List<FlightBookingAmenityModel> transform(Amenity entity) {
        List<FlightBookingAmenityModel> viewModels = new ArrayList<>();
        FlightBookingAmenityModel data = null;
        if (entity != null) {
            for (AmenityItem item : entity.getItems()) {
                data = transform(entity.getType(), item);
                if (data != null) {
                    viewModels.add(data);
                }
            }
        }
        return viewModels;
    }

    private FlightBookingAmenityModel transform(int type, AmenityItem item) {
        FlightBookingAmenityModel viewModel = null;
        if (item != null) {
            viewModel = new FlightBookingAmenityModel();
            viewModel.setId(item.getId());
            viewModel.setPrice(item.getPrice());
            viewModel.setPriceNumeric(item.getPriceNumeric());
            viewModel.setTitle(item.getDescription());
            viewModel.setAmenityType(type);
        }
        return viewModel;
    }

}
