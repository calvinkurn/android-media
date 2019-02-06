package com.tokopedia.core.shopinfo.models.etalasemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ListOther {

    @SerializedName("etalase_url")
    @Expose
    public String etalaseUrl;
    @SerializedName("etalase_id")
    @Expose
    public String etalaseId;
    @SerializedName("etalase_name")
    @Expose
    public String etalaseName;
    @SerializedName("use_ace")
    @Expose
    public int useAce;
    @SerializedName("etalase_badge")
    @Expose
    public String etalaseBadge;

}