package com.tokopedia.mitra.homepage.domain.model;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.mitra.homepage.adapter.HomepageCategoriesTypeFactory;

public class CategoryRow implements Visitable<HomepageCategoriesTypeFactory> {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name = "";
    @SerializedName("url")
    private String url = "";
    @SerializedName("imageUrl")
    private String imageUrl = "";
    @SerializedName("applinks")
    private String applinks = "";
    @SerializedName("type")
    private String type = "";
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("categoryLabel")
    private String categoryLabel = "";
    @SerializedName("score")
    private String score;

    public CategoryRow() {
    }

    public CategoryRow(int id, String name, String url, String imageUrl, String applinks, String type, int categoryId, String categoryLabel, String score) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.imageUrl = imageUrl;
        this.applinks = applinks;
        this.type = type;
        this.categoryId = categoryId;
        this.categoryLabel = categoryLabel;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getApplinks() {
        return applinks;
    }

    public String getType() {
        return type;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public String getScore() {
        return score;
    }

    @Override
    public int type(HomepageCategoriesTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }
}
