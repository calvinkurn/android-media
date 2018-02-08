package com.tokopedia.core.product.model.productdetail.discussion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alifa on 8/24/17.
 */

public class CommentCreateTimeList {

    @SerializedName("date_time_android")
    @Expose
    private String dateTimeAndroid;

    public String getDateTimeAndroid() {
        return dateTimeAndroid;
    }

    public void setDateTimeAndroid(String dateTimeAndroid) {
        this.dateTimeAndroid = dateTimeAndroid;
    }
}
