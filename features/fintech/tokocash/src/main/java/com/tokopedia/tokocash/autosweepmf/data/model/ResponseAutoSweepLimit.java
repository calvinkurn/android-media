package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAutoSweepLimit {
    @Expose
    @SerializedName("mf_autosweep_update")
    private AutoSweepLimitEntity mfAutoSweepUpdate;

    public AutoSweepLimitEntity getMfAutoSweepUpdate() {
        return mfAutoSweepUpdate;
    }

    public void setMfAutoSweepUpdate(AutoSweepLimitEntity mfAutoSweepUpdate) {
        this.mfAutoSweepUpdate = mfAutoSweepUpdate;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepLimit{" +
                "mfAutoSweepUpdate=" + mfAutoSweepUpdate +
                '}';
    }
}
