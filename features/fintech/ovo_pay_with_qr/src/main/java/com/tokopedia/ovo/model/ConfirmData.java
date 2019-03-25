package com.tokopedia.ovo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmData {
    @SerializedName("goalQRConfirm")
    @Expose
    private ImeiConfirmResponse response;

    public ImeiConfirmResponse getImeiConfirmResponse() {
        return response;
    }
}
