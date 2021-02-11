package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;

import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierBottomsheetListener {

    void onCourierChoosen(ShippingCourierUiModel shippingCourierUiModel, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                          int cartPosition, boolean isCod, boolean isPromoCourier, boolean isNeedPinpoint);

    void onCourierShipmentRecpmmendationCloseClicked();

    void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition);

}
