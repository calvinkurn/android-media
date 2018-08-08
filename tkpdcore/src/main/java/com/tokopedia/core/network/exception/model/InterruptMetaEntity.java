package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/17/17.
 */

public class InterruptMetaEntity {
    /**
     * {
     * "href": "https://ride-staging.tokopedia.com/user/linkwallet?authorisateion=324234-sdd-234-234",
     * }
     */

    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }
}
