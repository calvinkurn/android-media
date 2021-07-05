package com.tokopedia.tkpd.tkpdreputation.data.pojo.likedislike;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by nisie on 9/29/17.
 */

public class GetLikeDislikePojo {


    @SerializedName("list")
    @Expose
    private List<LikeDislikeList> list = null;

    public List<LikeDislikeList> getList() {
        return list;
    }

    public void setList(List<LikeDislikeList> list) {
        this.list = list;
    }
}
