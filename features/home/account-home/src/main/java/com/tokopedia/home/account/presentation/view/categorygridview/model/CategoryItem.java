package com.tokopedia.home.account.presentation.view.categorygridview.model;

import android.support.annotation.DrawableRes;

/**
 * @author okasurya on 7/19/18.
 */
public class CategoryItem {
    @DrawableRes
    private int resourceId;
    private String imageUrl;
    private String description;
    private String applink;


    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }
}
