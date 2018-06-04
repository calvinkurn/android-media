package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

public class ChangeAddressRequest {

    @SerializedName("carts")
    @Expose
    List<DataChangeAddressRequest> addressRequests;

    public void setAddressRequests(List<DataChangeAddressRequest> addressRequests) {
        this.addressRequests = addressRequests;
    }
}
