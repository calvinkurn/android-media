package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogFilterPointRange {
    @SerializedName("id")
    private int id;

    @SerializedName("text")
    private String text;

    @Expose(serialize = false, deserialize = false)
    private boolean isSelected = false;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "CatalogFilterPointRange{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
