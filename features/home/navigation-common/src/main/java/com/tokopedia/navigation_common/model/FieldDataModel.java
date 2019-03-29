package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 17/09/18.
 */
public class FieldDataModel {

    @SerializedName("url")
    @Expose
    private String url = "";

    @SerializedName("preApp")
    @Expose
    private PreAppModel preApp = new PreAppModel();

    public String getUrl() {
        return url;
    }

    public PreAppModel getPreApp() {
        return preApp;
    }
}
