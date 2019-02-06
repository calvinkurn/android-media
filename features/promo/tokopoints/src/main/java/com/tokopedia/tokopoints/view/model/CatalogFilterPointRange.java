package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogFilterPointRange {
    @SerializedName("id")
    private int id;

    @SerializedName("index")
    private int index;

    @SerializedName("maxPoint")
    private int maxPoint;

    @SerializedName("minPoint")
    private int minPoint;

    @SerializedName("myPoint")
    private int myPoint;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    public int getMinPoint() {
        return minPoint;
    }

    public void setMinPoint(int minPoint) {
        this.minPoint = minPoint;
    }

    public int getMyPoint() {
        return myPoint;
    }

    public void setMyPoint(int myPoint) {
        this.myPoint = myPoint;
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
                ", index=" + index +
                ", maxPoint=" + maxPoint +
                ", minPoint=" + minPoint +
                ", myPoint=" + myPoint +
                ", text='" + text + '\'' +
                '}';
    }
}
