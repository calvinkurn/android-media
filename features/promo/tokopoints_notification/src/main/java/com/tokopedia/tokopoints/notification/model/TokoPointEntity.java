package com.tokopedia.tokopoints.notification.model;

import com.google.gson.annotations.SerializedName;

public class TokoPointEntity {

    @SerializedName("popupNotif")
    PopupNotification popupNotif;

    public PopupNotification getPopupNotif() {
        return popupNotif;
    }

    public void setPopupNotif(PopupNotification popupNotif) {
        this.popupNotif = popupNotif;
    }

    @Override
    public String toString() {
        return "TokoPointEntity{" +
                "popupNotif=" + popupNotif +
                '}';
    }
}
