package com.tokopedia.core.product.model.productdetail.discussion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangnadi on 8/22/17.
 */

public class DataCommentTalk {

    @SerializedName("list")
    @Expose
    private List<TalkComment> talkDetail = new ArrayList<TalkComment>();

    public List<TalkComment> getTalkDetail() {
        return talkDetail;
    }

    public void setTalkDetail(List<TalkComment> talkDetail) {
        this.talkDetail = talkDetail;
    }
}
