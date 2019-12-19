package com.tokopedia.tkpd.thankyou.data.pojo.marketplace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderInfoGraphql;

public class OrderGraphql {
    @SerializedName("get_order_info")
    @Expose
    private OrderInfoGraphql orderInfoGraphql;

    public OrderInfoGraphql getOrderInfoGraphql() {
        return orderInfoGraphql;
    }

    public void setOrderInfoGraphql(OrderInfoGraphql orderInfoGraphql) {
        this.orderInfoGraphql = orderInfoGraphql;
    }
}
