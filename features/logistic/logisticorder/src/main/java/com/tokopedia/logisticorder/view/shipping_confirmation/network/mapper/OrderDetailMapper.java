package com.tokopedia.logisticorder.view.shipping_confirmation.network.mapper;

import com.tokopedia.logisticCommon.data.entity.courierlist.CourierResponse;
import com.tokopedia.logisticCommon.data.entity.courierlist.Shipment;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.CourierServiceModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.CourierUiModel;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.ListCourierUiModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailMapper {

    public ListCourierUiModel getCourierServiceModel(CourierResponse response,
                                                     String selectedCourierId) {
        ListCourierUiModel listCourierUiModel = new ListCourierUiModel();
        List<CourierUiModel> uiModelList = new ArrayList<>();
        if (response.getShipment() != null) {
            for (int i = 0; i < response.getShipment().size(); i++) {
                CourierUiModel courierUiModel = new CourierUiModel();
                courierUiModel.setSelected(
                        selectedCourierId.equals(response.getShipment().get(i).getShipmentId())
                );
                courierUiModel.setCourierId(response.getShipment().get(i).getShipmentId());
                courierUiModel.setCourierName(response.getShipment().get(i).getShipmentName());
                courierUiModel.setCourierImageUrl(response.getShipment().get(i).getShipmentImage());
                List<CourierServiceModel> courierServiceModelList = new ArrayList<>();
                for (int j = 0; j < response.getShipment().get(i).getShipmentPackage().size(); j++) {
                    CourierServiceModel courierServiceModel = new CourierServiceModel();
                    Shipment courierShipment = response.getShipment().get(i);
                    courierServiceModel.setServiceId(courierShipment.getShipmentPackage().get(j).getSpId());
                    courierServiceModel.setServiceName(courierShipment.getShipmentPackage().get(j).getName());
                    courierServiceModelList.add(courierServiceModel);
                }
                courierUiModel.setCourierServiceList(courierServiceModelList);
                uiModelList.add(courierUiModel);
            }
        }
        listCourierUiModel.setCourierUiModelList(uiModelList);
        return listCourierUiModel;
    }
}
