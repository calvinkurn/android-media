/*
 * Created By Kulomady on 10/7/16 1:43 PM
 * Copyright (c) 2016. All Rights Reserved
 *
 * Last Modified 10/7/16 1:43 PM
 */

package com.tokopedia.core.network.entity.homeMenu;

/**
 * @author by mady on 9/23/16.
 */

@Deprecated
public class CategoryItemModel {


    private String name;
    private String imageUrl;
    private String description;
    private String redirectValue; // DepId or redirect url if category type is Digital
    private String categoryId;
    private String appLinks;
    private TYPE type;

    public CategoryItemModel() {

    }

    public CategoryItemModel(String name, String imageUrl, TYPE type, String redirectValue) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
        this.redirectValue = redirectValue;

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getRedirectValue() {
        return redirectValue;
    }

    public void setRedirectValue(String redirectValue) {
        this.redirectValue = redirectValue;
    }

    public String getAppLinks() {
        return appLinks;
    }

    public void setAppLinks(String appLinks) {
        this.appLinks = appLinks;
    }

    public enum TYPE {
        CATEGORY,
        GIMMIC,
        DIGITAL
    }
}
