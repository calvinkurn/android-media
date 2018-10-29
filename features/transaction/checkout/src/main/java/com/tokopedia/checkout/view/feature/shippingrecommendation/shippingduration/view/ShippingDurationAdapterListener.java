package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view;

import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;
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
