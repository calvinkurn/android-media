
package com.tokopedia.tkpd.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductModel {
    @SerializedName("list")
    @Expose
    public java.util.List<com.tokopedia.tkpd.shopinfo.models.productmodel.List> list = new ArrayList<com.tokopedia.tkpd.shopinfo.models.productmodel.List>();

}
