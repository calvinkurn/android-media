package com.tokopedia.buyerorder.detail.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActionButtonList {

    @SerializedName("orderDetailTapAction")
    @Expose
    private List<ActionButton> actionButtonList;

    public List<ActionButton> getActionButtonList() {
        return actionButtonList;
    }

    public void setActionButtonList(List<ActionButton> actionButtonList) {
        this.actionButtonList = actionButtonList;
    }

}
