package com.tokopedia.checkout.view.view.shippingoptions;

import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public interface CourierAdapterActionListener {

    void onCourierItemClick(CourierItemData courierItemData);

    void onSelectedCourierItemLoaded(CourierItemData courierItemData);
}
