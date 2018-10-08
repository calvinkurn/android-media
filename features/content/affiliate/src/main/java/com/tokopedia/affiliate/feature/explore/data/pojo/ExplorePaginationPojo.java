package com.tokopedia.affiliate.feature.explore.data.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExplorePaginationPojo {

    @SerializedName("nextCursor")
    private String nextCursor;

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
