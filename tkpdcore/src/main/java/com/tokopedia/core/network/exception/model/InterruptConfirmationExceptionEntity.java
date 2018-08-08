package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 3/29/17.
 */

public class InterruptConfirmationExceptionEntity {
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("Status")
    @Expose
    String Status;

    @SerializedName("errors")
    @Expose
    List<ErrorHttpEntity> errors;

    @SerializedName("meta")
    @Expose
    InterruptConfirmationMetaEntity meta;

    public InterruptConfirmationExceptionEntity() {
    }

    public List<ErrorHttpEntity> getErrors() {
        return errors;
    }

    public InterruptConfirmationMetaEntity getMeta() {
        return meta;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return Status;
    }
}
