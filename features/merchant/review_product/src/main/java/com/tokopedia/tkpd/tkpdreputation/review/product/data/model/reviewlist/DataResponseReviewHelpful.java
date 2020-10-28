package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 1/15/18.
 */

public class DataResponseReviewHelpful extends DataResponseReview {

    @SerializedName("list")
    @Expose
    private List<Review> list = null;

    public List<Review> getList() {
        return list;
    }

    public void setList(List<Review> list) {
        this.list = list;
    }

}
