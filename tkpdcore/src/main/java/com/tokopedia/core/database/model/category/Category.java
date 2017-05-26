
package com.tokopedia.core.database.model.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category implements Parcelable{

    @SerializedName("attributes")
    @Expose
    private CategoryAttributes attributes;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("type")
    @Expose
    private String type;

    public CategoryAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(CategoryAttributes attributes) {
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
        dest.writeParcelable(this.attributes, flags);
        dest.writeInt(this.id);
        dest.writeString(this.type);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.attributes = in.readParcelable(CategoryAttributes.class.getClassLoader());
        this.id = in.readInt();
        this.type = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
