package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.requestbody.smartcardcommand;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 22/05/18.
 */
public class RequestBodySmartcardCommand {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("attributes")
    @Expose
    private Attributes attributes;

    public RequestBodySmartcardCommand(String type, int id, Attributes attributes) {
        this.type = type;
        this.id = id;
        this.attributes = attributes;
    }

}