
package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewCreateTime {

    @SerializedName("date_time_fmt1")
    @Expose
    private String dateTimeFmt1;

    public String getDateTimeFmt1() {
        return dateTimeFmt1;
    }

    public void setDateTimeFmt1(String dateTimeFmt1) {
        this.dateTimeFmt1 = dateTimeFmt1;
    }

}
