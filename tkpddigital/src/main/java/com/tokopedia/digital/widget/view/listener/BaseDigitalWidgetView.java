package com.tokopedia.digital.widget.view.listener;

import com.tokopedia.digital.product.view.model.OrderClientNumber;
import com.tokopedia.digital.widget.view.model.lastorder.LastOrder;

import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */
@Deprecated
public interface BaseDigitalWidgetView {

    void renderNumberList(List<OrderClientNumber> results);

    void renderLastOrder(LastOrder lastOrder);

}
