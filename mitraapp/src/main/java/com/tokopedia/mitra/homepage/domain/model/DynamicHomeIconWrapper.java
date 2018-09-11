package com.tokopedia.mitra.homepage.domain.model;

import com.google.gson.annotations.SerializedName;

public class DynamicHomeIconWrapper {
    @SerializedName("dynamicHomeIcon")
    DynamicHomeIcon dynamicIcon;

    public DynamicHomeIconWrapper(DynamicHomeIcon dynamicIcon) {
        this.dynamicIcon = dynamicIcon;
    }

    public DynamicHomeIconWrapper() {
    }

    public DynamicHomeIcon getDynamicIcon() {
        return dynamicIcon;
    }
}
