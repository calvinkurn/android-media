package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 21/05/18.
 */
public class Attributes {

    @SerializedName("card_request")
    @Expose
    private CardRequest cardRequest;

    public Attributes(CardRequest cardRequest) {
        this.cardRequest = cardRequest;
    }

}
