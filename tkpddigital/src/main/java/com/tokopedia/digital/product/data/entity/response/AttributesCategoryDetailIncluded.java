package com.tokopedia.digital.product.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class AttributesCategoryDetailIncluded {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("lastorder_url")
    @Expose
    private String lastorderUrl;
    @SerializedName("default_product_id")
    @Expose
    private int defaultProductId;
    @SerializedName("prefix")
    @Expose
    private List<String> prefix = new ArrayList<>();
    @SerializedName("fields")
    @Expose
    private List<Field> fields = new ArrayList<>();
    @SerializedName("product")
    @Expose
    private List<Product> product = new ArrayList<>();
    @SerializedName("rule")
    @Expose
    private Rule rule;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subtitle;
    @SerializedName("promocode")
    @Expose
    private String promocode;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("data_title")
    @Expose
    private String dataTitle;
    @SerializedName("ussd")
    @Expose
    private String ussdCode;

    public String getUssdCode() {
        return ussdCode;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getLastorderUrl() {
        return lastorderUrl;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Product> getProduct() {
        return product;
    }

    public Rule getRule() {
        return rule;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPromocode() {
        return promocode;
    }

    public String getLink() {
        return link;
    }

    public String getDataTitle() {
        return dataTitle;
    }
}
