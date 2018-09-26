package com.tokopedia.feedplus.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by milhamj on 17/07/18.
 */

public class FeedMeta {
    @SerializedName("has_next_page")
    @Expose
    private boolean hasNextPage;

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
