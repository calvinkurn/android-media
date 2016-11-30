
package com.tokopedia.core.database.recharge.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Version {

    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("operator")
    @Expose
    private Integer operator;
    @SerializedName("product")
    @Expose
    private Integer product;
    @SerializedName("minimum_android_build")
    @Expose
    private String minimumAndroidBuild;
    @SerializedName("minimum_ios_build")
    @Expose
    private String minimumIosBuild;

    /**
     * 
     * @return
     *     The category
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The operator
     */
    public Integer getOperator() {
        return operator;
    }

    /**
     * 
     * @param operator
     *     The operator
     */
    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    /**
     * 
     * @return
     *     The product
     */
    public Integer getProduct() {
        return product;
    }

    /**
     * 
     * @param product
     *     The product
     */
    public void setProduct(Integer product) {
        this.product = product;
    }

    /**
     * 
     * @return
     *     The minimumAndroidBuild
     */
    public String getMinimumAndroidBuild() {
        return minimumAndroidBuild;
    }

    /**
     * 
     * @param minimumAndroidBuild
     *     The minimum_android_build
     */
    public void setMinimumAndroidBuild(String minimumAndroidBuild) {
        this.minimumAndroidBuild = minimumAndroidBuild;
    }

    /**
     * 
     * @return
     *     The minimumIosBuild
     */
    public String getMinimumIosBuild() {
        return minimumIosBuild;
    }

    /**
     * 
     * @param minimumIosBuild
     *     The minimum_ios_build
     */
    public void setMinimumIosBuild(String minimumIosBuild) {
        this.minimumIosBuild = minimumIosBuild;
    }

}
