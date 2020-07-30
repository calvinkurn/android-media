package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardinquiry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 21/05/18.
 */
public class CardRequest {

    @SerializedName("issuer_id")
    @Expose
    private int issuerId;

    @SerializedName("attribute")
    @Expose
    private String attribute;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("uuid")
    @Expose
    private String uuid;

    @SerializedName("last_balance")
    @Expose
    private String lastBalance;

    public CardRequest(int issuerId, String attribute, String info, String uuid, String lastBalance) {
        this.issuerId = issuerId;
        this.attribute = attribute;
        this.info = info;
        this.uuid = uuid;
        this.lastBalance = lastBalance;
    }
}
