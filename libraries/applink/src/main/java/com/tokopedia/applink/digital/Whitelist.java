package com.tokopedia.applink.digital;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rizky on 04/01/18.
 */

public class Whitelist {
    @SerializedName("data")
    @Expose
    public List<WhitelistItem> data;
}
