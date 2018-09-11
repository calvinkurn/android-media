package com.tokopedia.mitra.homepage.domain.model;

import com.google.gson.annotations.SerializedName;

public class DynamicHomeIconData {
    @SerializedName("data")
    DynamicHomeIconWrapper wrapper;

    public DynamicHomeIconData() {
    }

    public DynamicHomeIconData(DynamicHomeIconWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public DynamicHomeIconWrapper getWrapper() {
        return wrapper;
    }
}
