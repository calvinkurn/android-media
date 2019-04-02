package com.tokopedia.tokocash.autosweepmf.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAutoSweepDetail {
    @Expose
    @SerializedName("mf_autosweep_detail")
    private AutoSweepDetailEntity mfAutoSweepDetail;

    public AutoSweepDetailEntity getMfAutoSweepDetail() {
        return mfAutoSweepDetail;
    }

    public void setMfAutoSweepDetail(AutoSweepDetailEntity mfAutoSweepDetail) {
        this.mfAutoSweepDetail = mfAutoSweepDetail;
    }

    @Override
    public String toString() {
        return "ResponseAutoSweepDetail{" +
                "mfAutoSweepDetail=" + mfAutoSweepDetail +
                '}';
    }
}
