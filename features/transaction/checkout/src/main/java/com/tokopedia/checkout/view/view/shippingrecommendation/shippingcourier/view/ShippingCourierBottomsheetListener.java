package com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view;

import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierBottomsheetListener {

    void onCourierChoosen(CourierItemData courierItemData, int cartPosition);

}
