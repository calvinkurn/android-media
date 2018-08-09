package com.tokopedia.train.common.data.interceptor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrainError {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;

    public TrainError(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrainError && ((TrainError)obj).getId().equalsIgnoreCase(id);
    }
}
