package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class ResponseCrackResultEntity {

    @SerializedName("crack_result")
    @Expose
    private CrackResultEntity crackResultEntity;

    public CrackResultEntity getCrackResultEntity() {
        return crackResultEntity;
    }
}
