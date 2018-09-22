package com.tokopedia.browse.homepage.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowseCategoryGroupEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("categoryRows")
    @Expose
    private List<DigitalBrowseCategoryRowEntity> categoryRow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<DigitalBrowseCategoryRowEntity> getCategoryRow() {
        return categoryRow;
    }

    public void setCategoryRow(List<DigitalBrowseCategoryRowEntity> categoryRow) {
        this.categoryRow = categoryRow;
    }
}
