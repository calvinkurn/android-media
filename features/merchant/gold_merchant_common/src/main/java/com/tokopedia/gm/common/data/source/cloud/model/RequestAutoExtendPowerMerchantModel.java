package com.tokopedia.gm.common.data.source.cloud.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestAutoExtendPowerMerchantModel {
    @SerializedName("auto_extend")
    private boolean autoExtend;

    public RequestAutoExtendPowerMerchantModel(boolean isAutoExtend) {
        this.autoExtend = isAutoExtend;
    }
}
