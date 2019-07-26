package com.tokopedia.logisticcart.shipping.features.shippingduration.view;

import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel;
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel;
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ServiceData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public interface ShippingDurationAdapterListener {

    void onShippingDurationChoosen(List<ShippingCourierViewModel> shippingCourierViewModelList,
                                   int cartPosition, ServiceData serviceData);

    boolean isToogleYearEndPromotionOn();

    void onLogisticPromoClicked(LogisticPromoViewModel data);

}
