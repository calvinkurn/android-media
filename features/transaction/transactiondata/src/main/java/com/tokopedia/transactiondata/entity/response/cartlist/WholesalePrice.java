package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Irfan Khoirul on 23/05/18.
 */

public class WholesalePrice {

    @Expose
    @SerializedName("qty_min_fmt")
    private String qtyMinFmt = "";
    @Expose
    @SerializedName("qty_max_fmt")
    private String qtyMaxFmt = "";
    @Expose
    @SerializedName("prd_prc_fmt")
    private String prdPrcFmt = "";
    @Expose
    @SerializedName("qty_min")
    private int qtyMin;
    @Expose
    @SerializedName("qty_max")
    private int qtyMax;
    @Expose
    @SerializedName("prd_prc")
    private int prdPrc;

    public String getQtyMinFmt() {
        return qtyMinFmt;
    }

    public String getQtyMaxFmt() {
        return qtyMaxFmt;
    }

    public String getPrdPrcFmt() {
        return prdPrcFmt;
    }

    public int getQtyMin() {
        return qtyMin;
    }

    public int getQtyMax() {
        return qtyMax;
    }

    public int getPrdPrc() {
        return prdPrc;
    }
}
