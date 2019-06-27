package com.tokopedia.kolcommon.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 12/07/18.
 */
public class FollowKolQuery {
    @SerializedName("do_follow_kol")
    private FollowKol doFollowKol;

    public FollowKol getData() {
        return doFollowKol;
    }

    public void setData(FollowKol doFollowKol) {
        this.doFollowKol = doFollowKol;
    }
}
