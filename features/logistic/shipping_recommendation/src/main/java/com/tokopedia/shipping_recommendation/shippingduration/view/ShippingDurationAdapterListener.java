package com.tokopedia.shipping_recommendation.shippingduration.view;

import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationAdapterListener {

    void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModelList,
                                   int cartPosition, ServiceData serviceData, boolean hasCourierPromo);

    void onAllShippingDurationItemShown();

    void onDurationShipmentRecommendationShowCaseClosed();

    boolean isToogleYearEndPromotionOn();
}
