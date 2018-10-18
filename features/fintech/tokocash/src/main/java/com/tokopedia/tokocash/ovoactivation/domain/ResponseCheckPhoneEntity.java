package com.tokopedia.tokocash.ovoactivation.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public class ResponseCheckPhoneEntity {

    @SerializedName("check_phone")
    @Expose
    private CheckPhoneOvoEntity checkPhoneOvoEntity;

    public CheckPhoneOvoEntity getCheckPhoneOvoEntity() {
        return checkPhoneOvoEntity;
    }
}
