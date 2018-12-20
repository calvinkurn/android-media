package com.tokopedia.logisticinputreceiptshipment.network.mapper;

import com.tokopedia.logisticdata.data.entity.courierlist.CourierResponse;
import com.tokopedia.logisticdata.data.entity.courierlist.Shipment;
import com.tokopedia.transaction.common.data.order.CourierServiceModel;
import com.tokopedia.transaction.common.data.order.CourierViewModel;
import com.tokopedia.transaction.common.data.order.ListCourierViewModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailMapper {

    public ListCourierViewModel getCourierServiceModel(CourierResponse response,
                                                       String selectedCourierId) {
        ListCourierViewModel listCourierViewModel = new ListCourierViewModel();
        List<CourierViewModel> viewModelList = new ArrayList<>();
        if (response.getShipment() != null) {
            for (int i = 0; i < response.getShipment().size(); i++) {
                CourierViewModel courierViewModel = new CourierViewModel();
                courierViewModel.setSelected(
                        selectedCourierId.equals(response.getShipment().get(i).getShipmentId())
                );
                courierViewModel.setCourierId(response.getShipment().get(i).getShipmentId());
                courierViewModel.setCourierName(response.getShipment().get(i).getShipmentName());
                courierViewModel.setCourierImageUrl(response.getShipment().get(i).getShipmentImage());
                List<CourierServiceModel> courierServiceModelList = new ArrayList<>();
                for (int j = 0; j < response.getShipment().get(i).getShipmentPackage().size(); j++) {
                    CourierServiceModel courierServiceModel = new CourierServiceModel();
                    Shipment courierShipment = response.getShipment().get(i);
                    courierServiceModel.setServiceId(courierShipment.getShipmentPackage().get(j).getSpId());
                    courierServiceModel.setServiceName(courierShipment.getShipmentPackage().get(j).getName());
                    courierServiceModelList.add(courierServiceModel);
                }
                courierViewModel.setCourierServiceList(courierServiceModelList);
                viewModelList.add(courierViewModel);
            }
        }
        listCourierViewModel.setCourierViewModelList(viewModelList);
        return listCourierViewModel;
    }
}
