package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/07/18.
 */
public class FollowKolQuery {
    @SerializedName("data")
    private FollowKol data;

    public FollowKol getData() {
        return data;
    }

    public void setData(FollowKol data) {
        this.data = data;
    }
}
