package com.tokopedia.digital.widget.view.model;

import com.tokopedia.digital.product.view.model.OrderClientNumber;

import java.util.List;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class DigitalNumberList {

    private List<OrderClientNumber> orderClientNumbers;
    private OrderClientNumber lastOrder;

    public DigitalNumberList(List<OrderClientNumber> orderClientNumbers, OrderClientNumber lastOrder) {
        this.orderClientNumbers = orderClientNumbers;
        this.lastOrder = lastOrder;
    }

    public List<OrderClientNumber> getOrderClientNumbers() {
        return orderClientNumbers;
    }

    public OrderClientNumber getLastOrder() {
        return lastOrder;
    }

}
