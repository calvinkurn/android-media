package com.tokopedia.transactiondata.entity.response.cartlist.cartlistnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

public class OfficialStore {

    @SerializedName("is_official")
    @Expose
    private boolean isOfficial;
    @SerializedName("os_logo_url")
    @Expose
    private String osLogoUrl;

    public boolean isOfficial() {
        return isOfficial;
    }

    public String getOsLogoUrl() {
        return osLogoUrl;
    }
}
