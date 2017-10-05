package com.tokopedia.digital.widget.listener;

import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.digital.product.model.OrderClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public interface BaseDigitalWidgetView {

    void renderNumberList(List<OrderClientNumber> results);

//    void renderLastOrderFromApi(LastOrder lastOrder);

//    void renderLastOrderFromCache(LastOrder lastOrder);

    void renderLastOrder(LastOrder lastOrder);

}
