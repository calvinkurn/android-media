package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel;
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationAdapterListener {

    void onShippingDurationChoosen(List<ShippingCourierUiModel> shippingCourierUiModelList,
                                   int cartPosition, ServiceData serviceData);

    boolean isToogleYearEndPromotionOn();

    void onLogisticPromoClicked(LogisticPromoUiModel data);

}
