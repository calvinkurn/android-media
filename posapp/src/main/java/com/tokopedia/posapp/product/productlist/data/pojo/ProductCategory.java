package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/13/18.
 */

public class ProductCategory {
    @SerializedName("category_id")
    @Expose
    private String id;
    @SerializedName("category_full_name")
    @Expose
    private String fullName;
    @SerializedName("category_full_title")
    @Expose
    private String fullTitle;
    @SerializedName("category_breadcrumb_url")
    @Expose
    private String breadcrumbUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getBreadcrumbUrl() {
        return breadcrumbUrl;
    }

    public void setBreadcrumbUrl(String breadcrumbUrl) {
        this.breadcrumbUrl = breadcrumbUrl;
    }
}
