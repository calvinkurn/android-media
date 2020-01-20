package com.tokopedia.flight.orderlist.view.viewmodel;

import com.tokopedia.flight.orderlist.data.cloud.entity.AmenityEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.Info;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/8/17.
 */

public class FlightOrderDetailRouteInfoViewModelMapper {
    @Inject
    public FlightOrderDetailRouteInfoViewModelMapper() {

    }

    public FlightOrderDetailRouteInfoViewModel transform(Info info) {
        FlightOrderDetailRouteInfoViewModel viewModel = null;
        if (info != null) {
            viewModel = new FlightOrderDetailRouteInfoViewModel();
            viewModel.setLabel(info.getLabel());
            viewModel.setValue(info.getValue());
        }
        return viewModel;
    }

    public List<FlightOrderDetailRouteInfoViewModel> transform(List<Info> infos) {
        List<FlightOrderDetailRouteInfoViewModel> viewModels = new ArrayList<>();
        FlightOrderDetailRouteInfoViewModel viewModel;
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

    public List<FlightOrderDetailRouteInfoViewModel> transform(AmenityEntity freeAmenities) {
        List<FlightOrderDetailRouteInfoViewModel> routeInfoViewModels = new ArrayList<>();
        if (freeAmenities != null) {
            if (freeAmenities.getCabinBaggage() != null) {
                FlightOrderDetailRouteInfoViewModel infoViewModel = new FlightOrderDetailRouteInfoViewModel();
                infoViewModel.setLabel(freeAmenities.getCabinBaggage().getValue());
                infoViewModel.setValue(freeAmenities.getCabinBaggage().getUnit());
                routeInfoViewModels.add(infoViewModel);
            }
            if (freeAmenities.getFreeBaggage() != null) {
                FlightOrderDetailRouteInfoViewModel infoViewModel = new FlightOrderDetailRouteInfoViewModel();
                infoViewModel.setLabel(freeAmenities.getFreeBaggage().getValue());
                infoViewModel.setValue(freeAmenities.getFreeBaggage().getUnit());
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isMeal()) {
                FlightOrderDetailRouteInfoViewModel infoViewModel = new FlightOrderDetailRouteInfoViewModel();
                infoViewModel.setLabel("Meal");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isUsbPort()) {
                FlightOrderDetailRouteInfoViewModel infoViewModel = new FlightOrderDetailRouteInfoViewModel();
                infoViewModel.setLabel("Usb");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isWifi()) {
                FlightOrderDetailRouteInfoViewModel infoViewModel = new FlightOrderDetailRouteInfoViewModel();
                infoViewModel.setLabel("Wifi");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
        }
        return routeInfoViewModels;
    }
}
