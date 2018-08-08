package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ProductData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierAdapterListener {

    void onCourierChoosen(ProductData productData);

}
