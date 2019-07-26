package com.tokopedia.logisticcart.shipping.features.shippingcourier.view;


import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingCourierAdapterListener {

    void onCourierChoosen(ShippingCourierViewModel shippingCourierViewModel, int cartPosition, boolean isNeedPinpoint);

    boolean isToogleYearEndPromotionOn();

}
