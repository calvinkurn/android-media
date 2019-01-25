package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 1/19/18.
 */

public class ProductCategory {
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_name_level1")
    @Expose
    private String categoryNameLevel1 = "";
    @SerializedName("category_name_level2")
    @Expose
    private String categoryNameLevel2 = "";
    @SerializedName("category_name_level3")
    @Expose
    private String categoryNameLevel3 = "";

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryNameLevel1() {
        return categoryNameLevel1;
    }

    public void setCategoryNameLevel1(String categoryNameLevel1) {
        this.categoryNameLevel1 = categoryNameLevel1;
    }

    public String getCategoryNameLevel2() {
        return categoryNameLevel2;
    }

    public void setCategoryNameLevel2(String categoryNameLevel2) {
        this.categoryNameLevel2 = categoryNameLevel2;
    }

    public String getCategoryNameLevel3() {
        return categoryNameLevel3;
    }

    public void setCategoryNameLevel3(String categoryNameLevel3) {
        this.categoryNameLevel3 = categoryNameLevel3;
    }
}
