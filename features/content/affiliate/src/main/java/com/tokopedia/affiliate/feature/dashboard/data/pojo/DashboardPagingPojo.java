package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardPagingPojo {
    /**
     * nextCursor : AJlkjHKLJHj=
     */

    @SerializedName("nextCursor")
    @Expose
    private String nextCursor;

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
