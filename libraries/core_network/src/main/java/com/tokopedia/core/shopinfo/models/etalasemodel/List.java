
package com.tokopedia.core.shopinfo.models.etalasemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class List {

    @SerializedName("etalase_id")
    @Expose
    public String etalaseId;
    @SerializedName("etalase_name")
    @Expose
    public String etalaseName;
    @SerializedName("etalase_num_product")
    @Expose
    public int etalaseNumProduct;
    @SerializedName("etalase_total_product")
    @Expose
    public int etalaseTotalProduct;
    @SerializedName("use_ace")
    @Expose
    public int useAce;
    @SerializedName("etalase_badge")
    @Expose
    public String etalaseBadge;
}
