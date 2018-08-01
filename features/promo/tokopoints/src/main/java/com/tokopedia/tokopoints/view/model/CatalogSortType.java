package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogSortType {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("text")
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CatalogSortType{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
