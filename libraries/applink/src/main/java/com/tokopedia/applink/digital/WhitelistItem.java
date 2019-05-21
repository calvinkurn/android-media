package com.tokopedia.applink.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 04/01/18.
 */

public class WhitelistItem {
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("applink")
    @Expose
    public String applink;

    public WhitelistItem(String path, String applink) {
        this.path = path;
        this.applink = applink;
    }
}
