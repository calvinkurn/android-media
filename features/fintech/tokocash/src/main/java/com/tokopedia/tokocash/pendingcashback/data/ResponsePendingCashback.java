package com.tokopedia.tokocash.pendingcashback.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 01/10/18.
 */
public class ResponsePendingCashback {

    @SerializedName("goalPendingBalance")
    @Expose
    private PendingCashbackEntity pendingCashbackEntity;

    public PendingCashbackEntity getPendingCashbackEntity() {
        return pendingCashbackEntity;
    }
}
