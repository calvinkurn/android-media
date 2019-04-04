
package com.tokopedia.digital.categorylist.data.cloud.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LayoutSection {

    @SerializedName("id")
    private Long mId;
    @SerializedName("layout_rows")
    private List<LayoutRow> mLayoutRows;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("weight")
    private Long mWeight;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public List<LayoutRow> getLayoutRows() {
        return mLayoutRows;
    }

    public void setLayoutRows(List<LayoutRow> layout_rows) {
        mLayoutRows = layout_rows;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Long getWeight() {
        return mWeight;
    }

    public void setWeight(Long weight) {
        mWeight = weight;
    }

}
