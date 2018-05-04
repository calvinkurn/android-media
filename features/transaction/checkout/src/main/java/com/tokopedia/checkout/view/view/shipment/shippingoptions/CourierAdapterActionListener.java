package com.tokopedia.checkout.view.view.shipment.shippingoptions;

import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;

public interface CourierAdapterActionListener {

    void onCourierItemClick(CourierItemData courierItemData);

    void onSelectedCourierItemLoaded(CourierItemData courierItemData);
}
