package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingDurationConverter {

    public List<ShippingDurationViewModel> convertToViewModel(List<ServiceData> serviceDataList) {
        List<ShippingDurationViewModel> shippingDurationViewModels = new ArrayList<>();
        for (ServiceData serviceData : serviceDataList) {
            ShippingDurationViewModel shippingDurationViewModel = new ShippingDurationViewModel();
            shippingDurationViewModel.setServiceData(serviceData);
            shippingDurationViewModel.setSelected(false);
            shippingDurationViewModels.add(shippingDurationViewModel);
        }

        return shippingDurationViewModels;
    }

}
