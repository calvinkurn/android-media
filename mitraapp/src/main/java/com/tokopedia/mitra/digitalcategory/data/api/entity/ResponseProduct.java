package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 31/08/18.
 */
class ResponseProduct {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("attributes")
    @Expose
    private ResponseProductAttributes attributes;

    public String getId() {
        return id;
    }

    public ResponseProductAttributes getAttributes() {
        return attributes;
    }

}
