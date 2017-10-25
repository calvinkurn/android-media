package com.tokopedia.digital.widget.model;

import com.tokopedia.digital.product.model.OrderClientNumber;

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

    public void setOrderClientNumbers(List<OrderClientNumber> orderClientNumbers) {
        this.orderClientNumbers = orderClientNumbers;
    }

    public OrderClientNumber getLastOrder() {
        return lastOrder;
    }

    public void setLastOrder(OrderClientNumber lastOrder) {
        this.lastOrder = lastOrder;
    }
}
