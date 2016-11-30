package com.tokopedia.sellerapp.home.model;

/**
 * @author by mady on 9/23/16.
 */
public class CategoryItemModel {


    public enum TYPE {
        CATEGORY,
        GIMMIC
    }

    public int getImageResouceId() {
        return imageResouceId;
    }

    private int imageResouceId;
    private String name;
    private String imageUrl;
    private String description;
    private String redirectUrl;
    private String depId;
    private TYPE type;




    public CategoryItemModel() {
    }

    public CategoryItemModel(String name, TYPE type, String redirectUrl) {
        this.name = name;
        this.type = type;
        this.redirectUrl = redirectUrl;
    }

    public CategoryItemModel(String name, int imageResourceId, TYPE type, String depId) {
        this.name = name;
        this.imageResouceId = imageResourceId;
        this.type = type;
        this.depId = depId;

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

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getDepId() {
        return depId;
    }
}
