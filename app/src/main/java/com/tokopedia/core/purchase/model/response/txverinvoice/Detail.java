package com.tokopedia.core.purchase.model.response.txverinvoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 13/06/2016.
 */
public class Detail {
    private static final String TAG = Detail.class.getSimpleName();

    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("url")
    @Expose
    private String url;

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
