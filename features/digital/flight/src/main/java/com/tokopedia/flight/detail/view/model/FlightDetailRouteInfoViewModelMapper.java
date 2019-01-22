package com.tokopedia.flight.detail.view.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.AmenityEntity;
import com.tokopedia.flight.search.data.api.single.response.Info;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/8/17.
 */

public class FlightDetailRouteInfoViewModelMapper {
    @Inject
    public FlightDetailRouteInfoViewModelMapper() {

    }

    public FlightDetailRouteInfoViewModel transform(Info info) {
        FlightDetailRouteInfoViewModel viewModel = null;
        if (info != null) {
            viewModel = new FlightDetailRouteInfoViewModel();
            viewModel.setLabel(info.getLabel());
            viewModel.setValue(info.getValue());
        }
        return viewModel;
    }

    public List<FlightDetailRouteInfoViewModel> transform(List<Info> infos) {
        List<FlightDetailRouteInfoViewModel> viewModels = new ArrayList<>();
        FlightDetailRouteInfoViewModel viewModel;
        if (infos != null) {
            for (Info info : infos) {
                viewModel = transform(info);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }

    public List<FlightDetailRouteInfoViewModel> transform(AmenityEntity freeAmenities) {
        List<FlightDetailRouteInfoViewModel> routeInfoViewModels = new ArrayList<>();
        if (freeAmenities != null) {
            if (freeAmenities.getCabinBaggage() != null) {
                FlightDetailRouteInfoViewModel infoViewModel = new FlightDetailRouteInfoViewModel();
                infoViewModel.setLabel(freeAmenities.getCabinBaggage().getValue());
                infoViewModel.setValue(freeAmenities.getCabinBaggage().getUnit());
                routeInfoViewModels.add(infoViewModel);
            }
            if (freeAmenities.getFreeBaggage() != null) {
                FlightDetailRouteInfoViewModel infoViewModel = new FlightDetailRouteInfoViewModel();
                infoViewModel.setLabel(freeAmenities.getFreeBaggage().getValue());
                infoViewModel.setValue(freeAmenities.getFreeBaggage().getUnit());
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isMeal()) {
                FlightDetailRouteInfoViewModel infoViewModel = new FlightDetailRouteInfoViewModel();
                infoViewModel.setLabel("Meal");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isUsbPort()) {
                FlightDetailRouteInfoViewModel infoViewModel = new FlightDetailRouteInfoViewModel();
                infoViewModel.setLabel("Usb");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isWifi()) {
                FlightDetailRouteInfoViewModel infoViewModel = new FlightDetailRouteInfoViewModel();
                infoViewModel.setLabel("Wifi");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
        }
        return routeInfoViewModels;
    }
}
