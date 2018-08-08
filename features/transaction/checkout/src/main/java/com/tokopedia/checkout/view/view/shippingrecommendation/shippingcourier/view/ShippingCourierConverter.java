package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class ShippingCourierConverter {

    public List<ShippingCourierViewModel> convertToViewModel(List<ProductData> productDataList) {
        List<ShippingCourierViewModel> shippingCourierViewModels = new ArrayList<>();
        for (ProductData productData : productDataList) {
            ShippingCourierViewModel shippingCourierViewModel = new ShippingCourierViewModel();
            shippingCourierViewModel.setProductData(productData);
            shippingCourierViewModel.setSelected(false);
            shippingCourierViewModels.add(shippingCourierViewModel);
        }

        return shippingCourierViewModels;
    }

}
