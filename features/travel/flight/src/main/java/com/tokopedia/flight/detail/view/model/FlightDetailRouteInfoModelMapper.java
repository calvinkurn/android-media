package com.tokopedia.flight.detail.view.model;

import com.tokopedia.flight.orderlist.data.cloud.entity.AmenityEntity;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailRouteInfoViewModel;
import com.tokopedia.flight.searchV4.data.cloud.single.Amenity;
import com.tokopedia.flight.searchV4.data.cloud.single.Info;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/8/17.
 */

public class FlightDetailRouteInfoModelMapper {
    @Inject
    public FlightDetailRouteInfoModelMapper() {

    }

    public FlightDetailRouteInfoModel transform(Info info) {
        FlightDetailRouteInfoModel viewModel = null;
        if (info != null) {
            viewModel = new FlightDetailRouteInfoModel();
            viewModel.setLabel(info.getLabel());
            viewModel.setValue(info.getValue());
        }
        return viewModel;
    }

    public FlightDetailRouteInfoModel transform(FlightOrderDetailRouteInfoViewModel info) {
        FlightDetailRouteInfoModel viewModel = null;
        if (info != null) {
            viewModel = new FlightDetailRouteInfoModel();
            viewModel.setLabel(info.getLabel());
            viewModel.setValue(info.getValue());
        }
        return viewModel;
    }

    public List<FlightDetailRouteInfoModel> transform(List<Info> infos) {
        List<FlightDetailRouteInfoModel> viewModels = new ArrayList<>();
        FlightDetailRouteInfoModel viewModel;
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

    public List<FlightDetailRouteInfoModel> transformOrderInfo(List<FlightOrderDetailRouteInfoViewModel> infos) {
        List<FlightDetailRouteInfoModel> viewModels = new ArrayList<>();
        FlightDetailRouteInfoModel viewModel;
        if (infos != null) {
            for (FlightOrderDetailRouteInfoViewModel info : infos) {
                viewModel = transform(info);
                if (viewModel != null) {
                    viewModels.add(viewModel);
                }
            }
        }
        return viewModels;
    }

    public List<FlightDetailRouteInfoModel> transform(AmenityEntity freeAmenities) {
        List<FlightDetailRouteInfoModel> routeInfoViewModels = new ArrayList<>();
        if (freeAmenities != null) {
            if (freeAmenities.getCabinBaggage() != null) {
                FlightDetailRouteInfoModel infoViewModel = new FlightDetailRouteInfoModel();
                infoViewModel.setLabel(freeAmenities.getCabinBaggage().getValue());
                infoViewModel.setValue(freeAmenities.getCabinBaggage().getUnit());
                routeInfoViewModels.add(infoViewModel);
            }
            if (freeAmenities.getFreeBaggage() != null) {
                FlightDetailRouteInfoModel infoViewModel = new FlightDetailRouteInfoModel();
                infoViewModel.setLabel(freeAmenities.getFreeBaggage().getValue());
                infoViewModel.setValue(freeAmenities.getFreeBaggage().getUnit());
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isMeal()) {
                FlightDetailRouteInfoModel infoViewModel = new FlightDetailRouteInfoModel();
                infoViewModel.setLabel("Meal");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isUsbPort()) {
                FlightDetailRouteInfoModel infoViewModel = new FlightDetailRouteInfoModel();
                infoViewModel.setLabel("Usb");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
            if (!freeAmenities.isWifi()) {
                FlightDetailRouteInfoModel infoViewModel = new FlightDetailRouteInfoModel();
                infoViewModel.setLabel("Wifi");
                infoViewModel.setValue("-");
                routeInfoViewModels.add(infoViewModel);
            }
        }
        return routeInfoViewModels;
    }

    public List<Amenity> transformOrderAmenities(List<com.tokopedia.flight.orderlist.data.cloud.entity.Amenity> amenities) {
        List<Amenity> dataList = new ArrayList<>();

        if (amenities != null) {
            for (com.tokopedia.flight.orderlist.data.cloud.entity.Amenity item : amenities) {
                dataList.add(transform(item));
            }
        }

        return dataList;
    }

    public Amenity transform(com.tokopedia.flight.orderlist.data.cloud.entity.Amenity amenity) {
        Amenity data = new Amenity();
        data.setIcon(amenity.getIcon());
        data.setLabel(amenity.getLabel());
        return data;
    }
}
