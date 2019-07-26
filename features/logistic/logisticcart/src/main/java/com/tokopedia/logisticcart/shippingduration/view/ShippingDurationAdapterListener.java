package com.tokopedia.logisticcart.shippingduration.view;

import com.tokopedia.logisticcart.domain.shipping.LogisticPromoViewModel;
import com.tokopedia.logisticcart.domain.shipping.ShippingCourierViewModel;
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
