package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationBottomsheetListener {

    void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                                   CourierItemData courierItemData,
                                   RecipientAddressModel recipientAddressModel,
                                   int cartPosition, int selectedServiceId,
                                   ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                   boolean isDurationClick, boolean isClearPromo);

    void onLogisticPromoChosen(List<ShippingCourierViewModel> shippingCourierViewModels,
                               CourierItemData courierData,
                               RecipientAddressModel recipientAddressModel,
                               int cartPosition, int selectedServiceId,
                               ServiceData serviceData, boolean flagNeedToSetPinpoint, String promoCode);

    void onNoCourierAvailable(String message);

    void onShippingDurationButtonCloseClicked();

    void onShowDurationListWithCourierPromo(boolean isCourierPromo, String duration);
}
