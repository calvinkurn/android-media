package com.tokopedia.shipping_recommendation.shippingduration.view;

import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.CourierItemData;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.RecipientAddressModel;
import com.tokopedia.logisticdata.data.domain.datamodel.shipmentrates.ShippingCourierViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationBottomsheetListener {

    void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                   CourierItemData courierItemData,
                                   RecipientAddressModel recipientAddressModel,
                                   int cartPosition, int selectedServiceId,
                                   String selectedServiceName, boolean flagNeedToSetPinpoint,
                                   boolean hasCourierPromo);

    void onNoCourierAvailable(String message);

    void onShippingDurationButtonCloseClicked();

    void onShippingDurationButtonShowCaseDoneClicked();

    void onShowDurationListWithCourierPromo(boolean isCourierPromo, String duration);
}
