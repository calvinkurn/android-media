package com.tokopedia.logisticcart.shippingcourier.view;

import com.tokopedia.logisticcart.domain.shipping.CourierItemData;
import com.tokopedia.logisticcart.domain.shipping.RecipientAddressModel;
import com.tokopedia.logisticcart.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.logisticcart.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.logisticcart.domain.shipping.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierBottomsheetListener {

    void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                          int cartPosition, boolean isCod, boolean isPromoCourier, boolean isNeedPinpoint);

    void onCourierShipmentRecpmmendationCloseClicked();

    void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList);

}
