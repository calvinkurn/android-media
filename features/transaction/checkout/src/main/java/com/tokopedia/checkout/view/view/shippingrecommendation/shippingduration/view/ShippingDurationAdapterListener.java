package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationAdapterListener {

    void onShippingDurationChoosen(ServiceData serviceData);

}
