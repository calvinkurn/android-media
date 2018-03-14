package com.tokopedia.home.explore.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by errysuprayogi on 2/2/18.
 */
public class LayoutSections implements Parcelable {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("weight")
    private int weight;
    @SerializedName("layoutRows")
    private List<LayoutRows> layoutRows;

    protected LayoutSections(Parcel in) {
        id = in.readInt();
        title = in.readString();
        weight = in.readInt();
        layoutRows = in.createTypedArrayList(LayoutRows.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(weight);
        dest.writeTypedList(layoutRows);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LayoutSections> CREATOR = new Creator<LayoutSections>() {
        @Override
        public LayoutSections createFromParcel(Parcel in) {
            return new LayoutSections(in);
        }

        @Override
        public LayoutSections[] newArray(int size) {
            return new LayoutSections[size];
        }
    };

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

    public List<LayoutRows> getLayoutRows() {
        return layoutRows;
    }

    public void setLayoutRows(List<LayoutRows> layoutRows) {
        this.layoutRows = layoutRows;
    }

}
