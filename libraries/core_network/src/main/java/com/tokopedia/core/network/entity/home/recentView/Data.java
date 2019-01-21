
package com.tokopedia.core.network.entity.home.recentView;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("list")
    private List<RecentView> mRecentView;

    public List<RecentView> getRecentView() {
        return mRecentView;
    }

    public void setRecentView(List<RecentView> recent_view) {
        mRecentView = recent_view;
    }

}
