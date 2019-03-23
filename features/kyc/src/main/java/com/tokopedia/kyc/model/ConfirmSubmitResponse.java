package com.tokopedia.kyc.model;

import com.google.gson.annotations.SerializedName;

public class ConfirmSubmitResponse {
    @SerializedName("goalKYCConfirm")
    KycImageUploadDataClass confirmSubmitResponse;

    public KycImageUploadDataClass getConfirmSubmitResponse() {
        return confirmSubmitResponse;
    }

    public void setConfirmSubmitResponse(KycImageUploadDataClass confirmSubmitResponse) {
        this.confirmSubmitResponse = confirmSubmitResponse;
    }
}
