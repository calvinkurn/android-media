package com.tokopedia.core.common.category.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryViewModel implements Parcelable {
    private String name;
    private long id;
    private boolean hasChild;

    public CategoryViewModel(){

    }

    public CategoryViewModel(String name, long id, boolean hasChild) {
        this.name = name;
        this.id = id;
        this.hasChild = hasChild;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.id);
        dest.writeByte(this.hasChild ? (byte) 1 : (byte) 0);
    }

    protected CategoryViewModel(Parcel in) {
        this.name = in.readString();
        this.id = in.readLong();
        this.hasChild = in.readByte() != 0;
    }

    public static final Creator<CategoryViewModel> CREATOR = new Creator<CategoryViewModel>() {
        @Override
        public CategoryViewModel createFromParcel(Parcel source) {
            return new CategoryViewModel(source);
        }

        @Override
        public CategoryViewModel[] newArray(int size) {
            return new CategoryViewModel[size];
        }
    };
}
