
package com.tokopedia.core.database.recharge.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("category")
    @Expose
    private int category;
    @SerializedName("operator")
    @Expose
    private int operator;
    @SerializedName("product")
    @Expose
    private int product;
    @SerializedName("minimum_android_build")
    @Expose
    private String minimumAndroidBuild;
    @SerializedName("minimum_ios_build")
    @Expose
    private String minimumIosBuild;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getOperator() {
        return operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public String getMinimumAndroidBuild() {
        return minimumAndroidBuild;
    }

    public void setMinimumAndroidBuild(String minimumAndroidBuild) {
        this.minimumAndroidBuild = minimumAndroidBuild;
    }

    public String getMinimumIosBuild() {
        return minimumIosBuild;
    }

    public void setMinimumIosBuild(String minimumIosBuild) {
        this.minimumIosBuild = minimumIosBuild;
    }
}
