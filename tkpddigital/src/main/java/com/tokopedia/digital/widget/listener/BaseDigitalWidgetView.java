package com.tokopedia.digital.widget.listener;

import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;

import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public interface BaseDigitalWidgetView {

    void renderNumberList(List<OrderClientNumber> results);

    void renderLastOrder(LastOrder lastOrder);

}
