package com.tokopedia.flight.bookingV2.presentation.viewmodel.mapper;

import com.tokopedia.flight.bookingV2.data.cloud.entity.Amenity;
import com.tokopedia.flight.bookingV2.data.cloud.entity.AmenityItem;
import com.tokopedia.flight.bookingV2.presentation.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingAmenityViewModelMapper {
    @Inject
    public FlightBookingAmenityViewModelMapper() {
    }

    public List<FlightBookingAmenityViewModel> transform(Amenity entity) {
        List<FlightBookingAmenityViewModel> viewModels = new ArrayList<>();
        FlightBookingAmenityViewModel data = null;
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

    private FlightBookingAmenityViewModel transform(int type, AmenityItem item) {
        FlightBookingAmenityViewModel viewModel = null;
        if (item != null) {
            viewModel = new FlightBookingAmenityViewModel();
            viewModel.setId(item.getId());
            viewModel.setPrice(item.getPrice());
            viewModel.setPriceNumeric(item.getPriceNumeric());
            viewModel.setTitle(item.getDescription());
            viewModel.setAmenityType(type);
        }
        return viewModel;
    }

}
