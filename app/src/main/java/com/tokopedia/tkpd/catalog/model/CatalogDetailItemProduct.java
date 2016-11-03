package com.tokopedia.tkpd.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogDetailItemProduct implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("image_uri")
    @Expose
    private String imageUri;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("condition")
    @Expose
    private Integer condition;
    @SerializedName("preorder")
    @Expose
    private Integer preorder;
    @SerializedName("department_id")
    @Expose
    private Integer departmentId;

    protected CatalogDetailItemProduct(Parcel in) {
        id = in.readString();
        name = in.readString();
        uri = in.readString();
        imageUri = in.readString();
        price = in.readString();
        condition = in.readByte() == 0x00 ? null : in.readInt();
        preorder = in.readByte() == 0x00 ? null : in.readInt();
        departmentId = in.readByte() == 0x00 ? null : in.readInt();
    }

    public CatalogDetailItemProduct() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(uri);
        dest.writeString(imageUri);
        dest.writeString(price);
        if (condition == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(condition);
        }
        if (preorder == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(preorder);
        }
        if (departmentId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(departmentId);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CatalogDetailItemProduct> CREATOR = new Parcelable.Creator<CatalogDetailItemProduct>() {
        @Override
        public CatalogDetailItemProduct createFromParcel(Parcel in) {
            return new CatalogDetailItemProduct(in);
        }

        @Override
        public CatalogDetailItemProduct[] newArray(int size) {
            return new CatalogDetailItemProduct[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public Integer getPreorder() {
        return preorder;
    }

    public void setPreorder(Integer preorder) {
        this.preorder = preorder;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
