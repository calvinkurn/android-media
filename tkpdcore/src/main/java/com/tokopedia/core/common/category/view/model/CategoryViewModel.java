package com.tokopedia.core.common.category.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sebastianuskh on 4/4/17.
 */

public class CategoryViewModel implements Parcelable{
    private String name;
    private long id;
    private boolean hasChild;

    public CategoryViewModel(){

    }

    protected CategoryViewModel(Parcel in) {
        name = in.readString();
        id = in.readLong();
        hasChild = in.readByte() != 0;
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

    public static final Creator<CategoryViewModel> CREATOR = new Creator<CategoryViewModel>() {
        @Override
        public CategoryViewModel createFromParcel(Parcel in) {
            return new CategoryViewModel(in);
        }

        @Override
        public CategoryViewModel[] newArray(int size) {
            return new CategoryViewModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(id);
        dest.writeByte((byte) (hasChild ? 1 : 0));
    }
}
