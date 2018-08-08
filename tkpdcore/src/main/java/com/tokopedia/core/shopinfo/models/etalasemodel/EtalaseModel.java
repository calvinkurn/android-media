
package com.tokopedia.core.shopinfo.models.etalasemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EtalaseModel {

    @SerializedName("list_other")
    @Expose
    public java.util.List<com.tokopedia.core.shopinfo.models.etalasemodel.ListOther> listOther = new ArrayList<com.tokopedia.core.shopinfo.models.etalasemodel.ListOther>();

    @SerializedName("list")
    @Expose
    public java.util.List<com.tokopedia.core.shopinfo.models.etalasemodel.List> list = new ArrayList<com.tokopedia.core.shopinfo.models.etalasemodel.List>();

}
