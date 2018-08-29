
package com.tokopedia.imagepicker.picker.instagram.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListMediaInstagram {

    @SerializedName("data")
    @Expose
    private List<MediaInstagram> data = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    @SerializedName("meta")
    @Expose
    private Meta meta;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<MediaInstagram> getData() {
        return data;
    }

    public void setData(List<MediaInstagram> data) {
        this.data = data;
    }

}
