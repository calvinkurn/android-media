package com.tokopedia.digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
