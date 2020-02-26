package com.tokopedia.topchat.chatlist.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stevenfredian on 10/30/17.
 */

public class DeleteChatListUiModel {

    @SerializedName("list")
    @Expose
    private List<DeleteChatUiModel> list;

    public List<DeleteChatUiModel> getList() {
        return list;
    }

    public void setList(List<DeleteChatUiModel> list) {
        this.list = list;
    }
}