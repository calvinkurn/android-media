
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Vishal Gupta 7th May, 2018
 */
public class Attributes {

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
    private List<String> prefix = null;
    @SerializedName("ussd")
    @Expose
    private String ussd;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("rule")
    @Expose
    private Rule rule;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("first_color")
    @Expose
    private String firstColor;
    @SerializedName("second_color")
    @Expose
    private String secondColor;
    @SerializedName("fields")
    @Expose
    private List<Field> fields = null;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subtitle")
    @Expose
    private String subTitle;
    @SerializedName("promocode")
    @Expose
    private String promocode;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("data_title")
    @Expose
    private String dataTitle;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastorderUrl() {
        return lastorderUrl;
    }

    public void setLastorderUrl(String lastorderUrl) {
        this.lastorderUrl = lastorderUrl;
    }

    public int getDefaultProductId() {
        return defaultProductId;
    }

    public void setDefaultProductId(int defaultProductId) {
        this.defaultProductId = defaultProductId;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public void setPrefix(List<String> prefix) {
        this.prefix = prefix;
    }

    public String getUssd() {
        return ussd;
    }

    public void setUssd(String ussd) {
        this.ussd = ussd;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(String firstColor) {
        this.firstColor = firstColor;
    }

    public String getSecondColor() {
        return secondColor;
    }

    public void setSecondColor(String secondColor) {
        this.secondColor = secondColor;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }
}
