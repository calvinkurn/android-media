package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingduration.view;

import com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationAdapterListener {

    void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModelList,
                                   int cartPosition, String serviceName, boolean hasCourierPromo);

    void onAllShippingDurationItemShown();

    void onDurationShipmentRecommendationShowCaseClosed();

    boolean isToogleYearEndPromotionOn();
}
