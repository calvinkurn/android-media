package com.tokopedia.core.database.model.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ricoharisin on 7/14/16.
 */
public class CategoryData implements Parcelable {

    @SerializedName("data")
    @Expose
    private List<Category> data;

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
    }

    public CategoryData() {
    }

    protected CategoryData(Parcel in) {
        this.data = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Parcelable.Creator<CategoryData> CREATOR = new Parcelable.Creator<CategoryData>() {
        @Override
        public CategoryData createFromParcel(Parcel source) {
            return new CategoryData(source);
        }

        @Override
        public CategoryData[] newArray(int size) {
            return new CategoryData[size];
        }
    };
}
