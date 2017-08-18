
package com.tokopedia.core.network.entity.variant;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductVariant {

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variant_option")
    @Expose
    private List<VariantOption> variantOption = null;
    @SerializedName("variant_data")
    @Expose
    private List<VariantDatum> variantData = null;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<VariantOption> getVariantOption() {
        return variantOption;
    }

    public void setVariantOption(List<VariantOption> variantOption) {
        this.variantOption = variantOption;
    }

    public List<VariantDatum> getVariantData() {
        return variantData;
    }

    public void setVariantData(List<VariantDatum> variantData) {
        this.variantData = variantData;
    }

}
