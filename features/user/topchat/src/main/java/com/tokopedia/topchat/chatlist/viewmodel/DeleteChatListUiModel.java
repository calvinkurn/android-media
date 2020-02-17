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
    private List<DeleteChatViewModel> list;

    public List<DeleteChatViewModel> getList() {
        return list;
    }

    public void setList(List<DeleteChatViewModel> list) {
        this.list = list;
    }
}