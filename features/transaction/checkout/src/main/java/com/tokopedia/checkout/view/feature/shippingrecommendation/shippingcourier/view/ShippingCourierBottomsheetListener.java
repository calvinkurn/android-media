package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierBottomsheetListener {

    void onCourierChoosen(CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                          int cartPosition, boolean isNeedPinpoint);

    void onCourierShipmentRecpmmendationCloseClicked();

}
