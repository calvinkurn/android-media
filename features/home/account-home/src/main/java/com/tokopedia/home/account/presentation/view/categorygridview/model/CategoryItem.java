package com.tokopedia.home.account.presentation.view.categorygridview.model;

import android.support.annotation.DrawableRes;

/**
 * @author okasurya on 7/19/18.
 */
public class CategoryItem {
    @DrawableRes
    private int resourceId;
    private String url;
    private String description;


    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
