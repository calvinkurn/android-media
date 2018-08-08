
package com.tokopedia.core.network.entity.home.recentView;

import com.google.gson.annotations.SerializedName;

public class Header {

    @SerializedName("server_process_time")
    private String mServerProcessTime;
    @SerializedName("total_data")
    private String mTotalData;

    public String getServerProcessTime() {
        return mServerProcessTime;
    }

    public void setServerProcessTime(String server_process_time) {
        mServerProcessTime = server_process_time;
    }

    public String getTotalData() {
        return mTotalData;
    }

    public void setTotalData(String total_data) {
        mTotalData = total_data;
    }

}
