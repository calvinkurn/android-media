package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 18/05/18.
 */
public class Attributes {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("card_response")
    @Expose
    private CardResponse cardResponse;
    @SerializedName("error")
    @Expose
    private Error error;

    public int getStatus() {
        return status;
    }

    public CardResponse getCardResponse() {
        return cardResponse;
    }

    public Error getError() {
        return error;
    }

}
