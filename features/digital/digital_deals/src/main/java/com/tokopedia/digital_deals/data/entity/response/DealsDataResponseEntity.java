package com.tokopedia.digital_deals.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealsDataResponseEntity {

    @SerializedName("home")
    @Expose
    private HomeResponseEntity home;

    public HomeResponseEntity getHome() {
        return home;
    }

    public void setHome(HomeResponseEntity home) {
        this.home = home;
    }

    @Override
    public String toString() {
        return home.toString();
    }
}
