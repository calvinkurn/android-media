package com.tokopedia.core.product.model.productotherace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 19/01/2016.
 */

@Deprecated
public class ProductOtherDataAce {
    private static final String TAG = ProductOtherDataAce.class.getSimpleName();

    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("data")
    @Expose
    private List<ProductOtherAce> productOthers = new ArrayList<ProductOtherAce>();

    public List<ProductOtherAce> getProductOthers() {
        return productOthers;
    }

    public void setProductOthers(List<ProductOtherAce> productOthers) {
        this.productOthers = productOthers;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
