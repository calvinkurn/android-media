package com.tokopedia.affiliate.feature.dashboard.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 19/09/18.
 */
public class DashboardItemPojo {
    /**
     * id : 1234
     * image : http://ecs.
     * name : The unbranded...
     * isActive : true
     * totalSold : 10 Dibeli
     * totalClick : 10 Klik
     * commission : Rp 50.000
     */

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("isActive")
    @Expose
    private boolean isActive;
    @SerializedName("totalSold")
    @Expose
    private String totalSold;
    @SerializedName("totalClick")
    @Expose
    private String totalClick;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("productCommission")
    @Expose
    private String productCommission;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(String totalSold) {
        this.totalSold = totalSold;
    }

    public String getTotalClick() {
        return totalClick;
    }

    public void setTotalClick(String totalClick) {
        this.totalClick = totalClick;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getProductCommission() {
        return productCommission;
    }

    public void setProductCommission(String productCommission) {
        this.productCommission = productCommission;
    }

}
