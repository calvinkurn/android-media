
package com.tokopedia.core.product.model.productdetail;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class ProductInstallment implements Parcelable {

    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("terms")
    @Expose
    private Terms terms;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Terms getTerms() {
        return terms;
    }

    public void setTerms(Terms terms) {
        this.terms = terms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductInstallment(){}

    protected ProductInstallment(Parcel in) {
        icon = in.readString();
        terms = (Terms) in.readValue(Terms.class.getClassLoader());
        name = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeValue(terms);
        dest.writeString(name);
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<ProductInstallment> CREATOR = new Creator<ProductInstallment>() {
        @Override
        public ProductInstallment createFromParcel(Parcel in) {
            return new ProductInstallment(in);
        }

        @Override
        public ProductInstallment[] newArray(int size) {
            return new ProductInstallment[size];
        }
    };


}
