package com.tokopedia.kolcommon.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/07/18.
 */
public class FollowKolData {

    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
