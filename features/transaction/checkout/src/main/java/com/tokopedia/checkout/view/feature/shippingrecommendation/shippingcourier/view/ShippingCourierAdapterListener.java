package com.tokopedia.checkout.view.feature.shippingrecommendation.shippingcourier.view;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierAdapterListener {

    void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, int cartPosition, boolean hasCourierPromo, boolean isNeedPinpoint);

    boolean isToogleYearEndPromotionOn();

}
