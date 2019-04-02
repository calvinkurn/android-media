package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.common_digital.product.presentation.model.Rule;

import java.util.List;

/**
 * Created by Rizky on 31/08/18.
 */
public class ResponseOperatorAttributes {

    @SerializedName("default_product_id")
    @Expose
    private int defaultProductId;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("firstColor")
    @Expose
    private String firstColor;

    @SerializedName("secondColor")
    @Expose
    private String secondColor;

    @SerializedName("rule")
    @Expose
    private Rule rule;

    @SerializedName("prefix")
    @Expose
    private List<String> prefix;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("product")
    @Expose
    private List<ResponseProduct>product;

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public String getSecondColor() {
        return secondColor;
    }

    public Rule getRule() {
        return rule;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public String getDescription() {
        return description;
    }

    public List<ResponseProduct> getProduct() {
        return product;
    }

}
