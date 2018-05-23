package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 22/05/18.
 */
public class CardRequest {

    @SerializedName("payload")
    @Expose
    private String payload;

    public CardRequest(String payload) {
        this.payload = payload;
    }

}
