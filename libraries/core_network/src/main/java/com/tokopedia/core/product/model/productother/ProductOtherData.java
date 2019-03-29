package com.tokopedia.core.product.model.productother;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 14/12/2015.
 */

@Deprecated
public class ProductOtherData {
    private static final String TAG = ProductOtherData.class.getSimpleName();

    @SerializedName("other_product")
    @Expose
    private List<ProductOther> productOthers = new ArrayList<ProductOther>();

    /**
     *
     * @return
     * The otherProduct
     */
    public List<ProductOther> getProductOthers() {
        return productOthers;
    }

    /**
     *
     * @param productOthers
     * The other_product
     */
    public void setProductOthers(List<ProductOther> productOthers) {
        this.productOthers = productOthers;
    }
}
