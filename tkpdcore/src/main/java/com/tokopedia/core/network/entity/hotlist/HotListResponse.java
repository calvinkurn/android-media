
package com.tokopedia.core.network.entity.hotlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HotListResponse {

    @SerializedName("process_time")
    @Expose
    private String processTime;
    @SerializedName("list")
    @Expose
    private java.util.List<com.tokopedia.core.network.entity.hotlist.List> list = null;

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }

    public java.util.List<com.tokopedia.core.network.entity.hotlist.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.tokopedia.core.network.entity.hotlist.List> list) {
        this.list = list;
    }

}
