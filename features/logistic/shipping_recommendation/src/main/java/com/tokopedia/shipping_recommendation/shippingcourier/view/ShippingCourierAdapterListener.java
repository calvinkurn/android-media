package com.tokopedia.shipping_recommendation.shippingcourier.view;


import com.tokopedia.shipping_recommendation.domain.shipping.ShippingCourierViewModel;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierAdapterListener {

    void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, int cartPosition, boolean isNeedPinpoint);

    boolean isToogleYearEndPromotionOn();

}
