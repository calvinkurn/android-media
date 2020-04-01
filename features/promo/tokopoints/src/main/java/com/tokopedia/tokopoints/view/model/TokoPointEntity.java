package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.tokopoints.notification.model.PopupNotification;

public class TokoPointEntity {
    @Expose
    @SerializedName("resultStatus")
    private ResultStatusEntity resultStatus;

    @Expose
    @SerializedName("status")
    private TokoPointStatusEntity status;

    @Expose
    @SerializedName("sheetHowToGet")
    LobDetails lobs;

    public LobDetails getLobs() {
        return lobs;
    }

    public void setLobs(LobDetails lobs) {
        this.lobs = lobs;
    }


    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    public TokoPointStatusEntity getStatus() {
        return status;
    }

    public void setStatus(TokoPointStatusEntity status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TokoPointEntity{" +
                "resultStatus=" + resultStatus +
                ", status=" + status +
                ", lobs=" + lobs +
                '}';
    }
}
