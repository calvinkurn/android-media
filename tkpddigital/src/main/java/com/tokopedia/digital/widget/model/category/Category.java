package com.tokopedia.digital.widget.model.category;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 9/14/17.
 */

public class Category implements Parcelable {

    private Attributes attributes;
    private int id;
    private String type;

    public Category() {
    }

    protected Category(Parcel in) {
        attributes = in.readParcelable(Attributes.class.getClassLoader());
        id = in.readInt();
        type = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(attributes, flags);
        dest.writeInt(id);
        dest.writeString(type);
    }
}
