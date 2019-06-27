
package com.tokopedia.core.product.model.shopscore.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ShopScoreDetailItemServiceModel {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Value")
    @Expose
    private Integer value;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Color")
    @Expose
    private String color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
