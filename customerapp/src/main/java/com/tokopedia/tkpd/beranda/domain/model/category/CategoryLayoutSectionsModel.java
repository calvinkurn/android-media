package com.tokopedia.tkpd.beranda.domain.model.category;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by errysuprayogi on 11/27/17.
 */
public class CategoryLayoutSectionsModel {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("weight")
    private int weight;
    @SerializedName("layout_rows")
    private List<CategoryLayoutRowModel> layoutRows;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<CategoryLayoutRowModel> getLayoutRows() {
        return layoutRows;
    }

    public void setLayoutRows(List<CategoryLayoutRowModel> layoutRows) {
        this.layoutRows = layoutRows;
    }

}
