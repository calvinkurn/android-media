package com.tokopedia.core.product.model.productdetail.discussion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 8/21/17.
 */

public class DataTalk {

    @SerializedName("list")
    @Expose
    private List<Talk> talkList = new ArrayList<Talk>();

    public List<Talk> getTalkList() {
        return talkList;
    }

    public void setTalkList(List<Talk> talkList) {
        this.talkList = talkList;
    }
}
