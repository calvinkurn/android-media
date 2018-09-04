package com.tokopedia.checkout.view.view.shippingrecommendation.shippingduration.view;

import com.tokopedia.checkout.view.view.shippingrecommendation.shippingcourier.view.ShippingCourierViewModel;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationAdapterListener {

    void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModelList, int cartPosition);

    void onAllShippingDurationItemShown();

}
