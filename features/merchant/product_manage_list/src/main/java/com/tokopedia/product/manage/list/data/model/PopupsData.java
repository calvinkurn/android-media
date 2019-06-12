package com.tokopedia.product.manage.list.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopupsData {
    @SerializedName("showPopUp")
    @Expose
    boolean isShowPopUp;

    public boolean isShowPopUp() {
        return isShowPopUp;
    }

    public void setShowPopUp(boolean showPopUp) {
        isShowPopUp = showPopUp;
    }
}
