package com.tokopedia.topchat.chatlist.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by stevenfredian on 10/30/17.
 */

public class DeleteChatUiModel {

    @SerializedName("msg_id")
    @Expose
    private long msgId;
    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("detail_response")
    @Expose
    private String detailResponse;

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getDetailResponse() {
        return detailResponse;
    }

    public void setDetailResponse(String detailResponse) {
        this.detailResponse = detailResponse;
    }

}