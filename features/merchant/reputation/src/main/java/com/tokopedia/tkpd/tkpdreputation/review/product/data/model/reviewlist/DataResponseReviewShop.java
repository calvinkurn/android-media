package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class DataResponseReviewShop extends DataResponseReview {
    @SerializedName("list")
    @Expose
    private List<ReviewShop> list = null;

    public List<ReviewShop> getList() {
        return list;
    }

    public void setList(List<ReviewShop> list) {
        this.list = list;
    }

}
