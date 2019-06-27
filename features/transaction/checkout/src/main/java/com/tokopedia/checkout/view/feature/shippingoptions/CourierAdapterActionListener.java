package com.tokopedia.checkout.view.feature.shippingoptions;

import com.tokopedia.shipping_recommendation.domain.shipping.CourierItemData;

/**
 * @author Irfan Khoirul on 04/05/18.
 */

public interface CourierAdapterActionListener {

    void onCourierItemClick(CourierItemData courierItemData);

    void onSelectedCourierItemLoaded(CourierItemData courierItemData);
}
