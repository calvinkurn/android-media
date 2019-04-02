package com.tokopedia.digital.widget.data.entity.operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class AttributesEntity {

    @SerializedName("additional_form")
    @Expose
    private List<Object> additionalForm = new ArrayList<Object>();
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("maximum_length")
    @Expose
    private int maximumLength;
    @SerializedName("minimum_length")
    @Expose
    private int minimumLength;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("prefix")
    @Expose
    private List<String> prefix = new ArrayList<String>();
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("rule")
    @Expose
    private RuleEntity rule;
    @SerializedName("default_product_id")
    @Expose
    private int defaultProductId;

    public List<Object> getAdditionalForm() {
        return additionalForm;
    }

    public void setAdditionalForm(List<Object> additionalForm) {
        this.additionalForm = additionalForm;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    public int getMinimumLength() {
        return minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public RuleEntity getRule() {
        return rule;
    }

    public void setRule(RuleEntity rule) {
        this.rule = rule;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public void setDefaultProductId(int defaultProductId) {
        this.defaultProductId = defaultProductId;
    }
}
