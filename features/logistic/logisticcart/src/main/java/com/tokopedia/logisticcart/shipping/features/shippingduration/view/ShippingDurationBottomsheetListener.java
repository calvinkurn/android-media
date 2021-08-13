package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData;
import com.tokopedia.logisticcart.shipping.model.CourierItemData;
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationBottomsheetListener {

    void onShippingDurationChoosen(List<ShippingCourierUiModel> shippingCourierUiModels,
                                   CourierItemData courierItemData,
                                   RecipientAddressModel recipientAddressModel,
                                   int cartPosition, int selectedServiceId,
                                   ServiceData serviceData, boolean flagNeedToSetPinpoint,
                                   boolean isDurationClick, boolean isClearPromo);

    void onLogisticPromoChosen(List<ShippingCourierUiModel> shippingCourierUiModels,
                               CourierItemData courierData,
                               RecipientAddressModel recipientAddressModel, int cartPosition,
                               ServiceData serviceData, boolean flagNeedToSetPinpoint, String promoCode, int selectedServiceId);

    void onNoCourierAvailable(String message);

    void onShippingDurationButtonCloseClicked();

    void onShowDurationListWithCourierPromo(boolean isCourierPromo, String duration);
}
