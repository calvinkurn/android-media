
package com.tokopedia.core.network.entity.homeMenu;

import com.google.gson.annotations.SerializedName;

@Deprecated
public class Headers {

    @SerializedName("process_time")
    private Double mProcessTime;
    @SerializedName("total_data")
    private Long mTotalData;

    public Double getProcessTime() {
        return mProcessTime;
    }

    public void setProcessTime(Double process_time) {
        mProcessTime = process_time;
    }

    public Long getTotalData() {
        return mTotalData;
    }

    public void setTotalData(Long total_data) {
        mTotalData = total_data;
    }

}
