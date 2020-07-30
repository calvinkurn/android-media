package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 18/05/18.
 */
public class Response {

    @SerializedName("data")
    @Expose
    private ResponseSmartcard data;

    public ResponseSmartcard getData() {
        return data;
    }

}
