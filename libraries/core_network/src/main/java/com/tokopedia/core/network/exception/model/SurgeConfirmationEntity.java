package com.tokopedia.core.network.exception.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 4/17/17.
 */

@Deprecated
public class SurgeConfirmationEntity {
    /**
     * "expires_at": 1492424173,
     * "href": "https://sandbox-api.uber.com/surge-confirmations/8222830a-1d10-447b-bdb5-27975a8caab0",
     * "multiplier": 2,
     * "surge_confirmation_id": "8222830a-1d10-447b-bdb5-27975a8caab0"
     */
    @SerializedName("expires_at")
    @Expose
    private long expiresAt;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("multiplier")
    @Expose
    private int multiplier;
    @SerializedName("surge_confirmation_id")
    @Expose
    private String surgeConfirmationId;

    public long getExpiresAt() {
        return expiresAt;
    }

    public String getHref() {
        return href;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public String getSurgeConfirmationId() {
        return surgeConfirmationId;
    }
}
