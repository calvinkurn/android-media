package com.tokopedia.kolcommon.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/07/18.
 */
public class FollowKol {

    @SerializedName("data")
    private FollowKolData data;

    @SerializedName("error")
    private String error;

    public FollowKolData getData() {
        return data;
    }

    public void setData(FollowKolData data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
