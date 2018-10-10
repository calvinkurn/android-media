package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 31/08/18.
 */
public class ResponseProductAttributes {

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("detail")
    @Expose
    private String detail;

    @SerializedName("detail_url")
    @Expose
    private String detailUrl;

    @SerializedName("detail_url_text")
    @Expose
    private String detailUrlText;

    @SerializedName("desc")
    @Expose
    private String desc;

    @SerializedName("price")
    @Expose
    private String price;

//    @SerializedName("promo")
//    @Expose
//    private String promo;

    public String getInfo() {
        return info;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public String getDetailUrlText() {
        return detailUrlText;
    }

    public String getDesc() {
        return desc;
    }

    public String getPrice() {
        return price;
    }

}
