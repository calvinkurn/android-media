package com.tokopedia.home.account.presentation.viewmodel;

import android.support.annotation.DrawableRes;

/**
 * @author okasurya on 7/19/18.
 */
public class MenuGridItemViewModel {
    @DrawableRes
    private int resourceId;
    private String imageUrl;
    private String description;
    private String applink;
    private int count;

    public MenuGridItemViewModel(int resourceId, String description, String applink, int count) {
        this.resourceId = resourceId;
        this.description = description;
        this.applink = applink;
        this.count = count;
    }

    public MenuGridItemViewModel(String imageUrl, String description, String applink, int count) {
        this.imageUrl = imageUrl;
        this.description = description;
        this.applink = applink;
        this.count = count;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
