package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view;

import com.tokopedia.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.checkout.view.feature.shipment.viewmodel.ShipmentCartItemModel;

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
