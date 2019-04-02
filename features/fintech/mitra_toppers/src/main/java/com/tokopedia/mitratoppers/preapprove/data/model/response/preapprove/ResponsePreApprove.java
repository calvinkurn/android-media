package com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nakama on 23/01/18.
 */

public class ResponsePreApprove {
    @SerializedName("preapp")
    @Expose
    private Preapp preapp;
    @SerializedName("url")
    @Expose
    private String url;

    public Preapp getPreapp() {
        return preapp;
    }

    public String getUrl() {
        return url;
    }

}

