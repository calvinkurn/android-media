
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
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;

    protected Category(Parcel in) {
        attributes = (CategoryAttributes) in.readValue(CategoryAttributes.class.getClassLoader());
        id = in.readByte() == 0x00 ? null : in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(attributes);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
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


    /**
     * 
     * @return
     *     The attributes
     */
    public CategoryAttributes getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param categoryAttributes
     *     The categoryAttributes
     */
    public void setAttributes(CategoryAttributes categoryAttributes) {
        this.attributes = categoryAttributes;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }


}
