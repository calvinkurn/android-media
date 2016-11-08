
package com.tokopedia.core.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductModel {
    @SerializedName("list")
    @Expose
    public java.util.List<com.tokopedia.core.shopinfo.models.productmodel.List> list = new ArrayList<com.tokopedia.core.shopinfo.models.productmodel.List>();

}
