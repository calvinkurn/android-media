package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

public class AdditionalInfo {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("detail")
    @Expose
    private List<Detail> detail = null;


    public String getTitle() {
        return title;
    }

    public List<Detail> getDetail() {
        return detail;
    }
}