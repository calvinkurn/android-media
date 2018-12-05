package com.tokopedia.shipping_recommendation.shippingcourier.view;

import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.RecipientAddressModel;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierBottomsheetListener {

    void onCourierChoosen(CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                          int cartPosition, boolean hasCourierPromo, boolean isPromoCourier, boolean isNeedPinpoint);

    void onCourierShipmentRecpmmendationCloseClicked();

    void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList);

}
