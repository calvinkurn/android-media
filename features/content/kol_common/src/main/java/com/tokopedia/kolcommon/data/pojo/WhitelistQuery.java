package com.tokopedia.kolcommon.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 20/06/18.
 */

public class WhitelistQuery {
    @SerializedName("feed_check_whitelist")
    @Expose
    private Whitelist whitelist;

    public Whitelist getWhitelist() {
        return whitelist;
    }
}
