package com.tokopedia.shipping_recommendation.shippingcourier.view;

import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;
import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShipmentCartItemModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierBottomsheetListener {

    void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, CourierItemData courierItemData, RecipientAddressModel recipientAddressModel,
                          int cartPosition, boolean hasCourierPromo, boolean isCod, boolean isPromoCourier, boolean isNeedPinpoint);

    void onCourierShipmentRecpmmendationCloseClicked();

    void onRetryReloadCourier(ShipmentCartItemModel shipmentCartItemModel, int cartPosition, List<ShopShipment> shopShipmentList);

}
