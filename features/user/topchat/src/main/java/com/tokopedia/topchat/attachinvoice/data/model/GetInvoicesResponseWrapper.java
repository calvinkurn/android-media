package com.tokopedia.topchat.attachinvoice.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 28/03/18.
 */

public class GetInvoicesResponseWrapper {
    @SerializedName("data")
    @Expose
    GetInvoicesDataWrapper dataWrapper;

    public GetInvoicesDataWrapper getDataWrapper() {
        return dataWrapper;
    }

    public void setDataWrapper(GetInvoicesDataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }
}
