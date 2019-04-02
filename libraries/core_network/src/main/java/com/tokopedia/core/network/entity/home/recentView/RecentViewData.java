package com.tokopedia.core.network.entity.home.recentView;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class RecentViewData {

    @SerializedName("data")
    private Data mData;
    @SerializedName("header")
    private Header mHeader;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public Header getHeader() {
        return mHeader;
    }

    public void setHeader(Header header) {
        mHeader = header;
    }

}
