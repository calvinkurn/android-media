package com.tokopedia.gm.common.data.source.cloud.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class RequestCashbackModel {
    @SerializedName("product_id")
    private Long product_id;
    @SerializedName("cashback")
    private int cashback;

    public RequestCashbackModel(Long product_id, int cashback) {
        this.product_id = product_id;
        this.cashback = cashback;
    }
}
