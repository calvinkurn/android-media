
package com.tokopedia.topchat.chatlist.domain.pojo.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageData {

    @SerializedName("list")
    @Expose
    private List<ListMessage> list = null;
    @SerializedName("paging_next")
    @Expose
    private boolean hasNext;
    @SerializedName("time_machine_status")
    @Expose
    private int timeMachineStatus;

    public List<ListMessage> getList() {
        return list;
    }

    public void setList(List<ListMessage> list) {
        this.list = list;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getTimeMachineStatus() {
        return timeMachineStatus;
    }
}
