package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 18/05/18.
 */
public class CardResponse {

    @SerializedName("payload")
    @Expose
    private String payload;
    @SerializedName("card_number")
    @Expose
    private String cardNumber;
    @SerializedName("last_balance")
    @Expose
    private int lastBalance;
    @SerializedName("issuer_image")
    @Expose
    private String issuerImage;
    @SerializedName("button_text")
    @Expose
    private String buttonText;

    public String getPayload() {
        return payload;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getLastBalance() {
        return lastBalance;
    }

    public String getIssuerImage() {
        return issuerImage;
    }

    public String getButtonText() {
        return buttonText;
    }

}
