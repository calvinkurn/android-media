package com.tokopedia.digital.product.additionalfeature.etoll.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 18/05/18.
 */
public class Error {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("title")
    @Expose
    private String title;

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

}